package org.techtown.volleyball.slideradapter;

public class SliderItem {
    String thumbUrl;
    String naverUrl;

    public SliderItem(String thumbUrl, String naverUrl) {
        this.thumbUrl = thumbUrl;
        this.naverUrl = naverUrl;
    }

    public String getNaverUrl() {
        return naverUrl;
    }

    public void setNaverUrl(String naverUrl) {
        this.naverUrl = naverUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
