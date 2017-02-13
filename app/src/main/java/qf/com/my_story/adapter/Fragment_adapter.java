package qf.com.my_story.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qf.com.my_story.R;
import qf.com.my_story.bean.GetStory;
import qf.com.my_story.entity.dealWithTime;
import qf.com.my_story.utils.ShareCallBack;
import qf.com.my_story.utils.ShareCallBack2;
import qf.com.my_story.utils.Urls;

/**
 * Created by linke on 2017/2/6 0006.
 * email:linke0530@163.com.
 */

public class Fragment_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<GetStory> data;
    Context context;
    MyStory_adapter2 adapter2;
    ShareCallBack shareCallBack;
    ShareCallBack2 shareCallBack2;

    qf.com.my_story.entity.dealWithTime dealWithTime ;
    int hang=1;
    public void  setShareCallBack(ShareCallBack shareCallBack){
        this.shareCallBack=shareCallBack;
    }
    public void  setShareCallBack2(ShareCallBack2 shareCallBack2){
        this.shareCallBack2=shareCallBack2;
    }


    public Fragment_adapter(List<GetStory> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

           View view= LayoutInflater.from(context).inflate(R.layout.frist_item,null,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder=(ViewHolder)holder;
        final GetStory story=data.get(position);
        viewHolder.uid.setText(story.getUsername());
        viewHolder.comment.setText(story.getComment());
        viewHolder.info.setText(story.getStory_info());
        viewHolder.readcount.setText(story.getReadcount());
        viewHolder.city.setText(story.getCity());
        long story_time = story.getStory_time();

          dealWithTime=new dealWithTime();
        String dateFromSeconds = dealWithTime.getDateFromSeconds(story_time);
        if (null!=dateFromSeconds){
        viewHolder.time.setText(dateFromSeconds);}

        List<String> data_pics = story.getData_pics();
        if (null==data_pics||data_pics.size()==0 ){
            hang=1;
        }else if (data_pics.size()>=3){
            hang=3;
        }else {
            hang = data_pics.size();
        }

        viewHolder.recyclerView.setLayoutManager(new GridLayoutManager(context,hang));
        adapter2=new MyStory_adapter2(data_pics,context,1);
        viewHolder.recyclerView.setAdapter(adapter2);
       viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCallBack.GoBack(v,position,story.getUid());
            }
        });


        //readcount 的监听
        viewHolder.readcount_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCallBack.GoBack(v,position,story.getUid());

            }
        });
        //comment 的监听
        viewHolder.comment_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCallBack.JsonBack(v,story);
            }
        });
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCallBack.JsonBack(v,story);
            }
        });





        String pics_head = Urls.headImageUrl + story.getPortrait();

        Picasso.with(context).load(pics_head)
                .fit()
                .placeholder(R.drawable.jiazai)
                .error(R.drawable.product_default)
                .into(viewHolder.headImg);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public  class  ViewHolder extends RecyclerView.ViewHolder{
        private final LinearLayout linearLayout;
        ImageView headImg,share,comment_img,readcount_img;
        RecyclerView recyclerView;
        TextView  uid,city,time,info,readcount,comment;

        public ViewHolder(View itemView) {
            super(itemView);
            headImg=(ImageView)itemView.findViewById(R.id.fragment1_headimg);
            share=(ImageView)itemView.findViewById(R.id.share);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.fragment1_pic);
            uid=(TextView)itemView.findViewById(R.id.fragment1_uid);
            city=(TextView)itemView.findViewById(R.id.fragment1_city);
            time=(TextView)itemView.findViewById(R.id.fragment1_time);
            info=(TextView)itemView.findViewById(R.id.fragment1_info);
            comment_img=(ImageView) itemView.findViewById(R.id.comment);
            readcount_img=(ImageView) itemView.findViewById(R.id.readcount);
            readcount=(TextView)itemView.findViewById(R.id.fragment1_readcount);
            comment=(TextView)itemView.findViewById(R.id.fragment1_comment);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.frist_layout);
        }

    }
 }
