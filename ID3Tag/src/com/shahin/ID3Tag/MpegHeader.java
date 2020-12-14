package com.shahin.ID3Tag;

public class MpegHeader {

    public enum MPEG_VERSION{
        MPEG_VERSION_1_0("1.0"),
        MPEG_VERSION_2_0("2.0"),
        MPEG_VERSION_2_5("2.5");

        private final String str;
        MPEG_VERSION(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }

    public enum CHANNEL_MODE{
        MONO("Mono"),
        DUAL_MONO("Dual mono"),
        JOINT_STEREO("Joint stereo"),
        STEREO("Stereo");

        private final String str;
        CHANNEL_MODE(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }


    public static final String EMPHASIS_NONE = "None";
    public static final String EMPHASIS__50_15_MS = "50/15 ms";
    public static final String EMPHASIS_CCITT_J_17 = "CCITT J.17";

    public static final String MODE_EXTENSION_BANDS_4_31 = "Bands 4-31";
    public static final String MODE_EXTENSION_BANDS_8_31 = "Bands 8-31";
    public static final String MODE_EXTENSION_BANDS_12_31 = "Bands 12-31";
    public static final String MODE_EXTENSION_BANDS_16_31 = "Bands 16-31";
    public static final String MODE_EXTENSION_NONE = "None";
    public static final String MODE_EXTENSION_INTENSITY_STEREO = "Intensity stereo";
    public static final String MODE_EXTENSION_M_S_STEREO = "M/S stereo";
    public static final String MODE_EXTENSION_INTENSITY_M_S_STEREO = "Intensity & M/S stereo";
    public static final String MODE_EXTENSION_NA = "n/a";

    private boolean invalid = false;
    private MPEG_VERSION version;
    private int layer;
    private boolean isProtected;
    private int bitrate;
    private int sampleRate;
    private boolean padding;
    private boolean privat;
    private CHANNEL_MODE channelMode;
    private String modeExtension;
    private boolean copyright;
    private boolean original;
    private String emphasis;

    public MpegHeader(long header){
        int BITMASK_FRAME_SYNC = 0xFFE00000;
        int FRAME_SYNC = 0x7FF;
        if(getValue(header, BITMASK_FRAME_SYNC) != FRAME_SYNC){
            invalid = true;
            return;
        }
        int BITMASK_VERSION = 0x180000;
        setVersion(getValue(header, BITMASK_VERSION));
        int BITMASK_LAYER = 0x60000;
        setLayer(getValue(header, BITMASK_LAYER));
        int BITMASK_PROTECTION = 0x10000;
        setIsProtected(getValue(header, BITMASK_PROTECTION));
        int BITMASK_BITRATE = 0xF000;
        setBitrate(getValue(header, BITMASK_BITRATE));
        int BITMASK_SAMPLE_RATE = 0xC00;
        setSampleRate(getValue(header, BITMASK_SAMPLE_RATE));
        int BITMASK_PADDING = 0x200;
        setPadding(getValue(header, BITMASK_PADDING));
        int BITMASK_PRIVATE = 0x100;
        setPrivate(getValue(header, BITMASK_PRIVATE));
        int BITMASK_CHANNEL_MODE = 0xC0;
        setChannelMode(getValue(header, BITMASK_CHANNEL_MODE));
        int BITMASK_MODE_EXTENSION = 0x30;
        setModeExtension(getValue(header, BITMASK_MODE_EXTENSION));
        int BITMASK_COPYRIGHT = 0x8;
        setCopyright(getValue(header, BITMASK_COPYRIGHT));
        int BITMASK_ORIGINAL = 0x4;
        setOriginal(getValue(header, BITMASK_ORIGINAL));
        int BITMASK_EMPHASIS = 0x3;
        setEmphasis(getValue(header, BITMASK_EMPHASIS));
    }

    private int getValue(long base,long mask){
        int shift = 0;
        for(int i = 0 ; i < 32; i++){
            if(((mask>>i)&1)!=0){
                shift = i;
                break;
            }
        }
        return (int) ((base >> shift) & (mask>>shift));
    }

    private void setVersion(int value){
        switch (value) {
            case 3 -> version = MPEG_VERSION.MPEG_VERSION_1_0;
            case 2 -> version = MPEG_VERSION.MPEG_VERSION_2_0;
            case 0 -> version = MPEG_VERSION.MPEG_VERSION_2_5;
            default -> invalid = true;
        }
    }

    private void setLayer(int value){
        switch(value){
            case 1 -> layer = 3;
            case 2 -> layer = 2;
            case 3 -> layer = 1;
            default -> invalid = true;
        }
    }
    private void setIsProtected(int value){
        isProtected = (value==1);
    }

    private void setBitrate(int value){
        if (MPEG_VERSION.MPEG_VERSION_1_0 == version) {
            if (layer == 1) {
                switch (value) {
                    case 1 -> this.bitrate = 32;
                    case 2 -> this.bitrate = 64;
                    case 3 -> this.bitrate = 96;
                    case 4 -> this.bitrate = 128;
                    case 5 -> this.bitrate = 160;
                    case 6 -> this.bitrate = 192;
                    case 7 -> this.bitrate = 224;
                    case 8 -> this.bitrate = 256;
                    case 9 -> this.bitrate = 288;
                    case 10 -> this.bitrate = 320;
                    case 11 -> this.bitrate = 352;
                    case 12 -> this.bitrate = 384;
                    case 13 -> this.bitrate = 416;
                    case 14 -> this.bitrate = 448;
                    default -> invalid = true;
                }
            } else if (layer == 2) {
                switch (value) {
                    case 1 -> this.bitrate = 32;
                    case 2 -> this.bitrate = 48;
                    case 3 -> this.bitrate = 56;
                    case 4 -> this.bitrate = 64;
                    case 5 -> this.bitrate = 80;
                    case 6 -> this.bitrate = 96;
                    case 7 -> this.bitrate = 112;
                    case 8 -> this.bitrate = 128;
                    case 9 -> this.bitrate = 160;
                    case 10 -> this.bitrate = 192;
                    case 11 -> this.bitrate = 224;
                    case 12 -> this.bitrate = 256;
                    case 13 -> this.bitrate = 320;
                    case 14 -> this.bitrate = 384;
                    default -> invalid = true;
                }
            } else if (layer == 3) {
                switch (value) {
                    case 1 -> this.bitrate = 32;
                    case 2 -> this.bitrate = 40;
                    case 3 -> this.bitrate = 48;
                    case 4 -> this.bitrate = 56;
                    case 5 -> this.bitrate = 64;
                    case 6 -> this.bitrate = 80;
                    case 7 -> this.bitrate = 96;
                    case 8 -> this.bitrate = 112;
                    case 9 -> this.bitrate = 128;
                    case 10 -> this.bitrate = 160;
                    case 11 -> this.bitrate = 192;
                    case 12 -> this.bitrate = 224;
                    case 13 -> this.bitrate = 256;
                    case 14 -> this.bitrate = 320;
                    default -> invalid = true;
                }
            }
        } else{
            if (layer == 1) {
                switch (value) {
                    case 1 -> this.bitrate = 32;
                    case 2 -> this.bitrate = 48;
                    case 3 -> this.bitrate = 56;
                    case 4 -> this.bitrate = 64;
                    case 5 -> this.bitrate = 80;
                    case 6 -> this.bitrate = 96;
                    case 7 -> this.bitrate = 112;
                    case 8 -> this.bitrate = 128;
                    case 9 -> this.bitrate = 144;
                    case 10 -> this.bitrate = 160;
                    case 11 -> this.bitrate = 176;
                    case 12 -> this.bitrate = 192;
                    case 13 -> this.bitrate = 224;
                    case 14 -> this.bitrate = 256;
                    default -> invalid = true;
                }
            } else if (layer == 2 || layer == 3) {
                switch (value) {
                    case 1 -> this.bitrate = 8;
                    case 2 -> this.bitrate = 16;
                    case 3 -> this.bitrate = 24;
                    case 4 -> this.bitrate = 32;
                    case 5 -> this.bitrate = 40;
                    case 6 -> this.bitrate = 48;
                    case 7 -> this.bitrate = 56;
                    case 8 -> this.bitrate = 64;
                    case 9 -> this.bitrate = 80;
                    case 10 -> this.bitrate = 96;
                    case 11 -> this.bitrate = 112;
                    case 12 -> this.bitrate = 128;
                    case 13 -> this.bitrate = 144;
                    case 14 -> this.bitrate = 160;
                    default -> invalid = true;
                }
            }
        }
    }

    private void setSampleRate(int value) {
        if (MPEG_VERSION.MPEG_VERSION_1_0 == version) {
            switch (value) {
                case 0 -> this.sampleRate = 44100;
                case 1 -> this.sampleRate = 48000;
                case 2 -> this.sampleRate = 32000;
                default -> invalid = true;
            }
        } else if (MPEG_VERSION.MPEG_VERSION_2_0 == version) {
            switch (value) {
                case 0 -> this.sampleRate = 22050;
                case 1 -> this.sampleRate = 24000;
                case 2 -> this.sampleRate = 16000;
                default -> invalid = true;
            }
        } else if (MPEG_VERSION.MPEG_VERSION_2_5 == version) {
            switch (value) {
                case 0 -> this.sampleRate = 11025;
                case 1 -> this.sampleRate = 12000;
                case 2 -> this.sampleRate = 8000;
                default -> invalid = true;
            }
        }
    }

    private void setPadding(int paddingBit) {
        this.padding = (paddingBit == 1);
    }

    private void setPrivate(int privateBit) {
        this.privat = (privateBit == 1);
    }

    private void setChannelMode(int channelMode) {
        switch (channelMode) {
            case 0 -> this.channelMode = CHANNEL_MODE.STEREO;
            case 1 -> this.channelMode = CHANNEL_MODE.JOINT_STEREO;
            case 2 -> this.channelMode = CHANNEL_MODE.DUAL_MONO;
            case 3 -> this.channelMode = CHANNEL_MODE.MONO;
            default -> invalid = true;
        }
    }

    private void setModeExtension(int value){
        if (CHANNEL_MODE.JOINT_STEREO != channelMode) {
            this.modeExtension = MODE_EXTENSION_NA;
        } else {
            if (layer == 1 || layer == 2) {
                switch (value) {
                    case 0 -> this.modeExtension = MODE_EXTENSION_BANDS_4_31;
                    case 1 -> this.modeExtension = MODE_EXTENSION_BANDS_8_31;
                    case 2 -> this.modeExtension = MODE_EXTENSION_BANDS_12_31;
                    case 3 -> this.modeExtension = MODE_EXTENSION_BANDS_16_31;
                    default -> invalid = true;
                }
            } else if (layer == 3) {
                switch (value) {
                    case 0 -> this.modeExtension = MODE_EXTENSION_NONE;
                    case 1 -> this.modeExtension = MODE_EXTENSION_INTENSITY_STEREO;
                    case 2 -> this.modeExtension = MODE_EXTENSION_M_S_STEREO;
                    case 3 -> this.modeExtension = MODE_EXTENSION_INTENSITY_M_S_STEREO;
                    default -> invalid = true;
                }
            }
        }
    }

    private void setCopyright(int copyrightBit) {
        this.copyright = (copyrightBit == 1);
    }

    private void setOriginal(int originalBit) {
        this.original = (originalBit == 1);
    }

    private void setEmphasis(int emphasis){
        switch (emphasis) {
            case 0 -> this.emphasis = EMPHASIS_NONE;
            case 1 -> this.emphasis = EMPHASIS__50_15_MS;
            case 3 -> this.emphasis = EMPHASIS_CCITT_J_17;
            default -> invalid = true;
        }
    }

    public int getLengthInBytes() {
        long length;
        int pad;
        if (padding) pad = 1;
        else pad = 0;
        if (layer == 1) {
            length = ((48000L * bitrate) / sampleRate) + (pad * 4);
        } else {
            length = ((144000L * bitrate) / sampleRate) + pad;
        }
        return (int) length;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public MPEG_VERSION getVersion() {
        return version;
    }

    public int getLayer() {
        return layer;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public boolean isPadding() {
        return padding;
    }

    public boolean isPrivat() {
        return privat;
    }

    public CHANNEL_MODE getChannelMode() {
        return channelMode;
    }

    public String getModeExtension() {
        return modeExtension;
    }

    public boolean isCopyright() {
        return copyright;
    }

    public boolean isOriginal() {
        return original;
    }

    public String getEmphasis() {
        return emphasis;
    }
}
