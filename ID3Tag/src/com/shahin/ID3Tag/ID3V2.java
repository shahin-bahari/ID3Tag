package com.shahin.ID3Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://id3.org/id3v2.4.0-structure
public class ID3V2 {

    public static final int ID3V2_MIN_HEADER_SIZE = 10;

    private int major;
    private int minor;
    private int size;
    private int flag;
    private Map<String, List<ID3V2Frame>> frames = new HashMap<>();

    public static ID3V2 getTag(byte[] header){
        if(header == null || header.length <10){
            return null;
        }
        if(header[0] == 'I' && header[1] == 'D' && header[2] == '3'){
            int major = header[3];
            if(major != 2 && major != 3 && major != 4){
                return null;
            }
            ID3V2 tag = new ID3V2();
            tag.major = major;
            tag.minor = header[4];
            tag.flag = header[5];
            tag.size = Utils.getTagSize(header,6);
            return tag;
        }
        return null;
    }

    public List<ID3V2Frame> getFrame(String key){
        return frames.get(key);
    }

    public void fetchFrames(FileStream fs) throws IOException {
        int pos = 10;
        while(pos < size){
            byte[] head = fs.read(10);
            if(head[0] == 0 && head[1] == 0 && head[2] == 0 && head[3] == 0){
                break;  // reach padding
            }
            String key = new String(head,0,4);
            int frameSize = major==3 ? Utils.getIntValue(head,4):Utils.getTagSize(head,4);
            flag = (head[8] & 0xFF) << 8 | (head[9] & 0xFF);
            ID3V2Frame frame = ID3V2FrameFactory.getFrame(major,fs,key,flag,frameSize);
            frames.computeIfAbsent(key,(k)-> new ArrayList<>()).add(frame);
            pos += frameSize + 10;
        }
    }

    public int getSize() {
        return size;
    }

}
