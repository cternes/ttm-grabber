    package de.slackspace.ttmgrabber.service;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import de.slackspace.ttmgrabber.entity.VideoItem;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class RssProvider {

    private final String feedTitle = "TTM Grabber";
    private final String feedDescription = "Test The Max Videos";
    private final String feedUrl = "http://ttmgrabber-slackspace.rhcloud.com/";

    @Inject
    private VideoProvider provider;

    public SyndFeed createFeed() {
        SyndFeed feed = createRssHeader();
        
        for (VideoItem item : provider.fetchVideos()) {
            SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(item.getText());
            
            SyndContent desc = new SyndContentImpl();
            desc.setType("application/rss+xml");
            desc.setValue(item.getText());
            entry.setDescription(desc);
            
            entry.setLink(item.getVideo());
            
            feed.getEntries().add(entry);
        }

        return feed;
    }

    private SyndFeed createRssHeader() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_1.0");
        feed.setTitle(feedTitle);
        feed.setDescription(feedDescription);
        feed.setLink(feedUrl);
        feed.setEncoding("UTF-8");

        return feed;
    }

}
