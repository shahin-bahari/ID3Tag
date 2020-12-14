package com.shahin.ID3Tag;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ID3V2TextFrame extends ID3V2Frame {

    private String str;

    public ID3V2TextFrame(int version,String id, int flag, int size) {
        super(version,id, flag, size);
    }

    @Override
    protected void readData(FileStream fs) throws IOException {
        byte[] data = fs.read(size);
        int startOffset = (data[0] == 1 || data[0] == 3)? 3 : 1;
        Charset charset = StandardCharsets.ISO_8859_1;
        switch (data[0]){
            case 1 -> charset = StandardCharsets.UTF_16;
            case 2 -> charset = StandardCharsets.UTF_16BE;
            case 3 -> charset = StandardCharsets.UTF_8;
        }
        if(version < 4){
            charset = StandardCharsets.ISO_8859_1;
        }

        if((flag & (byte)0x80) == 0){
            byte[] temp = Utils.synchroniseBuffer(data);
            int endOffset = Utils.findNullTerminator(temp);
            endOffset = Math.max(endOffset, 0);
            str = new String(temp,startOffset, temp.length -startOffset- endOffset, charset);
        }else{
            int endOffset = Utils.findNullTerminator(data);
            endOffset = Math.max(endOffset, 0);
            str = new String(data,startOffset,data.length - startOffset - endOffset, charset);
        }
    }

    public String getText() {
        return str;
    }
}
