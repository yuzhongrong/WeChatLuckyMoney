package xyz.monkeytong.hongbao.activities;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import xyz.monkeytong.hongbao.R;
import xyz.monkeytong.hongbao.adapter.*;
import xyz.monkeytong.hongbao.adapter.BaseAdapter;
import xyz.monkeytong.hongbao.bean.Item;
import xyz.monkeytong.hongbao.fragments.GeneralSettingsFragment;
import xyz.monkeytong.hongbao.utils.ConnectivityUtil;
import xyz.monkeytong.hongbao.utils.UpdateTask;
import xyz.monkeytong.hongbao.widget.CircleImageView;

import com.tencent.bugly.crashreport.CrashReport;


public class MainActivity extends BaseActivity implements AccessibilityManager.AccessibilityStateChangeListener,View.OnClickListener,BaseAdapter.OnItemClickListener {

    //开关切换按钮

    private  CircleImageView iv;
    private TextView txt_state;
    private RecyclerView recyclerView;
    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;
    private List<Item> datas=new ArrayList<>();
    private RecyclerViewAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashReport.initCrashReport(getApplicationContext(), "900019352", false);
        setContentView(R.layout.activity_main);
        iv= (CircleImageView) findViewById(R.id.iv);
        txt_state= (TextView) findViewById(R.id.text_state);
        recyclerView= (RecyclerView) findViewById(R.id.frist_recyclerview);
        initRecycleView();
        iv.setBorderWidth(10);
        iv.setOnClickListener(this);
       // handleMaterialStatusBar();

        explicitlyLoadPreferences();

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
        updateServiceStatus();
    }


    private void initRecycleView(){
        datas.add(new Item(getString(R.string.settings), R.mipmap.settings));
        datas.add(new Item(getString(R.string.study), R.mipmap.jc));
        datas.add(new Item(getString(R.string.share),R.mipmap.share));


        viewAdapter=new RecyclerViewAdapter(this,datas);
        viewAdapter.setOnItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration());
        recyclerView.setAdapter(viewAdapter);



    }



    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.general_preferences, false);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffE46C62);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for update when WIFI is connected or on first time.
        if (ConnectivityUtil.isWifi(this) || UpdateTask.count == 0)
            new UpdateTask(this, false).update();
    }

    @Override
    protected void onDestroy() {
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
    }

    public void openAccessibility() {
        try {
            Toast.makeText(this, "点击「微信红包」", Toast.LENGTH_SHORT).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>微信红包(ฅ´ω`ฅ)", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void openGitHub() {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra("title", "GitHub 项目主页");
        webViewIntent.putExtra("url", "https://github.com/yuzhongrong/WeChatLuckyMoney/blob/master/README.md");
        startActivity(webViewIntent);
    }

    public void openUber() {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra("title", "Uber 优惠乘车机会(优惠码rgk2wue)");
        webViewIntent.putExtra("url", "https://get.uber.com.cn/invite/rgk2wue");
        startActivity(webViewIntent);
    }

    public void openSettings() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.putExtra("title", "偏好设置");
        settingsIntent.putExtra("frag_id", "GeneralSettingsFragment");
        startActivity(settingsIntent);
    }


    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            txt_state.setText(R.string.close_service);

        } else {
            txt_state.setText(R.string.start_service);
        }
    }

    /**
     * 获取 HongbaoService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.services.HongbaoService")) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.iv:

                openAccessibility();

                break;




        }


    }

    @Override
    public void onItemClick(View view, int position) {

        switch (position){
            case 0:
               openSettings();

                break;
            case 1:
                openUber();
                break;
            case 2:
                openGitHub();
                break;
            case 3:
                break;

        }



    }
}
