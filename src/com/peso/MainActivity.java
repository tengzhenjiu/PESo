package com.peso;

/**
 * 主入口，启动app欢迎界面，延时功能和判断功能
 */

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.peso.model.WelcomeActivity;

public class MainActivity extends Activity {
	private SharedPreferences sharepreferences;// 实例化 SharedPreferences
	private SharedPreferences.Editor editor;

	@SuppressLint("CommitPrefEdits")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharepreferences = this.getSharedPreferences("check",
				MODE_PRIVATE);// 初始化
						// SharedPreferences
						// 储存
		editor = sharepreferences.edit();// 将SharedPreferences 储存 可编辑化

		final Intent it = new Intent(this, WelcomeActivity.class); // 初始化跳转welcomeActivity
		final Intent ittomain = new Intent(this,
				MaininterfaceActivity.class);// 初始化跳转MaininterfaceActivity
		Timer timer = new Timer(); // 创建一个定时器实例
		firstloadjudge(timer, it, ittomain); // 调用 判断第一次app启动的方法

	}

	public void firstloadjudge(Timer timer, Intent it, Intent ittomain) {
		boolean fristload = sharepreferences.getBoolean("fristload",
				true);// 从SharedPreferences中获取是否第一次启动
		if (fristload) {
			delaytowel(timer, it);// 第一次进入延迟2秒后进入WelcomeActivity
			editor.putBoolean("fristload", false);// 第一次启动后，将firstload
								// 置为false
								// 以便以后直接进入主界面不再显示欢迎界面
			editor.commit(); // 提交，执行操作
		} else {
			// Toast.makeText(MainActivity.this,
			// "你不是第一次进入，应进入app主界面",
			// Toast.LENGTH_SHORT).show();
			delaytomain(timer, ittomain); // 延时并且进入主界面
		}

	}

	public void delaytowel(Timer timer, final Intent it)// 延时并进入welcome的方法
	{
		TimerTask task = new TimerTask() {
			@Override
			public void run() {

				startActivity(it); // 执行进入
			}
		};
		timer.schedule(task, 1000 * 1);// 1秒后就进入welcome
	}

	public void delaytomain(Timer timer, final Intent ittomain)// 延时并进入主界面的方法
	{
		TimerTask task = new TimerTask() {
			@Override
			public void run() {

				startActivity(ittomain); // 执行进入
			}
		};
		timer.schedule(task, 1000 * 1);// 1秒后就进入main
	}

}
