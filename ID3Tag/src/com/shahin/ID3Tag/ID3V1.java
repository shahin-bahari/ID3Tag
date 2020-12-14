package com.shahin.ID3Tag;

import java.nio.charset.StandardCharsets;

public class ID3V1 {

    private final int TITLE_OFFSET = 3;
    private final int TITLE_LENGTH = 30;
    private final int ARTIST_OFFSET = TITLE_OFFSET + TITLE_LENGTH;
    private final int ARTIST_LENGTH = 30;
    private final int ALBUM_OFFSET = ARTIST_OFFSET + ARTIST_LENGTH;
    private final int ALBUM_LENGTH = 30;
    private final int YEAR_OFFSET = ALBUM_OFFSET + ALBUM_LENGTH;
    private final int YEAR_LENGTH = 4;
    private final int COMMENT_OFFSET = YEAR_OFFSET + YEAR_LENGTH;
    private final int COMMENT_LENGTH = 30;
    private final int GENRE_OFFSET = COMMENT_OFFSET + COMMENT_LENGTH;
    private final int ZERO_BYTE = COMMENT_OFFSET + COMMENT_LENGTH - 2;
    private final int TRACK_OFFSET = ZERO_BYTE + 1;


    private String title;
    private String album;
    private String artist;
    private String comment;
    private String year;
    private int genre;
    private int track;

    public static ID3V1 getTag(byte[] data){
        if(data == null || data.length < 128){
            return null;
        }
        if(data[0] != 'T' && data[1] != 'A' && data[2] != 'G'){
            return null;
        }
        ID3V1 tag = new ID3V1();
        tag.title = new String(data,tag.TITLE_OFFSET, tag.TITLE_LENGTH, StandardCharsets.UTF_8);
        tag.artist = new String(data,tag.ARTIST_OFFSET, tag.ARTIST_LENGTH, StandardCharsets.UTF_8);
        tag.album = new String(data,tag.ALBUM_OFFSET, tag.ALBUM_LENGTH, StandardCharsets.UTF_8);
        tag.year = new String(data,tag.YEAR_OFFSET, tag.YEAR_LENGTH,StandardCharsets.UTF_8);
        tag.genre = data[tag.GENRE_OFFSET];
        if(data[tag.ZERO_BYTE] == 0){
            tag.track = data[tag.TRACK_OFFSET];
            tag.comment = new String(data,tag.COMMENT_OFFSET, tag.COMMENT_LENGTH-2,StandardCharsets.UTF_8);
        }else{
            tag.comment = new String(data,tag.COMMENT_OFFSET, tag.COMMENT_LENGTH,StandardCharsets.UTF_8);
        }
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getComment() {
        return comment;
    }

    public String getYear() {
        return year;
    }

    public int getGenre() {
        return genre;
    }

    public int getTrack() {
        return track;
    }

    public static final String[] GENRES = {
            "Blues",
            "Classic Rock",
            "Country",
            "Dance",
            "Disco",
            "Funk",
            "Grunge",
            "Hip-Hop",
            "Jazz",
            "Metal",
            "New Age",
            "Oldies",
            "Other",
            "Pop",
            "R&B",
            "Rap",
            "Reggae",
            "Rock",
            "Techno",
            "Industrial",
            "Alternative",
            "Ska",
            "Death Metal",
            "Pranks",
            "Soundtrack",
            "Euro-Techno",
            "Ambient",
            "Trip-Hop",
            "Vocal",
            "Jazz+Funk",
            "Fusion",
            "Trance",
            "Classical",
            "Instrumental",
            "Acid",
            "House",
            "Game",
            "Sound Clip",
            "Gospel",
            "Noise",
            "Alt Rock",
            "Bass",
            "Soul",
            "Punk",
            "Space",
            "Meditative",
            "Instrumental Pop",
            "Instrumental Rock",
            "Ethnic",
            "Gothic",
            "Darkwave",
            "Techno-Industrial",
            "Electronic",
            "Pop-Folk",
            "Eurodance",
            "Dream",
            "Southern Rock",
            "Comedy",
            "Cult",
            "Gangsta",
            "Top 40",
            "Christian Rap",
            "Pop/Funk",
            "Jungle",
            "Native American",
            "Cabaret",
            "New Wave",
            "Psychedelic",
            "Rave",
            "Showtunes",
            "Trailer",
            "Lo-Fi",
            "Tribal",
            "Acid Punk",
            "Acid Jazz",
            "Polka",
            "Retro",
            "Musical",
            "Rock & Roll",
            "Hard Rock",
            "Folk",
            "Folk/Rock",
            "National Folk",
            "Swing",
            "Fast Fusion",
            "Bebob",
            "Latin",
            "Revival",
            "Celtic",
            "Bluegrass",
            "Avantgarde",
            "Gothic Rock",
            "Progressive Rock",
            "Psychedelic Rock",
            "Symphonic Rock",
            "Slow Rock",
            "Big Band",
            "Chorus",
            "Easy Listening",
            "Acoustic",
            "Humour",
            "Speech",
            "Chanson",
            "Opera",
            "Chamber Music",
            "Sonata",
            "Symphony",
            "Booty Bass",
            "Primus",
            "Porn Groove",
            "Satire/Parody",
            "Slow Jam",
            "Club",
            "Tango",
            "Samba",
            "Folklore",
            "Ballad",
            "Power Ballad",
            "Rhythmic Soul",
            "Freestyle",
            "Duet",
            "Punk Rock",
            "Drum Solo",
            "Acapella",
            "Euro-House",
            "Dance Hall",
            "Goa",
            "Drum & Bass",
            "Club-House",
            "Hardcore",
            "Terror",
            "Indie",
            "BritPop",
            "Negerpunk",
            "Polsk Punk",
            "Beat",
            "Christian Gangsta",
            "Heavy Metal",
            "Black Metal",
            "Crossover",
            "Contemporary Chr",
            "Christian Rock",
            "Merengue",
            "Salsa",
            "Thrash Metal",
            "Anime",
            "JPop",
            "Synthpop"
    };
}
