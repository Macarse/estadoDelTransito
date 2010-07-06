package com.appspot.estadodeltransito.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.Subway;

public class SubwayAdapter extends ArrayAdapter<Subway> {

	private LayoutInflater mInflater;
	private static final HashMap<Character, Integer> ICONS;

	static {
		ICONS = new HashMap<Character, Integer>();
		ICONS.put('A', R.drawable.a);
		ICONS.put('B', R.drawable.b);
		ICONS.put('C', R.drawable.c);
		ICONS.put('D', R.drawable.d);
		ICONS.put('E', R.drawable.e);
		ICONS.put('H', R.drawable.h);
		ICONS.put('P', R.drawable.p);
		ICONS.put('U', R.drawable.u);
	}
	
	public SubwayAdapter(Context context, List<Subway> subways) {
		super(context, 1, subways);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        
        if (convertView == null) {
            view = mInflater.inflate(R.layout.subway_row_layout, parent, false);
        } else {
            view = convertView;
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.ImageView01);
        TextView text1 = (TextView) view.findViewById(R.id.text1);
        TextView text2 = (TextView) view.findViewById(R.id.text2);
        
        Subway subway = getItem(position);
        
        imageView.setImageResource(getIcon(subway));
        text1.setText(subway.getStatus());
        text2.setText(subway.getFrequency());

        return view;
    }

	public static int getIcon(Subway subway) {
		Integer ret = ICONS.get(subway.getLetter());

		if (ret == null ) {
			return -1;
		}
		
		return ret;
	}
}
