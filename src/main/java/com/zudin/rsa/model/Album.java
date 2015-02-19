package com.zudin.rsa.model;

/**
 * @author Sergey Zudin
 * @since 26.11.14.
 */
public class Album {
    private String name;
    private Artist artist;
    private String url;
    private int playcount;
    private String mbid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Album)) return false;
        Album that = (Album) obj;
        if (this.mbid != "" && that.getMbid() != "")
            return this.mbid.equals(that.mbid);
        else
            return this.name.equals(that.name) && this.artist.getName().equals(that.artist.getName());
    }

    @Override
    public int hashCode() {
        if (mbid != "")
            return mbid.hashCode();
        else
            return name.hashCode() * 17 + artist.getName().hashCode();
    }
}
