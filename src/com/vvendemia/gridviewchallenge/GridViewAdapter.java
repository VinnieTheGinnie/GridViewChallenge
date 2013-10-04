package com.vvendemia.gridviewchallenge;

import java.util.List;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<String> {

	

	static boolean hasHeader = false;
	private Context context;
	private LayoutInflater mInflater;
	private List<String> imagesList;
	
	public GridViewAdapter(Context context, int textViewResourceId, List<String> objects ) {
		super(context, textViewResourceId, objects);
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imagesList = objects;
		//		imageLoader = new ImageLoader();

	}





	@Override
	public int getCount() {
		return (imagesList.size() > 0) ? imagesList.size() : 0;
	}


	private class GridItemHolder {
		ImageView mImage;
		TextView header;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		GridItemHolder mViewHolder;

		if(convertView == null) {
			mViewHolder = new GridItemHolder();
			convertView = View.inflate(context, R.layout.grid_view_item, null);
			mViewHolder.header = (TextView) convertView.findViewById(R.id.header);
			mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.gridItemOne);		
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (GridItemHolder) convertView.getTag();
		}
		
		Picasso.with(context).load(imagesList.get(position)).resize(650, 400).into(mViewHolder.mImage);
		
		if(!hasHeader) {
			mViewHolder.header.setText("Custom Grid View");
			mViewHolder.header.setVisibility(View.VISIBLE);
			hasHeader = true;
		}
		
		return convertView;
	}
}
