package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar; //获取toolbar
    private EditText editText_id,editText_name,editText_pwd;//注册侧页面的编辑文本框
    private ImageView imageView_back; //返回图标
    private Button button; //确定注册
    private ImageView image_head;//选择头像
    private String name,id,pwd,lastUri;
    private Uri uri; //选择的图片地址
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //找到控件
        FindById();
        //设置监听
        setListener();
    }
    //控件的监听
    private void setListener() {
        image_head.setOnClickListener(this);
        button.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
    }

    //绑定控件
    private void FindById() {
        image_head = (ImageView) findViewById(R.id.register_headimg);
        toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        editText_id = (EditText) findViewById(R.id.register_et1);
        editText_name = (EditText) findViewById(R.id.register_et2);
        editText_pwd = (EditText) findViewById(R.id.register_et3);
        imageView_back=(ImageView)findViewById(R.id.register_back);
        button = (Button) findViewById(R.id.register_btn);

    }

    //注册完成
    public void register_btn(View view) {
        //携值跳转  登录页面  或者直接登录
         id = editText_id.getText().toString().trim();
         name = editText_name.getText().toString().trim();
         pwd = editText_pwd.getText().toString().trim();
        Intent intent=new Intent(RegisterActivity.this,LogInActivity.class);
        intent.putExtra("key_rg",id);
        intent.putExtra("key_nm",name);
        intent.putExtra("key_pwd",pwd);




        startActivity(intent);
        finish();
    }
    //打开图库带回数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101) {
            if (data != null) {
                uri = data.getData();
                //获取图片路径集合
                String[] filePath = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                while (cursor.moveToNext()) {
                    lastUri = cursor.getString(cursor.getColumnIndex(filePath[0]));
                }
                cursor.close();
                Picasso.with(RegisterActivity.this)
                        .load(uri)
                        .fit()
                        .into(image_head);
            }
        }
    }

    //控件的点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.register_headimg:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
                break;
            case R.id.register_btn:
                if (editText_id!=null && editText_pwd!=null && editText_name!=null){
                    PutIn();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "请填写完注册信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_back:
                onBackPressed();
                break;
        }
    }
    //提交信息
    private void PutIn() {
        final String id = editText_id.getText().toString().trim();
        final String pwd = editText_pwd.getText().toString().trim();
        final String name = editText_name.getText().toString().trim();
        if (null != lastUri){
            OkGo.post(Urls.jieKouUrl + "regist")
                    .tag(RegisterActivity.this)
                    .params("nikename", name)
                    .params("username", id)
                    .params("password", pwd)
                    .params("portrait", new File(lastUri))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                JSONObject obj = new JSONObject(s);
                                String msg = obj.getString("msg");
                                String result = obj.getString("result").trim();
                                String data = obj.getString("data").trim();//获取到头像地址
                                if ("1".equals(result)) {
                                    final Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                                    intent.putExtra("key1", id);
                                    intent.putExtra("key2", pwd);
                                    sp =getSharedPreferences("file_info", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edit = sp.edit();
                                    edit.putString("portrait",Urls.headImageUrl+data);

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(1000);
                                                startActivity(intent);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).start();

                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
    }else {
            Toast.makeText(RegisterActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
        }
    }
}
