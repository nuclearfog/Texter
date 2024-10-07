package org.nuclearfog.texter.store.database.tables;

public class PostTable {

	public static final String NAME = "posts";

	public static final String POST_ID = "post_id";
	public static final String POST_TIME = "timestamp";
	public static final String POST_TITLE = "post_title";
	public static final String POST_TEXT = "post_text";

	public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS \"" + NAME + "\"(" +
			POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			POST_TIME + " INTEGER NOT NULL," +
			POST_TITLE + " TEXT NOT NULL," +
			POST_TEXT + " TEXT NOT NULL);";

	private PostTable() {
	}
}