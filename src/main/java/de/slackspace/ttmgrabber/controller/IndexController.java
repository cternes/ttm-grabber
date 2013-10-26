package de.slackspace.ttmgrabber.controller;

import de.slackspace.ttmgrabber.entity.VideoItem;
import de.slackspace.ttmgrabber.service.VideoProvider;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class IndexController {
    
    @Inject
    private VideoProvider provider;
     
    public List<VideoItem> getVideoList() {
        return provider.fetchVideos();
    }
    
}
