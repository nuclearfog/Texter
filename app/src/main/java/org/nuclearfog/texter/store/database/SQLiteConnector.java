package org.nuclearfog.texter.store.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class SQLiteConnector {

	private File databasePath;
	private SQLiteDatabase db;

	private static SQLiteConnector instance;


	private SQLiteConnector(Context context, String[] queries) {
		databasePath = context.getDatabasePath("main.db");
		db = context.openOrCreateDatabase(databasePath.toString(), Context.MODE_PRIVATE, null);
		for (String query : queries) {
			db.execSQL(query);
		}
	}


	public static SQLiteConnector getInstance(Context context, String... queries) {
		if (instance == null) {
			instance = new SQLiteConnector(context, queries);
		}
		return instance;
	}


	public SQLiteDatabase getReadableDatabase() {
		if (!db.isOpen())
			db = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
		return db;
	}


	public SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase db = getReadableDatabase();
		db.beginTransaction();
		return db;
	}


	public void commit() {
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}