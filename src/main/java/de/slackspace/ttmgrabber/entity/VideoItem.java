package de.slackspace.ttmgrabber.entity;

public class VideoItem {

    private String video;
    private String text;

    public VideoItem(String text, String video) {
        this.text = text;
        this.video = video;
    }
    
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
