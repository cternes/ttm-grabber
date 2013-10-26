package de.slackspace.ttmgrabber.cache;

import de.slackspace.ttmgrabber.entity.VideoItem;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class VideoCache {

    private final Logger logger = Logger.getLogger(getClass().toString());
    private ConcurrentHashMap<String, List<VideoItem>> cache;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<String, List<VideoItem>>();
    }

    public List<VideoItem> getCachedVideos() {
        return cache.get("videos");
    }
    
    public void putVideos(List<VideoItem> list) {
        logger.log(Level.INFO, "Adding videos to cache. Number of videos: " + list.size());
        cache.put("videos", list);
    }
    
    public void clear() {
        logger.log(Level.INFO, "Clearing cache.");
        cache.clear();
    }
}
