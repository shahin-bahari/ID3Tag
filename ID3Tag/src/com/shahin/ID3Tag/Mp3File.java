package com.shahin.ID3Tag;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Mp3File {

    public Mp3File() throws IOException {
        SeekableByteChannel sbc = Files.newByteChannel(Path.of(""), StandardOpenOption.READ);

    }
}
