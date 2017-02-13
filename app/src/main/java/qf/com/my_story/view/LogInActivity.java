package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

public class LogInActivity extends AppCompatActivity implements  CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Toolbar toolbar; //获取toolbar
    private TextView noid; //注册的跳转的控件
    private EditText login_name,login_passwold;  //id和密码的输入
    private ImageView imageView;
    private String et1_name,et2_pwd;
    private CheckBox  checkBox;//记住密码
    private boolean  isChoose=false; //判断是否选中
    private SharedPreferences sp; //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findById();
        setListener();
        //读取密码
        readPwd2();
        //来自注册页面的跳转
        FromRegisterActivity();
    }

    //读取密码
    private void readPwd2() {
        sp = getSharedPreferences("file_pwd", Context.MODE_PRIVATE);
        String username1 = sp.getString("username", "").toString();
        if (null!= username1) {
            Log.i("info", "readPwd: =============" + username1);
            login_name.setText(username1);

        }
        String userpasswold = sp.getString("userpasswold", "").toString();
        Log.i("info", "readPwd: =============" + userpasswold);
        if (null != userpasswold) {
            login_passwold.setText(userpasswold);
        }
    }




    //设置监听
    private void setListener() {
        //判断是否选中的监听
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setChecked(true);
        noid.setOnClickListener(this);
        imageView.setOnClickListener(this);

    }

    //开始存储
    private void SPtoSave(String name ,String pwd,String id,String userpass) {
        sp=getSharedPreferences("file_pwd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("username",name);
        editor.putString("userpasswold",pwd);
        editor.putString("id",id);
        editor.putString("userpass",userpass);
        Log.i("info", "SPtoSave:密码是否存储成功 "+editor.commit());
    }

    //来自注册页面
    private void FromRegisterActivity() {
        Intent intent=getIntent();
        if (intent!=null){
        String name = intent.getStringExtra("key1");
        String pwd = intent.getStringExtra("key2");
            if (null!=name && null!=pwd) {
               login_name.setText(name);
               login_passwold.setText(pwd);
            }
        }else {
        }
    }





    //找到控件
    private void findById() {
        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        noid= (TextView) findViewById(R.id.login_noid);
        login_name = (EditText) findViewById(R.id.login_username);
        login_passwold = (EditText) findViewById(R.id.login_userpasswold);
        imageView = (ImageView) findViewById(R.id.login_back);
        checkBox = (CheckBox) findViewById(R.id.login__checkbox);

    }


   //登陆 和存储
    public void login_btn(View view) {
         et1_name = login_name.getText().toString().trim(); //账号
         et2_pwd = login_passwold.getText().toString().trim();//密码
        if (et1_name!=null && et2_pwd!=null){
            //符合条件跳转
            OkGo.post(Urls.jieKouUrl+"login")
                    .tag(LogInActivity.this)
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("username",et1_name)
                    .params("password",et2_pwd)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject obj=new JSONObject(s);
                                Log.i("info", "onSuccess:******* ");
                                String result = obj.getString("result").trim();
                                JSONObject object = obj.optJSONObject("data");
                                String id = object.getString("id").trim();
                                String userpass = object.getString("userpass");
                                if ("1".equals(result)){
                                    // 登陆成功 存储 账号密码
                                    if (isChoose=true){
                                        //存储 密码sp的设置
                                        SPtoSave(et1_name,et2_pwd,id,userpass);
                                    }else {
                                        //还有做取消登陆
                                      /*  SPtoSave("","");*/
                                    }
                                    Intent intent=new Intent(LogInActivity.this,FirstActivity.class);
                                    intent.putExtra("s",s);
                                    startActivity(intent);
                                    finish();
                                }else {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


        }
        else {
            //可以用dialog
            Toast.makeText(LogInActivity.this, "请输入正确的用户名和密码", Toast.LENGTH_SHORT).show();
        }
    }





    //判断是否选中
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Log.i("info", "onCheckedChanged:+++6666666+++ "+isChoose);

        if (isChecked=true){
            Log.i("info", "onCheckedChanged:++++++ "+isChecked +isChoose);
            isChoose=true;
        }if (isChecked=false){
            isChoose=false;
            Log.i("info", "onCheckedChanged:+++2222++ "+isChecked +isChoose);
        }

    }
    //跳转注册页面
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_noid:
                 startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                 finish();
                 break;
            case R.id.login_back:
                 onBackPressed();
                 finish();
                 break;
        }
    }
}
