package com.shahin.ID3Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mp3File {

    private final FileStream fs;
    private ID3V2 v2Tag;
    private ID3V1 v1Tag;
    private MpegHeader mpegHeader;
    private int frameCount;
    private double bitrate;
    private final Map<Integer,Integer> bitrates = new HashMap<>();

    public Mp3File(String path) throws IOException {
        fs = new FileStream(path);
        init();
    }

    public ID3V2PictureFrame getEmbeddedImage(ID3V2PictureFrame.ImageType type){
        if(v2Tag != null){
            for(ID3V2Frame frame : v2Tag.getFrame("APIC")){
                if(frame instanceof ID3V2PictureFrame){
                    return (ID3V2PictureFrame) frame;
                }
            }
        }
        return null;
    }

    public List<ID3V2PictureFrame.ImageType> getEmbeddedImageTypes(){
        List<ID3V2PictureFrame.ImageType> types = new ArrayList<>();
        if(v2Tag != null){
            List<ID3V2Frame> pics = v2Tag.getFrame("APIC");
            if(pics != null){
                for(ID3V2Frame frame : pics){
                    if(frame instanceof ID3V2PictureFrame){
                        types.add(((ID3V2PictureFrame)frame).getImageType());
                    }
                }
            }
        }
        return types;
    }

    public String getTag(TagType tag){
        String res = null;
        if(v2Tag != null){
            List<ID3V2Frame> frs = v2Tag.getFrame(tag.getStr());
            if(frs != null && frs.size() > 0){
                ID3V2Frame frame = frs.get(0);
                if(frame instanceof ID3V2TextFrame){
                    res = ((ID3V2TextFrame)frame).getText();
                }else if(frame instanceof ID3V2CommFrame){
                    res = ((ID3V2CommFrame)frame).getText();
                }
            }
        }

        if(res == null && v1Tag != null){
            switch (tag){
                case ARTIST -> res = v1Tag.getArtist();
                case TITLE -> res = v1Tag.getTitle();
                case ALBUM -> res = v1Tag.getAlbum();
                case COMMENT -> res = v1Tag.getComment();
                case YEAR -> res = v1Tag.getYear();
                case GENRE -> res = ID3V1.GENRES[v1Tag.getGenre()];
                case TRACK -> res = "" + v1Tag.getTrack();
            }
        }
        if(res == null){
            switch (tag){
                case LENGTH -> res = getLength();
                case IS_VBR -> res = "" + isVbr();
                case BITRATE -> res = "" +getBitrate();
            }
        }
        return res;
    }

    private void init() throws IOException {
        v2Tag = ID3V2.getTag(fs.read(ID3V2.ID3V2_MIN_HEADER_SIZE));
        if(v2Tag == null){
            readV1Tag();
            return;
        }
        v2Tag.fetchFrames(fs);
        readMp3Frames();
        readV1Tag();
        int d = 2;
    }

    private void readMp3Frames() throws IOException {
        int pos = v2Tag.getSize() + 10;
        fs.syncBuffer(pos);
        byte last = fs.getByte();
        byte current = fs.getByte();
        pos += 2;
        while(pos < (fs.getSize() - 40)){
            if(frameCount > 10460){
                int a = 3;
            }
            if(last == (byte)0xFF && ((current) & 0xE0) == 0xE0){
                mpegHeader = new MpegHeader(Utils.makeInt(last,current,fs.getByte(), fs.getByte()));
                if(!mpegHeader.isInvalid()){
                    addBitrate(mpegHeader.getBitrate());
                    int frameLen = mpegHeader.getLengthInBytes() - 4;
                    fs.skip(frameLen);
                    pos += frameLen + 2;
                }else{
                    System.out.println("bad frame");
                }
                last = fs.getByte();
                current = fs.getByte();
                pos += 2;
                continue;
            }
            last = current;
            current = fs.getByte();
            pos++;
        }
    }

    private void readV1Tag() throws IOException {
        fs.syncBuffer(-128);
        v1Tag = ID3V1.getTag(fs.read(128));
    }

    private void addBitrate(final int bitrate) {
        frameCount++;
        Integer count = bitrates.get(bitrate);
        if (count != null) {
            count++;
        } else {
            count = 1;
        }
        bitrates.put(bitrate, count);
        this.bitrate = ((this.bitrate * (frameCount - 1)) + bitrate) / frameCount;
    }

    private String getLength(){
        return ""+ getLengthMs();
    }

    public int getLengthMs(){
        try {
            long length = fs.getSize() - ((v1Tag == null)?0:128) - ((v2Tag == null)?0: v2Tag.getSize()) ;
            length *= 8;
            length /= bitrate;
            length *= (mpegHeader.getChannelMode()== MpegHeader.CHANNEL_MODE.MONO)?2:1;
            return (int) length;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean isVbr(){
        return bitrates.size()>1;
    }

    private double getBitrate(){
        return bitrate;
    }

    public enum TagType{
        ARTIST("TPE1"),
        BAND("TPE2"),
        ALBUM("TALB"),
        TITLE("TIT2"),
        DESCRIPTION("TIT3"),
        YEAR("TYER"),
        TRACK("TRCK"),
        LENGTH("TLEN"),
        COMPOSER("TCOM"),
        PUBLISHER("TPUB"),
        GENRE("TCON"),
        BITRATE(""),
        IS_VBR(""),
        COMMENT("COMM");

        private final String str;
        TagType(String str){
            this.str = str;
        }

        public String getStr() {
            return str;
        }
    }
}
