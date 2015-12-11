package com.peso.model;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.peso.MaininterfaceActivity;
import com.peso.R;

public class WelcomeActivity extends Activity {
	private View view1, view2, view3, view4;
	private ViewPager mViewPager;
	private ArrayList<View> viewList;// 创建动态数组是为了存储布局
	private Button registerButton, lookButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wel_layout);
		inintView();

	}

	private void inintView() {
		mViewPager = (ViewPager) findViewById(R.id.wel); // 初始化主页面

		// view1=findViewById(R.id.wel_lay1);//初始化四个欢迎页面
		// view2=findViewById(R.id.wel_lay2);
		// view3=findViewById(R.id.wel_lay3);
		// view4=findViewById(R.id.wel_lay4);
		//
		LayoutInflater lf = getLayoutInflater().from(this); // 获取布局文件并初始化初始化四个欢迎页面
		view1 = lf.inflate(R.layout.welcome, null);
		view2 = lf.inflate(R.layout.welcome_2, null);
		view3 = lf.inflate(R.layout.welcome_3, null);
		view4 = lf.inflate(R.layout.welcome_4, null);

		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);

		// 添加PagerAdapter适配器，通过重写其中的几个方法将四个欢迎布局添加滑动动画
		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container,
					int position, Object object) {
				container.removeView(viewList.get(position));

			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}

			@SuppressLint("CutPasteId")
			@Override
			public Object instantiateItem(ViewGroup container,
					int position) {
				container.addView(viewList.get(position));
				initButtons();// 初始化按钮
				return viewList.get(position);
			}

		};
		mViewPager.setAdapter(pagerAdapter);// 将适配器添加到主布局中
	}

	// initButtons是一个初始化欢迎界面的所有按钮，监听事件。
	public void initButtons() {

		registerButton = (Button) view4
				.findViewById(R.id.wel4_resister_button);
		lookButton = (Button) view4.findViewById(R.id.wel4_look_button);
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent resigerIntent = new Intent(
						WelcomeActivity.this,
						RegisterActivity.class);
				startActivity(resigerIntent);

			}
		});

		lookButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent lookIntent = new Intent(
						WelcomeActivity.this,
						MaininterfaceActivity.class);
				startActivity(lookIntent);

			}
		});
	}
}
