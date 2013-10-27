package de.slackspace.ttmgrabber.servlet;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import de.slackspace.ttmgrabber.service.RssProvider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RssServlet", urlPatterns = "/rss")
public class RssServlet extends HttpServlet {

    @Inject
    private RssProvider rssProvider;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SyndFeed feed = rssProvider.createFeed();
        SyndFeedOutput output = new SyndFeedOutput();
        
        try {
            output.output(feed, resp.getWriter());
        } catch (FeedException ex) {
            Logger.getLogger(RssServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
