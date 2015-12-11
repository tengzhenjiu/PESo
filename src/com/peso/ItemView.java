package com.peso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dbmodels.Publication;

public class ItemView extends BaseAdapter {
	// 映射数据
	private List<Publication> mDataList;
	
	private Activity activity;
	private ImageView itemImage;
	private TextView itemAuthors;
	private TextView itemTime;//itemTitle
	private TextView itemTitle;

	// private long mSumTime;

	public ItemView(Activity activity, List<Publication> list) {
		this.activity = activity;
		mDataList = list;
	}

	// 获取数据量
	@Override
	public int getCount() {
		return mDataList.size();
	}

	// 获取对应ID项对应的Item
	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	// 获取对应项ID
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 逗比式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// 获取纳秒时间 更加精确
		// long start = System.nanoTime();list_item_time
		// 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
		int size=(int)(Resources.getSystem().getDisplayMetrics().widthPixels/30);//16dip
		int sizel=size*30/34;//14dip
		int sizel2=size*30/40;//12dip
		
		
		View view = activity.getLayoutInflater().inflate(
				R.layout.index_list_item, null);
		// 实例化控件
		itemImage = (ImageView) view
				.findViewById(R.id.list_item_download);
		itemTitle = (TextView) view
				.findViewById(R.id.list_item_title);
		/*
		 * 2015-09-17-15:43修改 TextView itemAuthor=(TextView)
		 * view.findViewById(R.id.textv_list_item_author);
		 */
		itemAuthors = (TextView) view
				.findViewById(R.id.list_item_authors);
		// RatingBar itemRatingBar=(RatingBar)
		// view.findViewById(R.id.id_download_bar);
		itemTime = (TextView) view
				.findViewById(R.id.list_item_time);
		// 取出bean对象
		final Publication pub = mDataList.get(position);
		// 设置控件的数据

		// itemRatingBar.setRating(bean.itemRatingBarId);
		// itemImage.setImageResource(R.drawable.download);
		itemTitle.setText(pub.getTitle());
		/*
		 * 2015-09-17-15:43修改 itemAuthor.setText(bean.itemAuthor);
		 */
		itemAuthors.setText(pub.getAuthorStr().replace(";",";\t"));

		// .*? \d{4}$
		if (pub.getDate() != null && pub.getDate().length() != 0) {
			Pattern p = Pattern.compile("(.*?) (\\d{4}$)");
			Matcher m = p.matcher(pub.getDate());
			while (m.find()) {
				itemTime.setText(m.group(1) + "\n" + m.group(2));
			}
		} else if (pub.getYear() != null
				&& pub.getYear().toString().length() != 0) {
			itemTime.setText(pub.getYear().toString());
		}

		if (pub.getUrl() != null && pub.getUrl().get(0).length() != 0) {
			itemImage.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri url = Uri.parse(pub.getUrl().get(0));
					intent.setData(url);
					activity.startActivity(intent);
				}
			});
		} else
			itemImage.setImageResource(R.drawable.package_download_none);
		//itemTitle.setTextSize(size);
		//itemAuthors.setTextSize(sizel);
		//itemTime.setTextSize(sizel2);//itemTitle
		
		/*设置大小*/
		// long end = System.nanoTime();
		// long dValue = end - start;
		// mSumTime += dValue;
		// // 输出每次getView消耗的时间和
		// Log.d("xys", String.valueOf(mSumTime));
		return view;
		// 逗比式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>24409529

		// 普通式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// 获取纳秒时间 更加精确
		// long start = System.nanoTime();
		// if (convertView == null) {
		// 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
		// convertView = mLayoutInflater.inflate(R.layout.item, null);
		// }
		// ImageView itemImage = (ImageView)
		// convertView.findViewById(R.id.iv_image);
		// TextView itemTitle = (TextView)
		// convertView.findViewById(R.id.tv_title);
		// TextView itemContent = (TextView)
		// convertView.findViewById(R.id.tv_content);
		// // 取出bean对象
		// ItemBean bean = mDataList.get(position);
		// 设置控件的数据
		// itemImage.setImageResource(bean.itemImageResid);
		// itemTitle.setText(bean.itemTitle);
		// itemContent.setText(bean.itemContent);
		// long end = System.nanoTime();
		// long dValue = end - start;
		// mSumTime += dValue;
		// 输出每次getView消耗的时间和
		// Log.d("xys", String.valueOf(mSumTime));
		// return convertView;
		// 普通式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>19271161

		// 文艺式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		// 获取纳秒时间 更加精确
		// long start = System.nanoTime();
		// ViewHolder holder = null;
		// if (convertView == null) {
		// holder = new ViewHolder();
		// 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
		// convertView = mLayoutInflater.inflate(R.layout.item, null);
		// holder.img = (ImageView)
		// convertView.findViewById(R.id.iv_image);
		// holder.title = (TextView)
		// convertView.findViewById(R.id.tv_title);
		// holder.content = (TextView)
		// convertView.findViewById(R.id.tv_content);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		// 取出bean对象
		// ItemBean bean = mDataList.get(position);
		// 设置控件的数据
		// holder.img.setImageResource(bean.itemImageResid);
		// holder.title.setText(bean.itemTitle);
		// holder.content.setText(bean.itemContent);
		// long end = System.nanoTime();
		// long dValue = end - start;
		// mSumTime += dValue;
		// 输出每次getView消耗的时间和
		// Log.d("xys", String.valueOf(mSumTime));
		// return convertView;
		// 文艺式 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>16325820
	}

	// ViewHolder用于缓存控件
	class ViewHolder {

		public TextView title;
		public TextView content;
		public TextView author;
		public ImageView img;
		// public RatingBar ratingbar;

	}

}
