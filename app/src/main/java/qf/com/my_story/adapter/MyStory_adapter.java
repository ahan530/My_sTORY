package qf.com.my_story.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import qf.com.my_story.R;
import qf.com.my_story.bean.GetStory;
import qf.com.my_story.utils.ShareCallBack;
import qf.com.my_story.view.MyStoryActivity;

/**
 * Created by linke on 2017/2/8 0008.
 * email:linke0530@163.com.
 */

public class MyStory_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<GetStory> data;
    Context context;
    MyStory_adapter2 adapter2;
   ShareCallBack shareCallBack;
    int hang=2;
    public void  setShareCallBack(ShareCallBack shareCallBack){
        this.shareCallBack=shareCallBack;
    }


    public MyStory_adapter(List<GetStory> getStory, MyStoryActivity myStoryActivity) {
        this.data=getStory;
        this.context=myStoryActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mystory_item,null,false);

        return  new MyStory_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder=(ViewHolder)holder;
        GetStory story=data.get(position);
        viewHolder.mystory_time1.setText(story.getStory_time()+"");
        viewHolder.mystory_info.setText(story.getStory_info());
        viewHolder.mystory_comment.setText(story.getComment());
        viewHolder.mystory_readcount.setText(story.getReadcount());

        List<String> data_pics = story.getData_pics();
        if (null==data_pics||data_pics.size()==0 ){
            hang=1;
        }else if (data_pics.size()>=3){
            hang=3;
        }else {
            hang = data_pics.size();
        }
        viewHolder.mystory_recycle.setLayoutManager(new GridLayoutManager(context,hang));
        adapter2=new MyStory_adapter2(data_pics,context,2);
        viewHolder.mystory_recycle.setAdapter(adapter2);


        viewHolder.mystory_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareCallBack.GoBack(v,position,"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView mystory_time1,mystory_time2,mystory_info,mystory_comment,mystory_readcount;
        ImageView mystory_share;
        RecyclerView mystory_recycle;
        public ViewHolder(View view) {
            super(view);
            mystory_time1=(TextView) view.findViewById(R.id.mystory_time1);
            mystory_time2=(TextView) view.findViewById(R.id.mystory_time2);
            mystory_info=(TextView) view.findViewById(R.id.mystory_info);
            mystory_comment=(TextView) view.findViewById(R.id.mystory_comment);
            mystory_readcount=(TextView) view.findViewById(R.id.mystory_readcount);
            mystory_share=(ImageView)view. findViewById(R.id.mystory_share);
            mystory_recycle=(RecyclerView)view.findViewById(R.id.mystory_recycle);

        }
    }

}
