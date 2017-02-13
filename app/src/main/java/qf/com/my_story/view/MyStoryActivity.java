package qf.com.my_story.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.adapter.MyStory_adapter;
import qf.com.my_story.bean.GetStory;
import qf.com.my_story.utils.ShareCallBack;
import qf.com.my_story.utils.Urls;

public class MyStoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView_back;
    private RecyclerView recyclerView;
    private  String uid;//来自首页的数据
    private MyStory_adapter adapter;
    private List<GetStory> getStory=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        //找到控件
        FindById();
        SetListener();

        FromFirst();

        DealData();

        adapter=new MyStory_adapter(getStory,MyStoryActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyStoryActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        //share图标的回掉
        ShareToMyStory();
    }
    //share图标的点击处理
    private void ShareToMyStory() {
        adapter.setShareCallBack(new ShareCallBack() {
            @Override
            public void GoBack(View view, int postion,String a) {
                Toast.makeText(MyStoryActivity.this, ""+postion, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void JsonBack(View view, GetStory data) {

            }
        });
    }

    //下载接口
    private void DealData() {

        OkGo.post(Urls.jieKouUrl+"myStorys")
                .tag(MyStoryActivity.this)
                .params("uid",uid)
                .params("page",1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        GetStory gstory=null;
                        try {
                            JSONObject object=new JSONObject(s);
                            JSONArray jsonarray=object.getJSONArray("data");

                            List<GetStory> getStories=new ArrayList<GetStory>();
                             List<String>  ss =null;
                            for (int i = 0; i <jsonarray.length() ; i++) {
                                JSONObject obj=jsonarray.getJSONObject(i);
                                JSONArray array = obj.optJSONArray("pics");
                                ss= new ArrayList<>();
                                if (array!=null){
                                    for (int j = 0; j <array.length() ; j++) {
                                        String string = array.getString(j);
                                        ss.add(string);
                                    }
                                }
                                gstory=new GetStory();
                                gstory.setUid(obj.getString("id"));
                                gstory.setComment(obj.getString("comment"));
                                gstory.setStory_info(obj.getString("story_info"));
                                gstory.setStory_time(obj.getLong("story_time"));
                                gstory.setReadcount(obj.getString("readcount"));
                                gstory.setCity(obj.getString("city"));
                                gstory.setData_pics(ss);
                                getStories.add(gstory);
                            }
                            getStory.addAll(getStories);

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    //来自首页的数据
    private void FromFirst() {
        Intent intent=getIntent();
        uid = intent.getStringExtra("toMyStory");
    }

    //设置监听
    private void SetListener() {
        imageView_back.setOnClickListener(this);
    }

    //找到控件
    private void FindById() {
        imageView_back = (ImageView) findViewById(R.id.mystory_ivback);
        recyclerView = (RecyclerView) findViewById(R.id.mystory_recycle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mystory_ivback:
                onBackPressed();
                break;

        }
    }



}
