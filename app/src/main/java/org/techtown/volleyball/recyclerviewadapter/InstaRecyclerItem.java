package org.techtown.volleyball.recyclerviewadapter;

public class InstaRecyclerItem {
    String img_url;
    String insta_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getInsta_url() {
        return insta_url;
    }

    public void setInsta_url(String insta_url) {
        this.insta_url = insta_url;
    }

    public InstaRecyclerItem(String img_url, String insta_url) {
        this.img_url = img_url;
        this.insta_url = insta_url;
    }

    @Override
    public String toString() {
        return "RecyclerItem{" +
                "img_url='" + img_url + '\'' +
                ", insta_url='" + insta_url + '\'' +
                '}';
    }
}
