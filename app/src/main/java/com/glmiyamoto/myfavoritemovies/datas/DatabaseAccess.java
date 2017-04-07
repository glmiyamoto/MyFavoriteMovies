package com.glmiyamoto.myfavoritemovies.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess extends SQLiteOpenHelper {
	public static final String DB_NAME = "myfavoritemovies.db";

	private static final Integer DB_VERSION = 1;
	private final transient Context context;

	public DatabaseAccess(final Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		database.execSQL(DbMovie.CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase dataBase, final int oldVersion,
						  final int newVersion) {
		onCreate(dataBase);

	}

	public SQLiteDatabase getDatabase() {
		boolean dbError = false;
		try {
			return SQLiteManager.getInstanceDataBase(this).open(context, DB_NAME);
		} catch (SQLiteException e) {
			dbError = true;
		}

		if (dbError) {
			//throw new DatabaseErrorException();
		}

		return null;
	}

	public void closeDataBase() {
		SQLiteManager.getInstanceDataBase(this).close();
	}
}
