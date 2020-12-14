package com.shahin.ID3Tag;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ID3V2CommFrame extends ID3V2Frame{

    private String text;
    private String brief;
    private String language;

    public ID3V2CommFrame(int version,String id, int flag, int size) {
        super(version,id, flag, size);
    }

    @Override
    protected void readData(FileStream fs) throws IOException {
        byte[] data = fs.read(size);
        int startOffset = (data[0] == 1 || data[0] == 3)? 2 : 0;
        Charset charset = StandardCharsets.ISO_8859_1;
        switch (data[0]){
            case 1 -> charset = StandardCharsets.UTF_16;
            case 2 -> charset = StandardCharsets.UTF_16BE;
            case 3 -> charset = StandardCharsets.UTF_8;
        }
        if(version < 4){
            charset = StandardCharsets.ISO_8859_1;
        }
        int pos = 1;
        language = new String(data,pos,3,charset);
        pos += 3;
        int len =0;
        while(data[pos++] != 0){
            len++;
        }
        brief = new String(data,pos,len,charset);
        pos += len + 1;

        if(pos >= data.length){
            text = "";
            return;
        }
        if((flag & (byte)0x80) == 0){
            byte[] temp = Utils.synchroniseBuffer(data);
            int endOffset = Utils.findNullTerminator(temp);
            endOffset = Math.max(endOffset, 0);
            len = temp.length - pos - startOffset - endOffset;
            text = new String(temp,startOffset + pos, len, charset);
        }else{
            int endOffset = Utils.findNullTerminator(data);
            endOffset = Math.max(endOffset, 0);
            len = data.length - pos - startOffset - endOffset;
            text = new String(data,startOffset + pos,len, charset);
        }
    }

    public String getText() {
        return text;
    }
}
