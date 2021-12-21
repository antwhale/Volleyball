package org.techtown.volleyball.news3;

import android.graphics.drawable.Drawable;

public class RSSNewsItem {

    private String title;
    private String link;
    private String description;
    private String imgUrl;
    private Drawable mIcon;

    /**
     * Initialize with icon and data array
     */
    public RSSNewsItem() {
    }

    /**
     * Initialize with icon and strings
     */
    public RSSNewsItem(String title, String link, String description, String imgUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.imgUrl = imgUrl;
    }

    /**
     * Set icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    /**
     * Get icon
     *
     * @return
     */
    public Drawable getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int compareTo(RSSNewsItem other) {
        if (title.equals(other.getTitle())) {
            return -1;
        } else if (link.equals(other.getLink())) {
            return -1;
        } else if (description.equals(other.getDescription())) {
            return -1;
        }

        return 0;
    }

}

