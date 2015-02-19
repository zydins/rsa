package com.zudin.rsa.utils;

import com.zudin.rsa.model.Album;
import com.zudin.rsa.model.Artist;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Zudin
 * @since 20.02.15.
 */
public class AlbumGrabber {
    public static void getTopAlbums() {
        try {
            String base_reqest = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=6c41aa840502955e1ee8d9c44c44d7c5&artist=%s&album=%s&format=json";
            BufferedReader reader = new BufferedReader(new FileReader(new File("src/main/webapp/res/albums.txt")));
            List<Album> albums = new ArrayList<Album>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String str;
            while ((str = reader.readLine()) != null) {
                String[] data = str.split(";");
                String artist = data[2].replaceAll(" ", "+").replaceAll("&", "%26");
                String name = data[0].replaceAll(" ", "+").replaceAll("&", "%26");
                String request = String.format(base_reqest, artist, name);
                System.out.println(request);
                JsonNode node = null;
                try {
                    node = mapper.readValue(new URL(request), JsonNode.class);
                } catch (IOException e) {
                    node = mapper.readValue(new URL(request), JsonNode.class);
                }
                Album album = new Album();
                JsonNode root = node.get("album");
                album.setName(root.get("name").asText());
                album.setMbid(root.get("mbid").asText());
                album.setUrl(root.get("url").asText());
                album.setArtist(mapper.readValue(root.get("tracks").get("track").get(0).get("artist"), Artist.class));
                albums.add(album);
                System.out.println("Just read " + artist + " : " + name);
            }
            mapper.writerWithType(new TypeReference<List
                    <Album>>() {
            }).writeValue(new File("src/main/webapp/res/albums.json"), albums);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
