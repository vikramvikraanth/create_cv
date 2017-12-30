package com.muv.createcv.Modelclass;

/**
 * Created by vikram on 25-Dec-17.
 */

public class ProjectModel {

    private String title, url, coverimage;

    public ProjectModel() {
    }
    public ProjectModel(String title, String url, String coverimage) {
        this.title = title;
        this.url = url;
        this.coverimage = coverimage;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getCoverImage() {
        return coverimage;
    }

    public void setCoverImage(String coverimage) {
        this.coverimage = coverimage;
    }
}
