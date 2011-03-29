package com.appspot.estadodeltransito.util;

import java.util.HashMap;

import android.graphics.Color;

import com.appspot.estadodeltransito.R;

public class IconsUtil {

	private static final HashMap<String, Integer> MAP_PINS;
	private static HashMap<String, Integer> COLORS;
	private static final HashMap<String, Integer> LIST_ICONS;
	
	static {
		MAP_PINS = new HashMap<String, Integer>();
		MAP_PINS.put("A", R.drawable.map_a);
		MAP_PINS.put("B", R.drawable.map_b);
		MAP_PINS.put("C", R.drawable.map_c);
		MAP_PINS.put("D", R.drawable.map_d);
		MAP_PINS.put("E", R.drawable.map_e);
		MAP_PINS.put("H", R.drawable.map_h);
		MAP_PINS.put("P", R.drawable.map_p);
		MAP_PINS.put("U", R.drawable.map_u);
		MAP_PINS.put("Belgrano Norte", R.drawable.map_belgrano_norte);
		MAP_PINS.put("Belgrano Sur", R.drawable.map_belgrano_sur);
		MAP_PINS.put("Mitre", R.drawable.map_mitre);
		MAP_PINS.put("Gral Roca", R.drawable.map_roca);
		MAP_PINS.put("San Martín", R.drawable.map_san_martin);
		MAP_PINS.put("Sarmiento", R.drawable.map_sarmiento);
		MAP_PINS.put("Urquiza", R.drawable.map_urquiza);
		LIST_ICONS = new HashMap<String, Integer>();
		LIST_ICONS.put("Belgrano Sur", R.drawable.belgrano_sur);
		LIST_ICONS.put("Belgrano Norte", R.drawable.belgrano_norte);
		LIST_ICONS.put("Mitre", R.drawable.mitre);
		LIST_ICONS.put("San Martín", R.drawable.san_martin);
		LIST_ICONS.put("Sarmiento", R.drawable.sarmiento);
		LIST_ICONS.put("Gral Roca", R.drawable.roca);
		LIST_ICONS.put("Urquiza", R.drawable.u);
		LIST_ICONS.put("A", R.drawable.a);
		LIST_ICONS.put("B", R.drawable.b);
		LIST_ICONS.put("C", R.drawable.c);
		LIST_ICONS.put("D", R.drawable.d);
		LIST_ICONS.put("E", R.drawable.e);
		LIST_ICONS.put("H", R.drawable.h);
		LIST_ICONS.put("P", R.drawable.p);
		LIST_ICONS.put("U", R.drawable.u);
		COLORS = new HashMap<String, Integer>();
		COLORS.put("A", Color.rgb(40, 160, 255));
		COLORS.put("B", Color.RED);
		COLORS.put("C", Color.BLUE);
		COLORS.put("D", Color.rgb(0, 90, 0));
		COLORS.put("E", Color.rgb(96, 40, 110));
		COLORS.put("H", Color.YELLOW);
		COLORS.put("P", Color.rgb(222, 166, 39));
		COLORS.put("U", Color.rgb(229, 124, 61));
		COLORS.put("Belgrano Norte", Color.rgb(127, 68, 143));
		COLORS.put("Belgrano Sur", Color.rgb(47, 123, 107));
		COLORS.put("Mitre", Color.rgb(44, 119, 184));
		COLORS.put("Gral Roca", Color.rgb(227, 220, 48));
		COLORS.put("San Martín", Color.rgb(231, 93, 86));
		COLORS.put("Sarmiento", Color.rgb(44, 179, 213));
		COLORS.put("Urquiza", Color.rgb(230, 132, 68));
	}
	
	public static final int getSubwayLineIconResource(String lineName) {
		String linechar = lineName.trim().substring(lineName.length()-1);
		Integer integer = MAP_PINS.get(linechar);
		if ( integer == null )
			integer = MAP_PINS.get(MAP_PINS.keySet().iterator().next());
		return integer;
	}
	
	public static final int getSubwayLineColor(String lineName) {
		String linechar = lineName.trim().substring(lineName.length()-1);
		Integer integer = COLORS.get(linechar);
		if ( integer == null )
			integer = Color.BLACK;
		return integer;
	}
	
	public static final int getTrainLineIconResource(String lineName) {
		Integer integer = MAP_PINS.get(lineName);
		if ( integer == null )
			integer = MAP_PINS.get(MAP_PINS.keySet().iterator().next());
		return integer;
	}
	
	public static final int getTrainLineColor(String lineName) {
		Integer integer = COLORS.get(lineName);
		if ( integer == null )
			integer = Color.BLACK;
		return integer;
	}
	
	public static int getTrainIcon(String trainName) {
		Integer ret = LIST_ICONS.get(trainName);

		if (ret == null ) {
			return R.drawable.subway;
		}
		
		return ret;
	}
	
	public static int getSubwayIcon(String subwayName) {
		Integer ret = LIST_ICONS.get(subwayName.trim().substring(subwayName.length()-1));

		if (ret == null ) {
			return R.drawable.subway;
		}
		
		return ret;
	}
}
