package com.peso.model;

import java.util.ArrayList;
import java.util.List;

import Thread.CommThread;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peso.ItemView;
import com.peso.R;
import com.peso.view.SwipeBackActivity;

import dbmodels.Publication;

/*
 * 创建人：
 * 修改人：周映凯，添加了高级搜索数据回传功能。
 * 
 * 2015.9.21 修改人：滕真久
 * 内容 ：activity之间数据传输成功  完成了除模糊搜索外的功能
 * 
 * 
 * 
 */
public class SearchActivity extends SwipeBackActivity {

	private AutoCompleteTextView edit; // 定义输入文本框
	private ImageView img_back_search;// 返回按钮
	private Button btn_search;// 搜索按钮
	private TextView advanced_search;
	private ItemView mAdapter; // 适配器
	private ListView mMainListView;
	private SharedPreferences sharedPreferences;
	private List<Publication> dataList = new ArrayList<Publication>();
	private String res[] = { "aa", "a", "bb" };

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		mMainListView = (ListView) findViewById(R.id.list_view_search);

		img_back_search = (ImageView) findViewById(R.id.sign_arrow);

		/*
		 * dataList.add(new ItemBean( // R.id.list_item_ratingBar,
		 * R.drawable.download,
		 * "Auto-adjusted shape prior-based interactive segmentation via point set registration"
		 * , "Liman Liu;Kunqian Li;Wenbing Tao;Haihua Liu;",
		 * "A novel object specified segmentation approach based on the auto-adjusted shape prior by utilising the point set registration method is presented. Three steps are included in the proposed method: (i) initial segmentation, (ii) automatic shape prior adjustment by point set registration and (iii) object segmentation constrained by the adjusted shape prior. To repair the shape difference between the local targets and the given shape model, such as location, scale, rotation and local shape details, an excellent point set registration approach named coherent point drift (CPD) is adopted. The adjusted shape constraints under the coherent moving constraint implied in CPD give reliable boundary predictions. Experimental results on the ETHZ shape dataset have demonstrated the outstanding performance of the proposed method."
		 * , null, null, null, null, null));
		 */
		mAdapter = new ItemView(this, dataList);
		mMainListView.setAdapter(mAdapter);

		edit = (AutoCompleteTextView) findViewById(R.id.search_comments);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.search_list_item, res);
		edit.setAdapter(adapter);

		// String text = edit.getText().toString();//将输入文本框的值赋给text变量
		btn_search = (Button) findViewById(R.id.search_button);
		advanced_search = (TextView) findViewById(R.id.advanced_search);
		
		
		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/GothamRounded-Medium.otf");
		advanced_search.setTypeface(typeface);
		edit.setTypeface(typeface);
		
		btn_search.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit.getText().length() == 0) {
					Toast.makeText(SearchActivity.this,
							"Type something...",
							1000).show();
					return;
				}
				dataList.clear();

				sharedPreferences = getSharedPreferences(
						"UserInfo", MODE_PRIVATE);
				String username = sharedPreferences.getString(
						"username",
						"passager_null_name");
				Toast.makeText(SearchActivity.this,
						edit.getText().toString(), 1000)
						.show();
				CommThread search = new CommThread("search"
						+ edit.getText().toString()
						+ "username" + username); // 构造
										// search（.*）
										// 前边的search作为标示在服务器识别,顺带构造了username为了让服务器接受到用户名
				search.start();

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				}
				if (search.publist.size() == 0) {
					Toast.makeText(SearchActivity.this,
							"未搜索到论文", 1000).show();
					return;
				}
				if (!search.publist.isEmpty()) {
					Log.i("传来论文数量",
							""
									+ search.publist.size());

					Log.i("传来论文数量",
							""
									+ search.publist.size());
					for (Publication p : search.publist) {
						dataList.add(p);
						mAdapter.notifyDataSetChanged();
					}
				}
			}
		});// edit.getText().toString()可以获得文本框对应输入的值

		img_back_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.push_left_inn,
						R.anim.push_left_outt);
			}
		});

		advanced_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SearchActivity.this,
						Advanced_Search.class);
				startActivityForResult(intent, 1000);

			}
		});

		/*
		 * btn_search=(Button) findViewById(R.id.search_button);
		 * btn_search.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO
		 * Auto-generated method stub Intent intent = new
		 * Intent(SearchActivity.this,CommentActivity.class);
		 * startActivity(intent); } });
		 */

	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		ArrayList<Publication> publist = new ArrayList<Publication>();
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == 1001) {
			// 获取传送回来的数据。
			String publist1 = data.getStringExtra("data");
			Gson gson = new Gson();
			publist = gson.fromJson(
					publist1,
					new TypeToken<ArrayList<Publication>>() {
					}.getType());
			Log.i("yes", "activity之间解析成功");
		}
		dataList.clear();
		if (!publist.isEmpty()) {
			Log.i("传来论文数量", "" + publist.size());
			mAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

}
