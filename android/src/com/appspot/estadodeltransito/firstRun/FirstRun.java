package com.appspot.estadodeltransito.firstRun;

import java.io.IOException;

import android.content.Context;


public class FirstRun {

	public static final void firstRun(Context context) {
		createDB(context);
	}

	private static final void createDB(Context context) {
		DataBaseHelper dbh = new DataBaseHelper(context);
		try {
			dbh.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
