package com.vvendemia.gridviewchallenge;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends ArrayAdapter<String> {

	
	private final int ANSWER_CORRECT_FLAG = 1;
	private final int ANSWER_INCORRECT_FLAG = -1;
	
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
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		GridItemHolder mViewHolder;

		if(convertView == null) {
			mViewHolder = new GridItemHolder();
			convertView = View.inflate(context, R.layout.grid_view_item, null);
			mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.gridItemOne);		
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (GridItemHolder) convertView.getTag();
		}
		
		Drawable d = context.getResources().getDrawable(R.drawable.ic_launcher);
		mViewHolder.mImage.setImageDrawable(d);
		
		return convertView;
	}
}
