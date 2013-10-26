package de.slackspace.ttmgrabber.cache;

import de.slackspace.ttmgrabber.entity.VideoItem;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class VideoCache {

    private ConcurrentHashMap<String, List<VideoItem>> cache;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<String, List<VideoItem>>();
    }

    public List<VideoItem> getCachedVideos() {
        return cache.get("videos");
    }
    
    public void putVideos(List<VideoItem> list) {
        cache.put("videos", list);
    }
    
    public void clear() {
        cache.clear();
    }
}
