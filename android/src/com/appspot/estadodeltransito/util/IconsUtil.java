package com.appspot.estadodeltransito.util;

import java.util.HashMap;

import android.graphics.Color;

import com.appspot.estadodeltransito.R;

public class IconsUtil {

	private static final HashMap<Character, Integer> PINS;
	private static HashMap<Character, Integer> COLORS;
	
	static {
		PINS = new HashMap<Character, Integer>();
		PINS.put('A', R.drawable.map_a);
		PINS.put('B', R.drawable.map_b);
		PINS.put('C', R.drawable.map_c);
		PINS.put('D', R.drawable.map_d);
		PINS.put('E', R.drawable.map_e);
		PINS.put('H', R.drawable.map_h);
		PINS.put('P', R.drawable.map_p);
		PINS.put('U', R.drawable.map_u);
		COLORS = new HashMap<Character, Integer>();
		COLORS.put('A', Color.rgb(40, 160, 255));
		COLORS.put('B', Color.RED);
		COLORS.put('C', Color.BLUE);
		COLORS.put('D', Color.rgb(0, 90, 0));
		COLORS.put('E', Color.rgb(96, 40, 110));
		COLORS.put('H', Color.YELLOW);
		COLORS.put('P', Color.rgb(222, 166, 39));
		COLORS.put('U', Color.rgb(229, 124, 61));
	}
	
	public static final int getLineIconResource(String lineName) {
		char linechar = lineName.trim().charAt(lineName.length()-1);
		return PINS.get(linechar);
	}
	
	public static final int getLineColor(String lineName) {
		char linechar = lineName.trim().charAt(lineName.length()-1);
		return COLORS.get(linechar);
	}
}
