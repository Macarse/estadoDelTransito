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
		ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.highway_row_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.icon = (ImageView) convertView.findViewById(R.id.avenue_highways_icon);
            viewHolder.title = (TextView) convertView.findViewById(R.id.avenue_highways_title);

            viewHolder.from = (TextView) convertView.findViewById(R.id.avenue_highways_from);
            viewHolder.fromText = (TextView) convertView.findViewById(R.id.avenue_highways_from_text);
            viewHolder.fromStatus = (ImageView) convertView.findViewById(R.id.avenue_highways_from_status);

            viewHolder.to = (TextView) convertView.findViewById(R.id.avenue_highways_to);
            viewHolder.toText = (TextView) convertView.findViewById(R.id.avenue_highways_to_text);
            viewHolder.toStatus = (ImageView) convertView.findViewById(R.id.avenue_highways_to_status);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        
        Highway highway = getItem(position);

        viewHolder.icon.setImageResource(getIcon(highway.getName()));
        viewHolder.title.setText(highway.getName());

        viewHolder.from.setText(highway.getDirectionFrom());
        viewHolder.fromText.setText(highway.getDelayFrom());
        if ( highway.getStatusFrom() != null ) {
            viewHolder.fromStatus.setImageResource(getStatusIcon(highway.getStatusFrom()));
        } else {
            viewHolder.from.setVisibility(View.GONE);
            viewHolder.fromText.setVisibility(View.GONE);
            viewHolder.fromStatus.setVisibility(View.GONE);
        }

        viewHolder.to.setText(highway.getDirectionTo());
        viewHolder.toText.setText(highway.getDelayTo());
        if ( highway.getStatusTo() != null ) {
            viewHolder.toStatus.setImageResource(getStatusIcon(highway.getStatusTo()));
        } else {
            viewHolder.to.setVisibility(View.GONE);
            viewHolder.toText.setVisibility(View.GONE);
            viewHolder.toStatus.setVisibility(View.GONE);
        }

        return convertView;
	}

	private class ViewHolder {
	    public ImageView icon;
	    public TextView title;
	    public TextView from;
	    public TextView fromText;
	    public ImageView fromStatus;
	    public TextView to;
	    public TextView toText;
	    public ImageView toStatus;
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
		if (highway == null ||
		    (highway.getStatusMessageFrom() == null &&
		            highway.getStatusMessageTo() == null) )
			return false;

		return true;
	}
}
