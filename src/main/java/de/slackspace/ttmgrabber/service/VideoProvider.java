package de.slackspace.ttmgrabber.service;

import de.slackspace.ttmgrabber.cache.VideoCache;
import de.slackspace.ttmgrabber.entity.VideoItem;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Stateless
public class VideoProvider {
    
    private final Logger logger = Logger.getLogger(getClass().toString());
    private static String WEBSITE_URL = "http://www.playboy.de/videos/test_the_max";
    private static String MEDIA_URL = "http://images1.playboy.de/files/html/podcasts/medien/";
    private static String VIDEO_CLIPS = "var video_clips =";
    
    @Inject
    VideoCache cache; 
    
    public List<VideoItem> fetchVideos() {
        logger.log(Level.INFO, "Start video fetching...");
        
        //try first to fetch from cache
        if(cache.getCachedVideos() != null && cache.getCachedVideos().size() > 0) {
            logger.log(Level.INFO, "Serving " + cache.getCachedVideos().size() + " videos from cache...");
            return cache.getCachedVideos();
        }
        
        try {
            logger.log(Level.INFO, "Connecting to server " + WEBSITE_URL);
            Element doc = Jsoup.connect(WEBSITE_URL).get();
            Elements scriptNodes = doc.getElementsByTag("script");
            
            logger.log(Level.INFO, "Found " + scriptNodes.size() + " script nodes.");
        
            for (Element node : scriptNodes) {
                if(node.data().contains(VIDEO_CLIPS)) {
                    List<VideoItem> videoList = getVideoList(node.data());
                    cache.putVideos(videoList);
                    return videoList;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(VideoProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new ArrayList<VideoItem>();
    }
    
    private List<VideoItem> getVideoList(String js) {
        logger.log(Level.INFO, "Trying to extract json data...");
        
        String json = extractJson(js);
        if(json != null) {
            logger.log(Level.INFO, "Extracted json. Trying to parse...");
            return parseJson(json);
        }
        
        logger.log(Level.WARNING, "Failed to extract json data!");
        return new ArrayList<VideoItem>();
    }
    
    private String extractJson(String js) {
        int beginIdx = js.indexOf(VIDEO_CLIPS);
        if(beginIdx != -1) {
            int endIdx = js.indexOf("}];", beginIdx);
            if(endIdx != -1) {
                return js.substring(beginIdx + VIDEO_CLIPS.length(), endIdx + 2);
            }
        }
        return null;
    }
    
    private List<VideoItem> parseJson(String jsonData) {
        List<VideoItem> resultList = new ArrayList<VideoItem>();
        JsonReader reader = Json.createReader(new StringReader(jsonData));
        JsonArray jsonList = reader.readArray();
        
        logger.log(Level.INFO, "Found " + jsonList.size() + " videos in json array");
        
        for (int i = 0; i < jsonList.size(); i++) {
            JsonObject jsonObj = jsonList.getJsonObject(i);
            String text = jsonObj.getString("text");
            String video = jsonObj.getString("video");
            video = prepareVideoLink(video);
            
            resultList.add(new VideoItem(text, video));
        }
        
        return resultList;
    }
    
    private String prepareVideoLink(String originalLink) {
        int idx = originalLink.lastIndexOf('/');
        if(idx != -1) {
            String filename = originalLink.substring(idx + 1);
            return MEDIA_URL + filename;
        }
        
        return originalLink;
    }
}
