package com.glmiyamoto.myfavoritemovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by glmiyamoto on 4/3/17.
 */

public class OmdbObject {
    @SerializedName("Search")
    private List<OmdbItemObject> mItems;

    @SerializedName("totalResults")
    private int mTotalResults;

    @SerializedName("Response")
    private boolean mResponse;

    @SerializedName("Error")
    private String mError;

    public List<OmdbItemObject> getItems() {
        return mItems;
    }

    public int getTotalResults() {
        return mTotalResults;
    }

    public boolean isResponse() {
        return mResponse;
    }

    public String getError() {
        return mError;
    }
}
