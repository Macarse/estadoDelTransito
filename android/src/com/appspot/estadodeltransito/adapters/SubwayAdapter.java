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
	    ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.subway_row_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.text2 = (TextView) convertView.findViewById(R.id.text2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subway subway = getItem(position);

        viewHolder.imageView.setImageResource(IconsUtil.getSubwayIcon(subway.getName()));
        viewHolder.text1.setText(subway.getStatus());
        viewHolder.text2.setText(subway.getFrequency());

        return convertView;
    }

	static class ViewHolder {
	    public ImageView imageView;
	    public TextView text1;
	    public TextView text2;
	}
}
