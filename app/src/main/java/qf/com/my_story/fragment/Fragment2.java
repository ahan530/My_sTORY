package qf.com.my_story.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {
    RecyclerView recyclerView; //fragment2 的控件
    private Fragment_adapter adapter; //listView的适配器
    private List<GetStory> getStory =new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment2_recycleView);
        //设置recycleView的布局方式
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        //数据
        initData();
        adapter=new Fragment_adapter(getStory,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setShareCallBack(new ShareCallBack() {
            @Override
            public void GoBack(View view, int postion,String s) {
                switch (view.getId()) {
                    case R.id.share:
                    Toast.makeText(getActivity(), "haha", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void JsonBack(View view, GetStory data) {
              /*  case  R.id.comment:
                Toast.makeText(getActivity(), "haha222", Toast.LENGTH_SHORT).show();
                break;*/
            }


        });
/*
        //recycle的回掉
        adapter.setShareCallBack2(new ShareCallBack2() {
            @Override
            public void GoBack(View view, int postion) {
                Toast.makeText(getActivity(),"我是评论"+postion,Toast.LENGTH_SHORT).show();
            }
        });*/

        return  view ;
    }

    //下载数据
    private void initData() {
        OkGo.post(Urls.getStory)
                .tag(getActivity())
                .cacheMode(CacheMode.NO_CACHE)
                .params("type","hot")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        GetStory gstory=null;
                        try {
                            JSONObject object=new JSONObject(s);
                            JSONArray jsonarray=object.getJSONArray("data");

                            List<GetStory> getStories=new ArrayList<GetStory>();
                            List<String>ss=null;
                            for (int i = 0; i <jsonarray.length() ; i++) {
                                JSONObject obj=jsonarray.getJSONObject(i);
                                JSONObject object1=obj.optJSONObject("user");
                                String portrait = object1.getString("portrait");
                                JSONArray array = obj.optJSONArray("pics");
                                ss= new ArrayList<>();
                                if (array!=null){
                                    for (int j = 0; j <array.length() ; j++) {
                                        String string = array.getString(j);

                                        if(null!=string){
                                            ss.add(string);
                                        }
                                    }
                                }
                                gstory=new GetStory();
                                gstory.setUid(obj.getString("uid"));
                                gstory.setUid(obj.getString("id"));
                                gstory.setComment(obj.getString("comment"));
                                gstory.setStory_info(obj.getString("story_info"));
                                gstory.setStory_time(obj.getLong("story_time"));
                                gstory.setReadcount(obj.getString("readcount"));
                                gstory.setCity(obj.getString("city"));
                                gstory.setPortrait(portrait);
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


}
