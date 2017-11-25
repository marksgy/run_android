package com.example.marks.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Grade extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void freshman(View view){
        setGrade(1);
        next();
    }

    public void sophomore(View view){
        setGrade(2);
        next();
    }

    public void junior(View view){
        setGrade(3);
        next();
    }

    public void senior(View view){
        setGrade(4);
        next();
    }
    public void setGrade(int g){
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //添加数据
        editor.putInt("Grade", g);
        editor.putBoolean("isFirstRun", false);
        editor.putBoolean("isSelected", true);
        //提交
        editor.commit();
    }
    public void next(){
        Intent intent = new Intent(Grade.this,
                Map.class);
        startActivity(intent);
        finish();

    }
}
