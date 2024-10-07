package org.nuclearfog.texter.store.database.tables;

public class ImageTable {

	public static final String NAME = "images";

	public static final String POST_ID = "post_id";
	public static final String IMAGE_ID = "image_id";
	public static final String IMAGE_PATH = "path";
	public static final String IMAGE_POS_X = "pos_x";
	public static final String IMAGE_POS_Y = "pos_y";
	public static final String IMAGE_WIDTH = "width";
	public static final String IMAGE_HEIGHT = "height";
	public static final String IMAGE_RANK = "rank";

	public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS \"" + NAME + "\"(" +
			IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			POST_ID + " INTEGER NOT NULL," +
			IMAGE_PATH + " TEXT NOT NULL," +
			IMAGE_POS_X + " INTEGER NOT NULL," +
			IMAGE_POS_Y + " INTEGER NOT NULL," +
			IMAGE_WIDTH + " INTEGER NOT NULL," +
			IMAGE_HEIGHT + " INTEGER NOT NULL," +
			IMAGE_RANK + " INTEGER NOT NULL," +
			"FOREIGN KEY(" + POST_ID + ") REFERENCES " +
			PostTable.NAME + "(" + PostTable.POST_ID + "));";

	private ImageTable() {
	}
}