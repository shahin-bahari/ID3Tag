package com.shahin.ID3Tag;

public class Utils {

    public static int getTagSize(byte[] buffer,int pos){
        if(buffer == null || buffer.length + 4 < pos){
            return -1;
        }
        int res = buffer[pos+3] & 0x7F;
        res += ((buffer[pos+2] & 0x7F) << 7);
        res += ((buffer[pos+1] & 0x7F) << 14);
        res += ((buffer[pos] & 0x7F) << 21);
        return res;
    }

    public static int getIntValue(byte[] buffer,int pos){
        if(buffer == null || buffer.length + 4 < pos){
            return -1;
        }
        int res = buffer[pos+3] & 0xFF;
        res += ((buffer[pos+2] & 0xFF) << 8);
        res += ((buffer[pos+1] & 0xFF) << 16);
        res += ((buffer[pos] & 0xFF) << 24);
        return res;
    }

    public static long makeInt(byte a,byte b, byte c, byte d){
        long res = d & 0xFF;
        res += ((c & 0xFF) << 8);
        res += ((b & 0xFF) << 16);
        res += ((long)(a & 0xFF) << 24);
        return res;
    }


    private static int sizeSynchronisationWouldSubtract(byte[] bytes) {
        int count = 0;
        for (int i = 0; i < bytes.length - 2; i++) {
            if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
                count++;
            }
        }
        if (bytes.length > 1 && bytes[bytes.length - 2] == (byte) 0xff && bytes[bytes.length - 1] == 0) count++;
        return count;
    }

    public static byte[] synchroniseBuffer(byte[] bytes) {
        // synchronisation is replacing instances of:
        // 11111111 00000000 111xxxxx with 11111111 111xxxxx and
        // 11111111 00000000 00000000 with 11111111 00000000
        int count = sizeSynchronisationWouldSubtract(bytes);
        if (count == 0) return bytes;
        byte[] newBuffer = new byte[bytes.length - count];
        int i = 0;
        for (int j = 0; j < newBuffer.length - 1; j++) {
            newBuffer[j] = bytes[i];
            if (bytes[i] == (byte) 0xff && bytes[i + 1] == 0 && ((bytes[i + 2] & (byte) 0xe0) == (byte) 0xe0 || bytes[i + 2] == 0)) {
                i++;
            }
            i++;
        }
        newBuffer[newBuffer.length - 1] = bytes[i];
        return newBuffer;
    }

    public static int findNullTerminator(byte[] data){
        int pos = 0;
        boolean find = false;
        for(byte b : data){
            if(b == 0){
                if(find){
                    return pos-1;
                }
                find = true;
                pos++;
                continue;
            }
            find= false;
            pos++;
        }
        return -1;
    }
}
