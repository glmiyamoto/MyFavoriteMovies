package com.glmiyamoto.myfavoritemovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by glmiyamoto on 4/3/17.
 */

public class OmdbItemObject extends Movie {

    @SerializedName("Response")
    private boolean mResponse;

    @SerializedName("Error")
    private String mError;

    public boolean isResponse() {
        return mResponse;
    }

    public String getError() {
        return mError;
    }
}
