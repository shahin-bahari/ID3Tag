package com.shahin.ID3Tag;

import java.io.IOException;

public class ID3V2FrameFactory {

    public static ID3V2Frame getFrame(int version,FileStream fs,String id, int flag, int size) throws IOException {
        ID3V2Frame frame = null;
        if(id.startsWith("T") || "UFID".equals(id)){
            frame = new ID3V2TextFrame(version,id,flag,size);
        }else if("APIC".equals(id)){
            frame = new ID3V2PictureFrame(version,id,flag,size);
        }else if("COMM".equals(id)){
            frame = new ID3V2CommFrame(version,id,flag,size);
        }
        if(frame != null){
            frame.readData(fs);
        }else{
            fs.skip(size);
            System.out.println(id);
        }
        return frame;
    }

}
