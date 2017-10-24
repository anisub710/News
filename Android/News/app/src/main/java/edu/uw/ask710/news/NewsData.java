package edu.uw.ask710.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anirudhsubramanyam on 10/23/17.
 */


public class NewsData implements Parcelable {
    public String headline = "";
    public String description = "";
    public String imageUrl = "";
    public long publishedTime = 0;

    public NewsData() {

    }
    public NewsData(String headline, String imageUrl, String description, long publishedTime){
        this.headline = headline;
        this.imageUrl = imageUrl;
        this.description = description;
        this.publishedTime = publishedTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected NewsData(Parcel in){
        headline = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        publishedTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(headline);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeLong(publishedTime);

    }

    public static final Parcelable.Creator<NewsData> CREATOR = new Parcelable.Creator<NewsData>(){
        @Override
        public NewsData createFromParcel(Parcel parcel) {
            return new NewsData(parcel);
        }

        @Override
        public NewsData[] newArray(int i) {
            return new NewsData[i];
        }
    };

//        public String toString(){
//            return this.headline;
//        }

}

