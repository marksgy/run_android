package com.example.marks.run;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);// 睡眠500毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = hand.obtainMessage();
                hand.sendMessage(msg);
            }

        }.start();
    }
    @SuppressLint("HandlerLeak")
    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (isLogin()) {
                if (isSelected()){
                    // 如果登陆了则进入主页
                    Intent intent = new Intent(MainActivity.this,
                            Map.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this,
                            MorF.class);
                    startActivity(intent);
                }


            } else {

                // 如果未登录则进入引导界面
                Intent intent = new Intent(MainActivity.this,
                        Login.class);
                startActivity(intent);
            }
            finish();
        };
    };

    // 判断是否是第一次启动程序 利用 SharedPreferences 将数据保存在本地
    private boolean isLogin() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "user_info", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isLogin) {
            return true;
        } else {

            return false;
        }
    }

    private boolean isSelected() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                "user_info", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        boolean isSelected = sharedPreferences.getBoolean("isSelected", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isSelected) {
            return true;
        } else {

            return false;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

        }
        return true;
    }

}

