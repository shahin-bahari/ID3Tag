package com.shahin.ID3Tag;

import java.io.IOException;

public abstract class ID3V2Frame {

    protected final String id;
    protected final int flag;
    protected final int size;
    protected final int version;

    public ID3V2Frame(int version,String id, int flag, int size) {
        this.version = version;
        this.id = id;
        this.flag = flag;
        this.size = size;
    }

    protected abstract void readData(FileStream fs) throws IOException;
}
