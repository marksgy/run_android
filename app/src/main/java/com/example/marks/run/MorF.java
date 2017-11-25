package com.example.marks.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MorF extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mor_f);
    }

    public void male(View view){
        setGender(true);
        next();
    }
    public void female(View view){
        setGender(false);
        next();
    }
    public void next(){
        Intent intent = new Intent(MorF.this,
                Grade.class);
        startActivity(intent);
        finish();
    }
    public void setGender(boolean b){
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //添加数据
        editor.putBoolean("isMale", b);
        //提交
        editor.commit();
    }
}
