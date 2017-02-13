package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

public class NewStoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView_send,imageView_back,imageView_image,imageView_camera;//返回，小飞机，打开图片，打开相机；
    private Uri uri;//相册或者相机选择的地址
    private String lastUri;//图片根路径
    private ImageView showimg;//展示选择的图片
    private SharedPreferences sp;
    private EditText editText;  //分享的文字内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);
        //绑定控件
        FindById();
        SetListener();
    }
   //设置监听
    private void SetListener() {
        imageView_back.setOnClickListener(this);
        imageView_send.setOnClickListener(this);
        imageView_image.setOnClickListener(this);
        imageView_camera.setOnClickListener(this);

    }

    //找到控件
    private void FindById() {
        imageView_send = (ImageView) findViewById(R.id.newstory_send);
        imageView_back= (ImageView) findViewById(R.id.newstory_back);
        imageView_image=(ImageView) findViewById(R.id.newstory_image);
        imageView_camera=(ImageView) findViewById(R.id.newstory_camera);
        showimg = (ImageView) findViewById(R.id.newstory_showimg);
        editText = (EditText) findViewById(R.id.newstory_et);
    }
    //事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newstory_back:
                onBackPressed();
                break;
            case R.id.newstory_send:
                toSend();
                break;
            case R.id.newstory_image:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 103);
                break;
            case R.id.newstory_camera:
                Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent2, 1);
                break;
        }

    }
    //确认分享
    private void toSend() {
        try {
        sp = getSharedPreferences("file_pwd", Context.MODE_PRIVATE);
        String id = sp.getString("id", "").toString().toString();
        String userpass = sp.getString("userpass", "").toString().toString();
        String share = editText.getText().toString();
        if (null!=id && null!=userpass && null!=share){
        OkGo.post(Urls.jieKouUrl+"sendStory")
                .tag(NewStoryActivity.this)
                .params("uid",id)
                .params("story_info",share)
                .params("userpass",userpass)
                .params("lng",18)
                .params("lat",19)
                .params("photo[]",new File(lastUri))
                .params("city","成都")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        JSONObject obj= null;
                        try {
                            obj = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String  msg = null;
                        try {
                            msg = obj.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(NewStoryActivity.this, ""+msg, Toast.LENGTH_SHORT).show();

                    }
                });
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //从相册带回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==103) {
            if (data != null) {
                uri = data.getData();
                //获取图片路径集合
                String[] filePath = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                while (cursor.moveToNext()) {
                    lastUri = cursor.getString(cursor.getColumnIndex(filePath[0]));
                }
                cursor.close();
                Picasso.with(NewStoryActivity.this)
                        .load(uri)
                        .fit()
                        .into(showimg);
            }
        }
    }
}
