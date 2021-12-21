package org.techtown.volleyball;

public class NewsItem {
    String thumbUrl;
    String newsUrl;
    String title;

    public NewsItem(String thumbUrl, String newsUrl) {
        this.thumbUrl = thumbUrl;
        this.newsUrl = newsUrl;
    }

    public NewsItem(String thumbUrl, String newsUrl, String title) {
        this.thumbUrl = thumbUrl;
        this.newsUrl = newsUrl;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
