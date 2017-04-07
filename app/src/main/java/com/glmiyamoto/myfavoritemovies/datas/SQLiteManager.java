package com.glmiyamoto.myfavoritemovies.datas;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager {

	private final AtomicInteger mOpenCounter = new AtomicInteger();
	private final static Integer ONE = 1;
	private final static Integer ZERO = 0;

	private static SQLiteOpenHelper mDatabaseHelper;
	private SQLiteDatabase mDatabaseAccess;

	private static SQLiteManager instance;

	private static void initializeDataBase(
			final SQLiteOpenHelper sqLiteOpenHelper) {
		synchronized (SQLiteManager.class) {
			if (instance == null) {
				instance = new SQLiteManager();
				mDatabaseHelper = sqLiteOpenHelper;
			}
		}

	}

	public static SQLiteManager getInstanceDataBase(
			final SQLiteOpenHelper sqLiteOpenHelper) {
		synchronized (SQLiteManager.class) {
			if (instance == null) {
				initializeDataBase(sqLiteOpenHelper);
			}
			return instance;
		}

	}

	public SQLiteDatabase open(final Context context, final String dbName) {
		if (mOpenCounter.incrementAndGet() == ONE) {
			mDatabaseAccess = context.openOrCreateDatabase(dbName,
					Context.MODE_PRIVATE, null);
			mDatabaseAccess = mDatabaseHelper.getWritableDatabase();
		}
		return mDatabaseAccess;
	}

	public void close() {
		if (mDatabaseAccess != null && mOpenCounter.decrementAndGet() == ZERO) {
			mDatabaseAccess.close();
		}
	}
}
