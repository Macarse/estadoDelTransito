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
import com.appspot.estadodeltransito.Train;

public class TrainAdapter extends ArrayAdapter<Train> {

	private LayoutInflater mInflater;
	private static final HashMap<String, Integer> ICONS;

	static {
		ICONS = new HashMap<String, Integer>();
		ICONS.put("Belgrano Sur", R.drawable.belgrano_sur);
		ICONS.put("Belgrano Norte", R.drawable.belgrano_norte);
		ICONS.put("Mitre", R.drawable.mitre);
		ICONS.put("San Martín", R.drawable.san_martin);
		ICONS.put("Sarmiento", R.drawable.sarmiento);
		ICONS.put("Gral Roca", R.drawable.roca);

	}
	
	public TrainAdapter(Context context, List<Train> subways) {
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
        
        Train train = getItem(position);
        
        imageView.setImageResource(getIcon(train));
        text1.setText(train.getName());
        text2.setText(train.getStatus());

        return view;
    }

	public static int getIcon(Train train) {
		Integer ret = ICONS.get(train.getLine());

		if (ret == null ) {
			return R.drawable.subway;
		}
		
		return ret;
	}
}
