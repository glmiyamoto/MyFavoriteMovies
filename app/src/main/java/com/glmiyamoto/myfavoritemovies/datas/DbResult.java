package com.glmiyamoto.myfavoritemovies.datas;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class DbResult {
    // Define the list of accepted constants and declare the ImageType annotation
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DB_RESULT_SUCCESS,
            DB_RESULT_ERROR
    })
    public @interface DbResultType {
    }

    // Declare the constants
    public static final int DB_RESULT_SUCCESS = 0;
    public static final int DB_RESULT_ERROR = 1;

    // Decorate the target methods with the annotation
    @DbResultType
    public abstract int getDbResultType();

    // Attach the annotation
    public abstract void setDbResultType(@DbResultType int type);
}