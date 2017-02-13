package qf.com.my_story.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import qf.com.my_story.R;
import qf.com.my_story.adapter.ViewPagerAdapter;
import qf.com.my_story.fragment.Fragment1;
import qf.com.my_story.fragment.Fragment2;
import qf.com.my_story.utils.Urls;

public class FirstActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    private Toolbar toolbar;//这是首页的toolbar
    private ViewPager viewPager;//用于首页滑动fragment的viewpager
    private  FloatingActionButton  fab;//粉色邮箱
    private  DrawerLayout   drawer ;//侧滑布局
    private NavigationView navigationView;//这个控件是侧滑菜单的下面布局
    private List<Fragment>fragment_data=new ArrayList<>();//碎片的数据
    private ViewPagerAdapter adapter;//这个是碎片的适配器
    private FragmentManager manager;//碎片管理器
    private Button button_zuixin,button_zuire;//toolbar上的两个按键
    private TextView username,nickname;
    private ImageView first_imageView; //这个是头像
    private  String sfromLogin; //这个是登录页面带过来的信息
    private  String uid,uid2; //这个是从其他页面带过来的id
    private ImageView edit_img;//编辑按钮
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindById();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//去掉标题文字
        //初始化fragment数据
        initFragmentList();
        //获取管理器对象
        manager=getSupportFragmentManager();
        adapter=new ViewPagerAdapter(manager,fragment_data);
        viewPager.setAdapter(adapter);//绑定适配器
        viewPager.addOnPageChangeListener(this);

        //邮箱的点击事件
        touchEmail();
        //设置监听
        SetListener();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        DealFromLogin();

    }
    //设置侧滑布局的监听
    private void SetListener() {
        first_imageView.setOnClickListener(this);
        edit_img.setOnClickListener(this);
    }

    //找控件
    private void FindById() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.first_viewpager);//找到viewpager
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        button_zuixin = (Button) findViewById(R.id.zuixin);
        button_zuire = (Button) findViewById(R.id.zuire);
        View headerView = navigationView.getHeaderView(0);
        username=(TextView)headerView.findViewById(R.id.first_username);
        nickname=(TextView)headerView.findViewById(R.id.first_nickname);
        first_imageView=(ImageView)headerView.findViewById(R.id.first_imageView1);
        edit_img = (ImageView) findViewById(R.id.first_editimg);
    }

    //处理来自登录页面的信息
    private void DealFromLogin() {
        Intent intent=getIntent();
        sfromLogin = intent.getStringExtra("s");
        String from_centername = intent.getStringExtra("from_centername");
        sp =getSharedPreferences("file_info", Context.MODE_PRIVATE);
        String pic_path = sp.getString("portrait", "");
        if (null!=from_centername){
            nickname.setText(from_centername);
        }if (null!=pic_path){
            Picasso.with(FirstActivity.this)
                    .load(Urls.headImageUrl+pic_path)
                    .fit()
                    .placeholder(R.drawable.jiazai)
                    .error(R.drawable.product_default)
                    .into(first_imageView);
        }

        if (null!=sfromLogin){
        try {
            JSONObject object=new JSONObject(sfromLogin);
            JSONObject object1 = object.optJSONObject("data");
            uid = object1.getString("username");
             uid2 = object1.getString("id");
            String nick_name = object1.getString("nickname");
            String portrait = object1.getString("portrait");
            username.setText(this.uid);
            nickname.setText(nick_name);
            Picasso.with(FirstActivity.this)
                    .load(Urls.headImageUrl+portrait)
                    .fit()
                    .placeholder(R.drawable.jiazai)
                    .error(R.drawable.product_default)
                    .into(first_imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }}
    }

    //初始化viewpager数据
    private void initFragmentList() {
        fragment_data.add(new Fragment1());
        fragment_data.add(new Fragment2());
    }
    //处理邮件图标
    private void touchEmail() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });
    }
    //整个布局控件
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_newtalk) {
          startActivity(new Intent(FirstActivity.this,StoryDetailsActivity.class));
        } else if (id == R.id.nav_newhistory) {
            startActivity(new Intent(FirstActivity.this,NewStoryActivity.class));
        } else if (id == R.id.nav_mystory) {
           Intent intent=new Intent(new Intent(FirstActivity.this,MyStoryActivity.class));
            intent.putExtra("toMyStory",uid2);
            startActivity(intent);

        } else if (id == R.id.nav_message) {
            toMyCenter();

        }else if (id == R.id.nav_setting) {
            startActivity(new Intent(FirstActivity.this,SettingActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //toolbar上面btn的点击事件1
    public  void first_btn1(View view){
        viewPager.setCurrentItem(0);
        btn1Changed();
    }
    //最新按键被选中
    private void btn1Changed() {
        button_zuixin.setBackground(null);
        button_zuire.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }
    //toolbar上面btn的点击事件2
    public  void first_btn2(View view){
       viewPager.setCurrentItem(1);
        btn2Changed();
    }
    //最热按键被选中
    private void btn2Changed() {
        button_zuire.setBackground(null);
        button_zuixin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }


    /***
     * 以下3个方法是viewPaGer变化的监听
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
      if(position==0){
          btn1Changed();
      }else {
          btn2Changed();
      }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
   //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first_imageView1:
                toMyCenter();
                break;
            case  R.id.first_editimg:
                Intent intent=new Intent(FirstActivity.this,NewStoryActivity.class);
                startActivity(intent);
                break;
        }
    }
    //跳转动作  到个人中心
    private void toMyCenter() {
        Intent intent=new Intent(FirstActivity.this,MyCenterActivity.class);
        intent.putExtra("keyfromfirst",sfromLogin);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
             finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
