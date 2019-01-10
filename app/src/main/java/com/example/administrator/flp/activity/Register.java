package com.example.administrator.flp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.flp.R;
import com.example.administrator.flp.utils.HttpUtil;
import com.example.administrator.flp.utils.StreamTool;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Register extends AppCompatActivity implements View.OnClickListener {
    // TODO:注册界面
    private TextView tv_main_title,tvNext,tvBack;
    private Button btnSignin;
    private ImageView iv_show;
    private EditText et_name, et_user, et_pass, et_pass2;
    private String name, user, password, password2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        title();
    }
//设计标题
    private void title() {
        tv_main_title.setText("注册");
        tvNext.setText("");
        tvBack.setBackgroundResource(R.drawable.title_2);
    }
    //初始化控件
    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        et_user = (EditText) findViewById(R.id.et_user);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass2 = (EditText) findViewById(R.id.et_pass2);
        iv_show = (ImageView) findViewById(R.id.iv_show);
        tvNext = (TextView) findViewById(R.id.tv_next);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        tvBack = (TextView) findViewById(R.id.tv_back);

        iv_show.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        btnSignin.setOnClickListener(this);
    }

    //返回，注册，显示密码监听
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_back:
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_signin:
                data();
                break;
            case R.id.iv_show:
                if (et_pass.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                    et_pass.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    et_pass2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    iv_show.setImageResource(R.drawable.a5_1);
                } else {
                    iv_show.setImageResource(R.drawable.a5_2);
                    et_pass.setInputType(InputType.TYPE_CLASS_NUMBER);
                    et_pass2.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                break;
        }

    }
//获取输入内容进行判断，开启一条子线程
    private void data() {
        user = et_user.getText().toString().trim();
        name = et_name.getText().toString().trim();
        password = et_pass.getText().toString().trim();
        password2 = et_pass2.getText().toString().trim();

        if (user.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (name.equals("")) {
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals("")) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (password2.equals("")) {
                        Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!password.equals(password2)) {
                            Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                        } else {

                            new Thread(rsignin).start();

                        }
                    }
                }
            }
        }
    }

    Runnable rsignin = new Runnable() {
        public void run() {
            //      http://123.207.85.214/chat/register.php
            String path = new HttpUtil("register.php").getUrl;
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(5000);
                String outData = "name=" + URLEncoder.encode(name)
                        + "&user=" + URLEncoder.encode(user)
                        + "&password=" + URLEncoder.encode(password);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(outData.getBytes());
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();


                    byte[] data = StreamTool.read(in);

                    String json = new String(data, "UTF-8");
                    final String status = parseJson(json);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, status, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //解析JSON对象
    public String parseJson(String json) {
        String status = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

}
