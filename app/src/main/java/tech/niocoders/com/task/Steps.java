package tech.niocoders.com.task;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Steps {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("shortDescription")
    @Expose
    public String shortDescription;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("videoURL")
    @Expose
    public String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    public String thumbnailURL;


    //setters


    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    //getters

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getVideoURL() {
        return videoURL;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
