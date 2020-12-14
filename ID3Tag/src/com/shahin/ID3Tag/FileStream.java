package com.shahin.ID3Tag;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileStream {

    public final static int BUFFER_SIZE = 8 * 1024;
    private final byte[] buffer;
    private final FileChannel fc;
    private final InputStream inStream;
    private int pos = 0;
    private int availableData = 0;
    private boolean eof = false;

    public FileStream(Path path) throws IOException {
        fc = FileChannel.open(path, StandardOpenOption.READ);
        buffer = new byte[BUFFER_SIZE];
        inStream = null;
        checkBuffer();
    }

    public FileStream(String path) throws IOException {
        this(Paths.get(path));
    }



    public FileStream(File file) throws IOException {
        this(file.toPath());
    }

    public FileStream(InputStream inStream){
        this.inStream = inStream;
        fc = null;
        buffer = null;
    }

    public long getInt() throws IOException {
        byte a = getByte();
        byte b = getByte();
        byte c = getByte();
        byte d = getByte();
        return (a << 24) | (b << 16) | (c << 8) | d;
    }

    public int getSize() throws IOException {
        int s = (int) fc.size();
        return s;
    }

    public byte[] read(int len) throws IOException {
        byte[] res = new byte[len];
        int readSize = 0;
        while(readSize < len){
            int size = Math.min(getBufferRemaining(),len-readSize);
            System.arraycopy(buffer,pos,res,readSize,size);
            pos += size;
            readSize += size;
            checkBuffer();
        }
        return res;
    }

    public void skip(int len) throws IOException {
        int readSize = 0;
        while(readSize < len){
            int size = Math.min(getBufferRemaining(),len-readSize);
            pos += size;
            readSize += size;
            checkBuffer();
        }
    }

    public void syncBuffer(int pos) throws IOException {
        if(pos < 0){
            pos = (int) (fc.size() +pos);
        }
        fc.position(pos);
        this.pos = availableData;
        checkBuffer();
    }

    private int getBufferRemaining(){
        return availableData - pos;
    }

    public byte getByte() throws IOException {
        checkBuffer();
        if(inStream == null){
            return buffer[pos++];
        }else{
            int res = inStream.read();
            if(res>0){
                return (byte)res;
            }else{
                throw new IOError(new Throwable("reached end of file"));
            }
        }
    }

    private void checkBuffer() throws IOException {
        if(inStream == null && pos == availableData){
            pos = 0;
            availableData = fc.read(ByteBuffer.wrap(buffer));
            if(availableData < 0){
                if(eof){
                    throw new IOException("EOF");
                }
                eof = true;
            }
        }
    }
}
