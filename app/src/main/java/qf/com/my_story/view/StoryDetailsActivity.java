package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import qf.com.my_story.R;
import qf.com.my_story.adapter.MyStory_adapter2;
import qf.com.my_story.utils.Urls;

public class StoryDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView_back,xq_headimg;//头像和返回键
    private SharedPreferences sp;
    private TextView uid_text,city_text,comment_text, readcount_text,info_text, time_text;//用户名
    private RecyclerView recyclerView;
    private MyStory_adapter2 adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storydetails);

        FindById();
        Setlistener();
        //读取数据
        ReadSp();

    }

    private void ReadSp() {
        sp =getSharedPreferences("file_info", Context.MODE_PRIVATE);
        String readcount = sp.getString("readcount", "").trim();
        String comment = sp.getString("comment", "").trim();
        String story_time = sp.getString("story_time", "").trim();
        String story_info = sp.getString("story_info", "");
        String uid = sp.getString("uid", "");
        String sid = sp.getString("sid", "");
        String user_portrait = sp.getString("portrait_user","");
        Log.i("info", "ReadSp:2222222222222233322222222222 "+user_portrait+"|"+comment+"|"+readcount);

          if (null!=uid){
              uid_text.setText(uid);
          }
        if (null!=user_portrait){
            Picasso.with(this)
                    .load(Urls.headImageUrl+user_portrait)
                    .fit()
                    .into(xq_headimg);
          }
        if (null!=comment){
            comment_text.setText(comment);
          }
        if (null!=readcount){
            readcount_text.setText(readcount);
        }
        if (null!=story_info){
            info_text.setText(story_info);
        }
        if (null!=story_time){
            time_text.setText(story_time);
        }



        Intent intent=getIntent();
        ArrayList<String> fromFramnet1 = intent.getStringArrayListExtra("fromFramnet1");
        List<String> data_pics = (List<String>) fromFramnet1;
        recyclerView.setLayoutManager(new LinearLayoutManager(this,1,false));
        adapter2=new MyStory_adapter2(data_pics,this,9);
        recyclerView.setAdapter(adapter2);

    }

    private void FindById() {
        imageView_back = (ImageView) findViewById(R.id.storydetails_back);
        xq_headimg = (ImageView) findViewById(R.id.xq_headimg);
        uid_text = (TextView) findViewById(R.id.xq_uid);
        city_text = (TextView) findViewById(R.id.xq_city);
        comment_text = (TextView) findViewById(R.id.xq_comment);
        readcount_text = (TextView) findViewById(R.id.xq_readcount);
        info_text = (TextView) findViewById(R.id.xq_info);
        time_text = (TextView) findViewById(R.id.xq_time);
        recyclerView = (RecyclerView) findViewById(R.id.xq_recycleview);
    }
//监听
    private void Setlistener() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
