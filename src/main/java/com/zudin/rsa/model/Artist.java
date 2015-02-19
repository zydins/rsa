package com.zudin.rsa.model;

/**
 * @author Sergey Zudin
 * @since 19.02.15.
 */
public class Artist {
    private String mbid;
    private String url;
    private String name;

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
     public boolean equals(Object obj) {
        if (!(obj instanceof Artist)) return false;
        Artist that = (Artist) obj;
        return this.mbid.equals(that.mbid);
    }
}
