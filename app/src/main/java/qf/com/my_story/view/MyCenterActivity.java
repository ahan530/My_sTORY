package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

public class MyCenterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ImageView back_iv;//返回图标
    private TextView username;//用户名
    private EditText nickname;//昵称
    private String userpass,user_name,id,sex;//首页传过来的用户标示和用户名和ID 选择的性别
    private EditText email;// 邮箱
    private ImageView imageView_head;//头像
    private String lastUri;//图库图片跟地址
    private Spinner spinner; //下拉列表
    private ImageView sex_img;//性别选择
    private List<String> data= Arrays.asList("女","男");
    private ArrayAdapter adapter; //下拉列表的适配器
    private Button button;//确认按钮
    private TextView textView_pwd;//点击修改密码
    private String photoPath;//照片路径
    public  static  final int TAKE_PHOTO=1;  //
    public  static  final String tupe="inmage/*";//照片路径
    public  static  final int RESULT=2;  //压缩返回照片的tag
    private String filePath;//文件路径
    private String data1_nickname;//修改后的昵称
    private SharedPreferences sp;

    private String over_nickname,over_email,over_sex,over_img;//修改数据成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        FindById();

        //软返回
        GoBack();
        //处理从首页跳转的数据
        dealFromFirst();
        //设置监听
        SetListener();
        //下拉列表添加数据
        ForSpinner();
    }
    //下拉列表
    private void ForSpinner() {
        adapter=new ArrayAdapter(MyCenterActivity.this,android.R.layout.simple_list_item_1,data);
        spinner.setAdapter(adapter);
        textView_pwd.setOnClickListener(this);
    }

    //设置事件监听
    private void SetListener() {
        imageView_head.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        button.setOnClickListener(this);
    }

    //找控件
    private void FindById() {
        back_iv = (ImageView) findViewById(R.id.mycenter_back);
        username = (TextView) findViewById(R.id.mycenter_username);
        nickname = (EditText) findViewById(R.id.mycenter_nickname);
        email = (EditText) findViewById(R.id.mycenter_email);
        imageView_head = (ImageView) findViewById(R.id.mycenter_headimg);
        spinner = (Spinner) findViewById(R.id.spinner);
        sex_img = (ImageView) findViewById(R.id.mycenter_seximg);
        button = (Button) findViewById(R.id.mycenter_btn);
        textView_pwd = (TextView) findViewById(R.id.mycenter_pwd);

    }

    //处理从首页跳转的数据
    private void dealFromFirst() {
        Intent intent=getIntent();
        String keyfromfirst = intent.getStringExtra("keyfromfirst");
        try {
            JSONObject object=new JSONObject(keyfromfirst);
            JSONObject object1 = object.optJSONObject("data");
             user_name = object1.getString("username");
            id=object1.getString("id");
            String nick_name = object1.getString("nickname");
            String portrait = object1.getString("portrait");
             userpass = object1.getString("userpass");
            username.setText(user_name);
            nickname.setText(nick_name);
            Picasso.with(MyCenterActivity.this)
                    .load(Urls.headImageUrl+portrait)
                    .fit()
                    .into(imageView_head);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //软返回
    private void GoBack() {
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
   //监听事件的处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mycenter_headimg:
                ChooseImg();
              /*  takePhoto();*/
                break;
            case R.id.mycenter_btn:
                 SendMessage("changeNickName","nickname",nickname.getText().toString().trim(),1);
                 SendMessage("changeSex","usersex",sex,2);
                 SendMessage("changeEmail","useremail",email.getText().toString().trim(),3);
                if (null!=lastUri){
                    SendMessageforHeadimg();
                }
                Toast.makeText(this,over_email+"/"+over_sex+"/"+over_nickname+"/"+over_img, Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            Intent intent=new Intent(MyCenterActivity.this,FirstActivity.class);
                            intent.putExtra("from_centername",data1_nickname);

                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();

                break;
            case R.id.mycenter_pwd:
                Intent intent1=new Intent(MyCenterActivity.this,ChangedPwdActivity.class);
                intent1.putExtra("uid",id);
                startActivity(intent1);
                break;
        }

    }
    //拍照
    private void takePhoto() {
        Intent intenta=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intenta.putExtra(MediaStore.EXTRA_OUTPUT,Uri.parse(photoPath));
        startActivityForResult(intenta,TAKE_PHOTO);

    }

    //选择照片
    private void ChooseImg() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 102);
    }

    //修改头像
    private void SendMessageforHeadimg() {
        Log.i("info", "onSuccess:==我是图片根地址1= "+lastUri+"/////"+id+"/////"+userpass);
        OkGo.post(Urls.jieKouUrl+"changePortrait")
                .tag(MyCenterActivity.this)
                .params("uid",id)
                .params("userpass",userpass)
                .params("portrait",new File(lastUri))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject object=new JSONObject(s);
                            String result = object.getString("result").trim();
                            if ("1".equals(result)){
                        String    data_img = object.getString("data");
                                sp =getSharedPreferences("file_info", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("portrait",data_img);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    //确认修改并且上传数据
    private void SendMessage(String s, String s_key, String s_view, final int a) {
        OkGo.post(Urls.jieKouUrl+s)
                .tag(MyCenterActivity.this)
                .params("uid",id)
                .params("userpass",userpass)
                .params(s_key,s_view)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject obj=new JSONObject(s);
                            String result = obj.getString("result").trim();
                            String msg = obj.getString("msg");
                            if (1==a){
                                if ("1".equals(result)){
                                data1_nickname = obj.getString("data");
                                    over_nickname="修改昵称成功";
                                }

                                Log.i("info", "onSuccess:==我是修改昵称= "+msg+result);
                            }else if (2==a){
                                String data2 = obj.getString("data");
                                if ("1".equals(result)){
                                    over_sex="修改性别成功";
                                }
                                Log.i("info", "onSuccess:==我是修改性别= "+msg+result+data2);
                            }else if (3==a){

                                if ("1".equals(result)){
                                    over_email="修改邮箱成功";
                                }
                                String data3 = obj.getString("data");
                                Log.i("info", "onSuccess:==我是修改邮箱= "+msg+result+data3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //处理带回来的头像图片数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null!=data){
        if(requestCode==102){

            Uri uri = data.getData();
            //获取图片路径集合
            String[] filePath=new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(uri,filePath,null,null,null);
            while (cursor.moveToNext()){
                lastUri= cursor.getString(cursor.getColumnIndex(filePath[0]));
            }
            cursor.close();
            Picasso.with(MyCenterActivity.this)
                    .load(uri)
                    .fit()
                    .into(imageView_head);

        }
        if (requestCode==TAKE_PHOTO) {
            startZoom(Uri.parse(filePath));

        }
        if (requestCode==RESULT){
            Bundle bundle=data.getExtras();
            Bitmap bitmap=bundle.getParcelable("data");
            imageView_head.setImageBitmap(bitmap);//显示
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(new File(photoPath)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    }
    //裁剪
    private void startZoom(Uri uri) {
        Intent intentb=new Intent("com.android,camera.action.CROP");
        intentb.setDataAndType(uri,tupe);
        intentb.putExtra("crop","true");
        intentb.putExtra("aspectX",1);
        intentb.putExtra("outputX",300);
        intentb.putExtra("outputY",300);
        intentb.putExtra("outputY",300);
        intentb.putExtra("return-data",true);
        startActivityForResult(intentb,RESULT);
    }

    //设置男女标示
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = data.get(position);
        if ("男".equals(s)){
            sex="1";
            Picasso.with(MyCenterActivity.this)
                    .load(R.drawable.icon_man)
                    .into(sex_img);
        }
        else {
            sex="0";
            Picasso.with(MyCenterActivity.this)
                    .load(R.drawable.icon_woman)
                    .into(sex_img);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
