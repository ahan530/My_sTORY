package qf.com.my_story.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import qf.com.my_story.R;
import qf.com.my_story.adapter.Fragment_adapter;
import qf.com.my_story.bean.GetStory;
import qf.com.my_story.utils.ShareCallBack;
import qf.com.my_story.utils.Urls;
import qf.com.my_story.view.StoryDetailsActivity;

import static qf.com.my_story.R.id.readcount;

/**
 * Created by linke on 2017/1/17 0017.
 * email:linke0530@163.com.
 */

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;//这是最新里面的listview
    private Fragment_adapter adapter; //listView的适配器
    private List<GetStory> getStory =new ArrayList<>();
    private SharedPreferences sp;
    private TwinklingRefreshLayout twinklingRefreshLayout;
    int page=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment1_listview);
        twinklingRefreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.twinkling_refresh);

        //设置监听
        SetListener();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //数据
        initDate(1);
        adapter=new Fragment_adapter(getStory,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setShareCallBack(new ShareCallBack() {
            @Override
            public void GoBack(View view, int postion,String s) {
                switch (view.getId()){
                    case R.id.share:
                        Toast.makeText(getActivity(),"我是share"+postion,Toast.LENGTH_SHORT).show();
                        break;
                    case readcount:
                        adapter.notifyDataSetChanged();
                        readstory(s);
                        break;
                }
            }
            @Override
            public void JsonBack(View view, GetStory data) {
                switch (view.getId()){
                    case R.id.comment:
                      toStoryDetails(view,data);
                        break;
                    case R.id.frist_layout:
                      toStoryDetails(view,data);
                        break;
                }
            }
            //点击心形图案后
            private void readstory(String uid) {
                OkGo.post(Urls.jieKouUrl+"readStorys")
                        .tag(getActivity())
                        .params("sid",uid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                try {
                                    JSONObject obj=new JSONObject(s);
                                    String msg = obj.getString("msg");
                                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
        return  view ;
    }
    //设置监听
    private void SetListener() {
        twinklingRefreshLayout.setOnRefreshListener(new TwinklingRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDate(1);
                       refreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        initDate(2);
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });
    }

    //下载数据
    private void initDate(int a) {
        if (a==2) {

            DoDifferent different = new DoDifferent() {
                @Override
                public void doThings() {

                    adapter.notifyDataSetChanged();
                }
            };

            OkGo.post(Urls.getStory)
                    .tag(getActivity())
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("type", "new")
                    .params("page", page)
                    .execute(new MStringCallback(different));
        }else  if (a==1){

            DoDifferent different = new DoDifferent() {
                @Override
                public void doThings() {
                  adapter.notifyDataSetChanged();

                }
            };

            OkGo.post(Urls.getStory)
                    .tag(getActivity())
                    .cacheMode(CacheMode.NO_CACHE)
                    .params("type", "new")
                    .params("page", page)
                    .execute(new MStringCallback(different));

        }
    }

    class MStringCallback extends StringCallback{

        private DoDifferent doDifferent;

        public MStringCallback(DoDifferent doDifferent) {
            this.doDifferent = doDifferent;
        }

        @Override
            public void onSuccess(String s, Call call, Response response) {
                GetStory gstory=null;
                try {
                    JSONObject object=new JSONObject(s);
                    String result = object.getString("result").trim();
                    JSONArray jsonarray=object.getJSONArray("data");
                    if ("1".equals(result)){
                        page++;
                    }

                    List<GetStory> getStories=new ArrayList<GetStory>();
                    List<String>  ss =null;
                    for (int i = 0; i <jsonarray.length() ; i++) {
                        JSONObject obj=jsonarray.getJSONObject(i);
                        JSONObject object1=obj.optJSONObject("user");
                        String portrait = object1.getString("portrait");
                        JSONArray array = obj.optJSONArray("pics");
                        gstory=new GetStory();
                        ss= new ArrayList<>();
                        if (array!=null){
                            for (int j = 0; j <array.length() ; j++) {
                                String string = array.getString(j);
                                if(null!=string){
                                    ss.add(string);
                                }
                            }
                            gstory.setData_pics(ss);
                        }
                        gstory.setUid(obj.getString("uid"));
                        gstory.setUid(obj.getString("id"));
                        gstory.setUsername(object1.getString("username"));
                        gstory.setComment(obj.getString("comment"));
                        gstory.setStory_info(obj.getString("story_info"));
                        gstory.setStory_time(obj.getLong("story_time"));
                        gstory.setReadcount(obj.getString("readcount"));
                        gstory.setCity(obj.getString("city"));
                        gstory.setPortrait(portrait);
                        getStories.add(gstory);
                    }

                    doDifferent.doThings();

                    getStory.addAll(getStories);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    interface DoDifferent{
        public void doThings();
    }

   //处理跳转盗故事详情
    private void toStoryDetails( View v,GetStory data ) {
        sp = getActivity().getSharedPreferences("file_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        String readcount = data.getReadcount();
        String comment = data.getComment();
        long story_time = data.getStory_time();
        String story_info = data.getStory_info();
        String uid = data.getUid();//用户Id;
        String sid = data.getSid();//故事ID
        String portrait = data.getPortrait();
        edit.putString("readcount",readcount);
        edit.putString("comment",comment);
        edit.putLong("story_time",story_time);
        edit.putString("story_info",story_info);
        edit.putString("uid",uid);
        edit.putString("sid",sid);
        edit.putString("portrait_user",portrait );
        ArrayList<String> data_pics = (ArrayList<String>) data.getData_pics();//这个没有传
        Intent intent=new Intent(getActivity(), StoryDetailsActivity.class);
        intent.putStringArrayListExtra("fromFramnet1",data_pics);
        startActivity(intent);
    }
}
