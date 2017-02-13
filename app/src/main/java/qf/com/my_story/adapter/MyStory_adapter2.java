package qf.com.my_story.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import qf.com.my_story.R;
import qf.com.my_story.utils.Urls;

/**
 * Created by linke on 2017/2/9 0009.
 * email:linke0530@163.com.
 */

public class MyStory_adapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String>data;
    Context context;
    int a;

    public MyStory_adapter2(List<String> data, Context context,int a) {
        this.data = data;
        this.context = context;
        this.a=a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (a==1){
            View view=  LayoutInflater.from(context).inflate(R.layout.img1,null,false);
            return new ViewHolder2(view);
        } else if (a==9){
            View view=  LayoutInflater.from(context).inflate(R.layout.img9,null,false);
            return new ViewHolder2(view);
        }else {
            View view=  LayoutInflater.from(context).inflate(R.layout.img,null,false);
            return new ViewHolder2(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder2 holder2 = (ViewHolder2) holder;
        String s =Urls.imageUrl+ data.get(position);
        Log.i("info", "onBindViewHolder:============================ "+s);

        Picasso.with(context).load(s)
                .fit()
                .placeholder(R.drawable.jiazai)
                .error(R.drawable.product_default)
                .into(holder2.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == data) {
            return 0;
        } else {
            return data.size();
        }
    }
    class  ViewHolder2 extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder2(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.img);
        }
    }

}
