package com.glmiyamoto.myfavoritemovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by glmiyamoto on 4/3/17.
 */

public class Movie implements Parcelable {

    @SerializedName("imdbID")
    private String mId;

    @SerializedName("Title")
    private String mTitle;

    @SerializedName("Year")
    private String mYear;

    @SerializedName("Poster")
    private String mPoster;

    @SerializedName("Genre")
    private String mGenre;

    @SerializedName("Director")
    private String mDirector;

    @SerializedName("Writer")
    private String mWriter;

    @SerializedName("Actors")
    private String mActors;

    @SerializedName("Plot")
    private String mPlot;

    @SerializedName("Language")
    private String mLanguage;

    @SerializedName("Country")
    private String mCountry;

    @SerializedName("Awards")
    private String mAwards;

    public static final Creator CREATOR = new Creator() {
        public Movie createFromParcel(final Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(final int size) {
            return new Movie[size];
        }
    };

    public Movie() {
    }

    // Parcelling part
    public Movie(final Parcel in){
        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mYear = in.readString();
        this.mPoster = in.readString();
        this.mGenre = in.readString();
        this.mDirector = in.readString();
        this.mWriter = in.readString();
        this.mActors = in.readString();
        this.mPlot = in.readString();
        this.mLanguage = in.readString();
        this.mCountry = in.readString();
        this.mAwards = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
        out.writeString(this.mId);
        out.writeString(this.mTitle);
        out.writeString(this.mYear);
        out.writeString(this.mPoster);
        out.writeString(this.mGenre);
        out.writeString(this.mDirector);
        out.writeString(this.mWriter);
        out.writeString(this.mActors);
        out.writeString(this.mPlot);
        out.writeString(this.mLanguage);
        out.writeString(this.mCountry);
        out.writeString(this.mAwards);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(final String year) {
        mYear = year;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(final String poster) {
        mPoster = poster;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(final String genre) {
        mGenre = genre;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(final String director) {
        mDirector = director;
    }

    public String getWriter() {
        return mWriter;
    }

    public void setWriter(final String writer) {
        mWriter = writer;
    }

    public String getActors() {
        return mActors;
    }

    public void setActors(final String actors) {
        mActors = actors;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(final String plot) {
        mPlot = plot;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(final String language) {
        mLanguage = language;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(final String country) {
        mCountry = country;
    }

    public String getAwards() {
        return mAwards;
    }

    public void setAwards(final String awards) {
        mAwards = awards;
    }
}
