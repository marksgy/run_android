package com.example.marks.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signup extends Activity implements CompoundButton.OnCheckedChangeListener{

    TextView day,time;
    EditText name,psd,studentId;
    MyHandler myHandler;
    CheckBox checkBox;
    Button button;
    final OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
    }

    public void initView(){
        day=findViewById(R.id.day2);
        time=findViewById(R.id.time2);
        name=findViewById(R.id.name2);
        psd=findViewById(R.id.psd2);
        studentId=findViewById(R.id.studentId);
        checkBox=findViewById(R.id.checkBox);
        button=findViewById(R.id.buttonsign);
        checkBox.setOnCheckedChangeListener(this);
        myHandler=new MyHandler(this);



    }

    public void signUp(View view){
        String mName=name.getText().toString().trim();
        String mPsd=psd.getText().toString().trim();
        String mStudentId=studentId.getText().toString().trim();
        if(name.length()==0){
            Toast.makeText(this, "请输入用户名",Toast.LENGTH_SHORT).show();
        } else if (psd.length() == 0) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else if (studentId.length()==0){
            Toast.makeText(this, "请输入学号", Toast.LENGTH_SHORT).show();
        } else {
            postRequest(mName,mPsd,mStudentId);
        }

    }

    private void postRequest(String name,String pwd,String studentId)  {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
                .add("name",name)
                .add("pwd",pwd)
                .add("studentId",studentId)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http://106.14.165.161:8080/signup/")
                .post(formBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String cookie = response.header("sessionid");



                        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //添加数据

                        editor.putString("cookie", cookie);
                        //提交
                        editor.commit();

                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        myHandler.obtainMessage(1, response.body().string()).sendToTarget();

                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            button.setEnabled(true);
        }else {
            button.setEnabled(false);
        }
    }


    static class MyHandler extends Handler{

        //对Activity的弱引用
        private final WeakReference<Activity> mActivity;

        public MyHandler(Activity activity){
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg){
            Activity activity=mActivity.get();
            switch (msg.what) {
                case 1:
                    JSONObject jsonObject=null;
                    try {
                        jsonObject=new JSONObject(msg.obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (jsonObject!=null){
                        int status=0;
                        try {
                            status=jsonObject.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            if (status==2){
                                Toast.makeText(activity,"该用户名已注册",Toast.LENGTH_SHORT).show();
                            }else {
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("user_info", Context.MODE_PRIVATE);
                                //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                boolean b=sharedPreferences.getBoolean("isSelected",false);
                                //添加数据
                                editor.putBoolean("isLogin", true);
                                //提交
                                editor.commit();
                                if (b){
                                    Intent intent = new Intent(activity,
                                            Map.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }else{
                                    Intent intent = new Intent(activity,
                                            MorF.class);
                                    activity.startActivity(intent);
                                    activity.finish();
                                }
                            }
                        }
                    }
                    break;

            }





        }
    }
}
