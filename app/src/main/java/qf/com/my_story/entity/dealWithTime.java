package qf.com.my_story.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linke on 2017/2/10 0010.
 * email:linke0530@163.com.
 */

public class dealWithTime {
    /**
     * 秒数转化为日期
     * */
    public  String getDateFromSeconds(long seconds){
        if(seconds==0)
            return "";
        else{
            Date date=new Date();
            try{
                date.setTime(seconds*1000);
            }catch(Exception nfe){

            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
    }
}
