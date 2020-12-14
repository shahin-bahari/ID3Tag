package com.shahin.ID3Tag;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ID3V2PictureFrame extends ID3V2Frame{

    public enum ImageType{
        Other(0),
        FileIcon(1),
        OtherFileIcon(2),
        CoverFront(3),
        CoverBack(4),
        LeafletPage(5),
        Media(6),
        Lead(7),
        Artist(8),
        Conductor(9),
        Band(10),
        Composer(11),
        Lyricist(12),
        RecordingLocation(13),
        DuringRecording(14),
        DuringPerformance(15),
        videoScreenCapture(16),
        BrightColouredFish(17),
        Illustration(18),
        BandLogo(19),
        Publisher(20);

        private final int code;
        ImageType(int code){
            this.code = code;
        }

        public static ImageType getType(int code){
            for(ImageType it :ImageType.values()){
                if(it.code == code){
                    return it;
                }
            }
            return Other;
        }
    }

    private byte[] image;
    private String mimeType;
    private ImageType imageType;
    private String description;

    public ID3V2PictureFrame(int version,String id, int flag, int size) {
        super(version,id, flag, size);
    }

    @Override
    protected void readData(FileStream fs) throws IOException {
        byte encoding = fs.getByte();
        int metaLen = 1;
        ArrayList<Byte> temp = new ArrayList<>();
        byte b = fs.getByte();
        while(b != 0){
            temp.add(b);
            b = fs.getByte();
        }
        byte[] m = new byte[temp.size()];
        for(int i = 0 ; i < temp.size() ; i++){
            m[i] = temp.get(i);
        }
        metaLen += m.length + 1;
        mimeType = new String(m, StandardCharsets.UTF_8);
        temp.clear();
        imageType = ImageType.getType(fs.getByte());
        b = fs.getByte();
        metaLen++;
        while(b != 0){
            temp.add(b);
            b = fs.getByte();
        }
        m = new byte[temp.size()];
        for(int i = 0 ; i < temp.size() ; i++){
            m[i] = temp.get(i);
        }
        metaLen += m.length + m.length == 0 ? 1 : 2;
        description = new String(m,StandardCharsets.ISO_8859_1);
        image = fs.read(size - metaLen);
    }

    public byte[] getImage() {
        return image;
    }

    public String getMimeType() {
        return mimeType;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public String getDescription() {
        return description;
    }
}
