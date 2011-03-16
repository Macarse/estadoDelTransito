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
import com.appspot.estadodeltransito.domain.highway.Highway;

public class HighwayAdapter extends ArrayAdapter<Highway> {

	private LayoutInflater mInflater;
	private static final HashMap<Character, Integer> ICONS;

	static {
		ICONS = new HashMap<Character, Integer>();
		ICONS.put('u', R.drawable.highway);
		ICONS.put('v', R.drawable.avenue);
		ICONS.put('t', R.drawable.bridge);
	}

	public HighwayAdapter(Context context, List<Highway> highways) {
		super(context, 1, highways);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
        
        if (convertView == null) {
            view = mInflater.inflate(R.layout.highway_row_layout, parent, false);
        } else {
            view = convertView;
        }

        ImageView icon = (ImageView) view.findViewById(R.id.avenue_highways_icon);
        TextView title = (TextView) view.findViewById(R.id.avenue_highways_title);

        TextView from = (TextView) view.findViewById(R.id.avenue_highways_from);
        TextView fromText = (TextView) view.findViewById(R.id.avenue_highways_from_text);
        ImageView fromStatus = (ImageView) view.findViewById(R.id.avenue_highways_from_status);

        TextView to = (TextView) view.findViewById(R.id.avenue_highways_to);
        TextView toText = (TextView) view.findViewById(R.id.avenue_highways_to_text);
        ImageView toStatus = (ImageView) view.findViewById(R.id.avenue_highways_to_status);
        
        Highway highway = getItem(position);

        icon.setImageResource(getIcon(highway.getName()));
        title.setText(highway.getName());

        from.setText(highway.getDirectionFrom());
        fromText.setText(highway.getDelayFrom());
        if ( highway.getStatusFrom() != null ) {
        	fromStatus.setImageResource(getStatusIcon(highway.getStatusFrom()));
        } else {
        	from.setVisibility(View.GONE);
        	fromText.setVisibility(View.GONE);
        	fromStatus.setVisibility(View.GONE);
        	
        }

        to.setText(highway.getDirectionTo());
        toText.setText(highway.getDelayTo());
        if ( highway.getStatusTo() != null ) {
        	toStatus.setImageResource(getStatusIcon(highway.getStatusTo()));
    	} else {
        	to.setVisibility(View.GONE);
        	toText.setVisibility(View.GONE);
        	toStatus.setVisibility(View.GONE);
    	}

        return view;
	}

	public static final int getStatusIcon(String statusFrom) {
		if ( statusFrom.contains("Normal") ) {
			return R.drawable.green_status;
		} else if ( statusFrom.contains("Interrumpido") ) {
			return R.drawable.red_status;
		} else {
			return R.drawable.yellow_status;
		}
	}

	public static final int getIcon(String name) {
		Integer ret = ICONS.get(name.charAt(1));

		if ( ret == null ) {
			return R.drawable.highway;
		}

		return ret;
	}

	public static final boolean shouldShowDetails(Highway highway) {
		if (highway.getStatusMessageFrom() == null
				&& highway.getStatusMessageTo() == null)
			return false;

		return true;
	}
}
