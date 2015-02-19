package com.zudin.rsa;

import com.zudin.rsa.model.Album;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.json.JsonException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * @author Sergey Zudin
 * @since 25.11.14.
 */
public class RSAServlet extends HttpServlet {

    private static final String API_KEY = "6c41aa840502955e1ee8d9c44c44d7c5";
    private static final int LIMIT = 1000;
    private static final String LASTFM_REQUEST = "http://ws.audioscrobbler.com/2.0/?method=user.gettopalbums" +
            "&user=%s&api_key=" + API_KEY + "&format=json&limit=" + LIMIT + "&page=%d";
    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String user;
        if (!(user = req.getParameter("user")).equals("")) {
            int page = 1;
            PrintWriter writer = resp.getWriter();
            List<Album> topAlbums = getTopAlbums();
            Collection<Album> listened = new ArrayList<Album>();
            while (true) {
                List<Album> userAlbums = getAlbums(user, page++);
                listened.addAll(CollectionUtils.intersection(userAlbums, topAlbums));
                writer.print(" ");
                if (userAlbums.size() != 1000) break;
            }
            Collection<Album> notListened = CollectionUtils.disjunction(topAlbums, listened);
            writer.println("You have listened to " + listened.size() + " albums from Top 500. " + (500 - notListened.size()) + " are still remaining!");
            writer.println("Listened: ");
            for (Album album : listened) {
                writer.println("Album " + album.getName() + " by " + album.getArtist().getName() + "; url : " + album.getUrl());
            }
            writer.println("Remaining: ");
            for (Album album : listened) {
                writer.println("Album " + album.getName() + " by " + album.getArtist().getName());
            }
        } else {
            String site = "/";
            resp.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            resp.setHeader("Location", site);
        }
    }

    private List<Album> getAlbums(String user, int page) throws IllegalFormatException, IOException {
        try {
            String url = String.format(LASTFM_REQUEST, user, page);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode node = mapper.readValue(new URL(url), JsonNode.class);
            return mapper.readValue(node.get("topalbums").get("album"), new TypeReference<List<Album>>(){});
        } catch (IOException e) {
            throw new IOException("Url has changed");
        } catch (Exception e) {
            throw new JsonException("Incorrect json from last.fm");
        }
    }

    private List<Album> getTopAlbums() {
        try {
            return mapper.readValue(new File("src/main/webapp/res/albums.json"), new TypeReference<List<Album>>(){});
        } catch (Exception e) {
            throw new JsonException("Incorrect json from last.fm");
        }
    }

    public static void main(String[] args) throws IOException {
        RSAServlet servlet = new RSAServlet();
        List<Album> userAlbums = servlet.getAlbums("mr_vaultboy", 1);
        List<Album> topAlbums = servlet.getTopAlbums();
        Collection<Album> listened = CollectionUtils.intersection(userAlbums, topAlbums);
        Collection<Album> notListened = CollectionUtils.disjunction(topAlbums, listened);
        System.out.println(notListened.size());
        System.out.println(listened.size());
    }
}
