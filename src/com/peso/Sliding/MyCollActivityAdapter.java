package com.peso.Sliding;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.peso.R;

import dbmodels.Publication;

public class MyCollActivityAdapter extends BaseAdapter{
private List<Publication> publist=new ArrayList<Publication>();
private LayoutInflater layoutInflater;
	public MyCollActivityAdapter(Context context,List<Publication> publist) {
		this.publist=publist;
		this.layoutInflater=LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return publist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return publist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view= layoutInflater.inflate(R.layout.index_list_item, null);
		TextView title=(TextView) view.findViewById(R.id.list_item_title);
		TextView  Auto=(TextView) view.findViewById(R.id.list_item_authors);
		
		title.setText(publist.get(position).getTitle());
		System.out.println("标题"+publist.get(position).getTitle());
		Auto.setText(publist.get(position).getAuthorStr());
		System.out.println("作者"+publist.get(position).getAuthorStr());
		
		return view;
	}

}
