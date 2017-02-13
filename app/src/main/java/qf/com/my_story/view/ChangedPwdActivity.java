package qf.com.my_story.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

public class ChangedPwdActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_back;
    private EditText editText_old,editText_new,editText_new2;
    private Button button;
    private String uid;//从个人中心传过来的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changed_pwd);
        FindById();
        setListener();

        Intent intent=getIntent();
        uid = intent.getStringExtra("uid");
    }
    //设置监听
    private void setListener() {
        img_back.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    private void FindById() {
        img_back = (ImageView) findViewById(R.id.changed_back);
        editText_old = (EditText) findViewById(R.id.change_oldpwd);
        editText_new = (EditText) findViewById(R.id.change_newpwd);
        editText_new2 = (EditText) findViewById(R.id.change_newpwd2);
        button = (Button) findViewById(R.id.change_btn);

    }

 //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changed_back:
                onBackPressed();
                break;
            case R.id.change_btn:
                dealBtn();
                break;
        }
    }
   //确认按键的处理
    private void dealBtn() {
        String oldpwd = editText_old.getText().toString().trim();
        String newpwd = editText_new.getText().toString().trim();
        String newpwd2 = editText_new2.getText().toString().trim();
        if (null!=oldpwd && null!=newpwd && null!=newpwd2&& newpwd.equals(newpwd2) ){
            OkGo.post(Urls.jieKouUrl+"changePassword")
                    .tag(ChangedPwdActivity.this)
                    .params("uid",uid)
                    .params("oldpass",oldpwd)
                    .params("newpass",newpwd2)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("info", "onSuccess:8888888888 "+s);
                            try {
                                JSONObject object=new JSONObject(s);
                                String result = object.getString("result").trim();
                                if ("1".equals(result)){
                                    startActivity(new Intent(ChangedPwdActivity.this,LogInActivity.class));
                                }else {
                                    Toast.makeText(ChangedPwdActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }else {
            Toast.makeText(this, "密码为空或两次密码不一致", Toast.LENGTH_SHORT).show();
        }

        Intent intent=new Intent(ChangedPwdActivity.this,LogInActivity.class);

    }
}
