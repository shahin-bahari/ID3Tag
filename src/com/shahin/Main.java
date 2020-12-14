package com.shahin;

import com.shahin.ID3Tag.ID3V2PictureFrame;
import com.shahin.ID3Tag.Mp3File;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) {

        try {
            Mp3File mp3 = new Mp3File("F:/test.mp3");
            System.out.println("Artist : " + mp3.getTag(Mp3File.TagType.ARTIST));
            System.out.println("Album : " + mp3.getTag(Mp3File.TagType.ALBUM));
            System.out.println("title : " + mp3.getTag(Mp3File.TagType.TITLE));
            System.out.println("description : " + mp3.getTag(Mp3File.TagType.DESCRIPTION));
            System.out.println("length : " + mp3.getLengthMs());
            System.out.println("genre : " + mp3.getTag(Mp3File.TagType.GENRE));
            System.out.println("bitrate : " + mp3.getTag(Mp3File.TagType.BITRATE));
            System.out.println("track : " + mp3.getTag(Mp3File.TagType.TRACK));
            System.out.println("year : " + mp3.getTag(Mp3File.TagType.YEAR));
            System.out.println("is vbr : " + mp3.getTag(Mp3File.TagType.IS_VBR));

            for(ID3V2PictureFrame.ImageType t :mp3.getEmbeddedImageTypes()){
                System.out.println(t);
                ID3V2PictureFrame pic = mp3.getEmbeddedImage(t);
                System.out.println("\t" + pic.getMimeType());
                saveThumbnail(pic.getImage(),"test.jpg");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveThumbnail(byte[] data,String name){
        try {
            FileChannel fc = FileChannel.open(Path.of(name),
                    StandardOpenOption.CREATE_NEW , StandardOpenOption.WRITE);
            fc.write(ByteBuffer.wrap(data));
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
