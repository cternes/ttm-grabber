package de.slackspace.ttmgrabber.cache;

import de.slackspace.ttmgrabber.service.VideoProvider;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class RefreshTimer {

    private final Logger logger = Logger.getLogger(getClass().toString());
    
    @Inject
    private VideoCache cache;
    
    @Inject
    private VideoProvider provider;

    @Schedule(minute = "0", hour = "2", second = "0", persistent = false)
    public void clearCache() {
        logger.info("Clearing cache...");
        
        //clear cache
        cache.clear();
        
        //fill cache
        provider.fetchVideos();
    } 
}
