package qf.com.my_story.utils;

import android.view.View;

import qf.com.my_story.bean.GetStory;

/**
 * Created by linke on 2017/2/9 0009.
 * email:linke0530@163.com.
 */

public interface ShareCallBack {
    public  void  GoBack (View view ,int postion,String a);
    public  void  JsonBack (View view,GetStory data) ;


}
