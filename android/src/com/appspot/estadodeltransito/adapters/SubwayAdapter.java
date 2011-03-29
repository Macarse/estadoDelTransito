package com.appspot.estadodeltransito.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.estadodeltransito.R;
import com.appspot.estadodeltransito.domain.subway.Subway;
import com.appspot.estadodeltransito.util.IconsUtil;

public class SubwayAdapter extends ArrayAdapter<Subway> {

	private LayoutInflater mInflater;

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
        
        imageView.setImageResource(IconsUtil.getSubwayIcon(subway.getName()));
        text1.setText(subway.getStatus());
        text2.setText(subway.getFrequency());

        return view;
    }

}
