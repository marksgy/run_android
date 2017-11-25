package com.example.marks.run;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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

public class Login extends Activity implements View.OnClickListener{


    TextView day;
    TextView time;
    TextView word;
    TextView month;
    TextView date;
    TextView signup;
    EditText name;
    EditText psd;

    final OkHttpClient client = new OkHttpClient();
    MyHandler mHandler=new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView(){
        day=findViewById(R.id.day);
        time=findViewById(R.id.time);
        month=findViewById(R.id.month);
        date=findViewById(R.id.date);
        word=findViewById(R.id.word);
        name=findViewById(R.id.name);
        psd=findViewById(R.id.psd);
        signup=findViewById(R.id.signup);
        signup.setOnClickListener(this);
        setInitTime(day,time,month,date);
    }
    public void login(View view){
        String mName=name.getText().toString().trim();
        String mPsd=psd.getText().toString().trim();
        postRequest(mName,mPsd);

    }


    private void postRequest(String name,String pwd)  {
        //建立请求表单，添加上传服务器的参数
        RequestBody formBody = new FormBody.Builder()
                .add("name",name)
                .add("pwd",pwd)
                .build();
        //发起请求
        final Request request = new Request.Builder()
                .url("http://106.14.165.161/login/")
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
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        String cookie = response.header("sessionid");



                        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                        //获取操作SharedPreferences实例的编辑器（必须通过此种方式添加数据）
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //添加数据

                        editor.putString("cookie", cookie);
                        //提交
                        editor.commit();
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();

                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void setInitTime(TextView day,TextView time,TextView month,TextView date){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));//星期
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute ="";
        if (c.get(Calendar.MINUTE)==0){
            mMinute ="00";
        }else if (c.get(Calendar.MINUTE)<10){
            mMinute = "0"+String.valueOf(c.get(Calendar.MINUTE));
        }else {
            mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        }
        switch (mMonth) {
            case "1":
                mMonth="Jan";
                break;
            case "2":
                mMonth="Feb";
                break;
            case "3":
                mMonth="Mar";
                break;
            case "4":
                mMonth="Apr";
                break;
            case "5":
                mMonth="May";
                break;
            case "6":
                mMonth="Jun";
                break;
            case "7":
                mMonth="Jul";
                break;
            case "8":
                mMonth="Aug";
                break;
            case "9":
                mMonth="Sep";
                break;
            case "10":
                mMonth="Oct";
                break;
            case "11":
                mMonth="Nov";
                break;
            case "12":
                mMonth="Dec";
                break;
        }
        if("1".equals(mWay)){
            mWay ="Sun";
        }else if("2".equals(mWay)){
            mWay ="Mon";
        }else if("3".equals(mWay)){
            mWay ="Tue";
        }else if("4".equals(mWay)){
            mWay ="Wed";
        }else if("5".equals(mWay)){
            mWay ="Thu";
        }else if("6".equals(mWay)){
            mWay ="Fri";
        }else if("7".equals(mWay)){
            mWay ="Sat";
        }
        day.setText(mWay);
        time.setText(mHour+":"+mMinute);
        month.setText(mMonth);
        date.setText(mDay);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(Login.this,Signup.class);
        startActivity(intent);
    }


    private static class MyHandler extends Handler{

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
                            if (status==0){
                                Toast.makeText(activity,"登录失败",Toast.LENGTH_SHORT).show();
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
