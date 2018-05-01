package com.christopherkegel.gcb_app;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener, DrawerLayout.DrawerListener{
//------------------------------------------------------------------------------------
// Variables -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    private MainActivity            ma;
    private Context                 context,con;
    private WetterFragment          wf;
    private InfoFragment            infoF;
    private HcpFragment             hcpFragment;
    private SettingsFragment        settingsFragment;
    private ImpressumFragment       impressumFragment;
    private HelpFragment            helpFragment;
    private StartListeFragment      startListeFragment;
    private ErgebnisListeFragment   ergebnisListeFragment;
    private DrawerLayout            drawer;
    private ListView                mDrawerList;
    private List<DrawerItem>        dataList = new ArrayList<DrawerItem>();
    private CustomDrawerAdapter     adapter;
    private View                    footer,iv;
    private TextView                lbl_dw_s, lbl_dw_i, lbl_dw_h;
    private FragmentManager         fragmentManager = null;
    private int                     zaehler = 0;
    private Toolbar toolbar;
    private ShowcaseView            sv;
    private Target                  t1,t2;
    private DisplayMetrics          metrics;
    private static long             back_pressed;
    private TextView                tv;
    private boolean                 isFirstRun;
    private SharedPreferences       wmbPreference;
    private boolean                 drawerOpened;
    private Bundle                  bundle;
//------------------------------------------------------------------------------------
// Set onCreate ----------------------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-----------------------------------------
        super.onCreate(savedInstanceState);
        setActionBarIcon(R.drawable.ic_ab_drawer);
        //-----------------------------------------
        //Initialice Variables
            ma              = this;
            context         = this;
            drawerOpened    = false;
        //Layout
            toolbar         = (Toolbar) findViewById(R.id.toolbar);
            drawer          = (DrawerLayout) findViewById(R.id.drawer);
            mDrawerList     = (ListView) findViewById(R.id.left_drawer_list);
            footer          = getLayoutInflater().inflate(R.layout.custom_drawer_footer, null);
        //Help
            wmbPreference   = PreferenceManager.getDefaultSharedPreferences(this);
            isFirstRun      = wmbPreference.getBoolean("FIRSTRUN", true);
            fragmentManager = getSupportFragmentManager();
        // Bundle
            /*
            bundle          = new Bundle();
            bundle.putSerializable("MA", (Serializable) ma);
            bundle.putSerializable("CO", (Serializable) context);
            */
        //-----------------------------------------
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }
        //-----------------------------------------
        //-----------------------------------------
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //Do things.
                InfoFragment infoF = new InfoFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, infoF).commit();

            }
        }).start();
        //-----------------------------------------
        //-----------------------------------------
        if (isFirstRun) {
            // List of all mToolbar items, assuming last is overflow
            List<View> views = toolbar.getTouchables();
            t1 = new ViewTarget(views.get(0)); // Menu
            t2 = new ViewTarget(R.id.frame_container, this);
            //-----------------------------------------
            sv = new ShowcaseView.Builder(this)
                    .setTarget(Target.NONE)
                    .setOnClickListener(this)
                    .setContentTitle(getResources().getString(R.string.sv_h1))
                    .setContentText(getResources().getString(R.string.sv_txt1))
                    .build();
        }
        //-----------------------------------------
        //-----------------------------------------
        drawer.setDrawerListener(this);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        //-----------------------------------------
        lbl_dw_s    = (TextView) footer.findViewById(R.id.dw_settings);
        lbl_dw_i    = (TextView) footer.findViewById(R.id.dw_impressum);
        lbl_dw_h    = (TextView) footer.findViewById(R.id.dw_help);
        //-----------------------------------------
        //-----------------------------------------
        lbl_dw_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDrawerItem2(0);
            }
        });
        lbl_dw_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDrawerItem2(1);
            }
        });
        lbl_dw_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDrawerItem2(2);
            }
        });
        //-----------------------------------------
        //-----------------------------------------
        // Add Drawer Item to dataList
        dataList.add(new DrawerItem("Platzinfo", R.drawable.calendar));
        dataList.add(new DrawerItem("Wetter", R.drawable.wetter));
        dataList.add(new DrawerItem("HCP Abfrage", R.drawable.scorecard_g));
        dataList.add(new DrawerItem("Startlisten", R.drawable.scorecard_g));
        dataList.add(new DrawerItem("Ergebnislisten", R.drawable.scorecard_g));
        //-----------------------------------------
        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item,dataList);
        mDrawerList.setAdapter(adapter);
        mDrawerList.addFooterView(footer);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        //-----------------------------------------
    }
//------------------------------------------------------------------------------------
// If firstpart of DrawerList is selected --------------------------------------------
//------------------------------------------------------------------------------------
    public void SelectItem(int position2) {
    switch (position2) {
        case 0:
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //Do things.
                    InfoFragment infoF = new InfoFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, infoF).commit();
                }
            }).start();
            setDrawerItem2BG(3,0);
            break;
        case 1:
                    //Do things.
                    //WetterFragment wf2 = new WetterFragment();
                    //fragmentManager.beginTransaction().replace(R.id.frame_container, wf2).commit();
                    Toast.makeText(this," Wetter ist momentan nicht verfügbar.",  Toast.LENGTH_LONG).show();
            setDrawerItem2BG(4, 0);
            break;
        case 2:
            new Thread(new Runnable()
            { 
                @Override
                public void run()
                {
                    //Do things.
                    hcpFragment = new HcpFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, hcpFragment).commit();
                }
            }).start();
            setDrawerItem2BG(5,0);
            break;
        case 3:
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //Do things.
                    startListeFragment = new StartListeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, startListeFragment).commit();
                }
            }).start();
            setDrawerItem2BG(6,0);
            break;
        case 4:
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    //Do things.
                    ergebnisListeFragment = new ErgebnisListeFragment();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, ergebnisListeFragment).commit();
                }
            }).start();
            setDrawerItem2BG(7,0);
            break;
        default:
            break;
    }
    drawer.closeDrawer(Gravity.START);
}
//------------------------------------------------------------------------------------
// If Second part of Drawer is selected ----------------------------------------------
//------------------------------------------------------------------------------------
    public void clickDrawerItem2(int i){
        switch(i) {
            case 0:
                settingsFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, settingsFragment).commit();
                setDrawerItem2BG(0, 0);
                drawer.closeDrawer(Gravity.START);
                break;
            case 1:
                impressumFragment = new ImpressumFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, impressumFragment).commit();
                drawer.closeDrawer(Gravity.START);
                setDrawerItem2BG(1, 0);
                drawer.closeDrawer(Gravity.START);
                break;
            case 2:
                helpFragment = new HelpFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, helpFragment).commit();
                drawer.closeDrawer(Gravity.START);
                setDrawerItem2BG(2, 0);
                drawer.closeDrawer(Gravity.START);
                break;
        }
    }
//------------------------------------------------------------------------------------
// Set DrawerItem Backgrounds --------------------------------------------------------
//------------------------------------------------------------------------------------
    public void setDrawerItem2BG(int i,int state){

        lbl_dw_s.setTextColor(getResources().getColor(R.color.txt_drawer));
        lbl_dw_i.setTextColor(getResources().getColor(R.color.txt_drawer));
        lbl_dw_h.setTextColor(getResources().getColor(R.color.txt_drawer));

        for(int b = 0;b<5;b++ ){
            iv = mDrawerList.getChildAt(b);
            tv = (TextView) iv.findViewById(R.id.drawer_itemName);
            mDrawerList.setItemChecked(b, false);
            iv.setBackgroundColor(getResources().getColor(R.color.txt_white));
            tv.setTextColor(getResources().getColor(R.color.txt_drawer));
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        ImageView imagex = null;

        switch(i) {
            case 0:
                if (state == 0) {
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);
                    lbl_dw_s.setTextColor(getResources().getColor(R.color.colorAccent));
                }break;
            case 1:
                if (state == 0) {
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);
                    lbl_dw_i.setTextColor(getResources().getColor(R.color.colorAccent));
                }break;
            case 2:
                if (state == 0) {
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);
                    lbl_dw_h.setTextColor(getResources().getColor(R.color.colorAccent));
                }break;
            case 3:
                if (state == 0){
                    iv = mDrawerList.getChildAt(0);
                    imagex = (ImageView) iv.findViewById(R.id.item_icon);
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);

                    iv.setBackgroundColor(getResources().getColor(R.color.txt_drawer_bg));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    map.put("calendar", R.drawable.temperatur);
                    imagex.setImageResource(map.get("calendar"));
                }break;
            case 4:
                if (state == 0) {
                    iv = mDrawerList.getChildAt(1);
                    imagex = (ImageView) iv.findViewById(R.id.item_icon);
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);

                    iv.setBackgroundColor(getResources().getColor(R.color.txt_drawer_bg));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    map.put("calendar", R.drawable.temperatur);
                    imagex.setImageResource(map.get("calendar"));
                }break;
            case 5:
                if(state == 0) {
                    iv = mDrawerList.getChildAt(2);
                    imagex = (ImageView) iv.findViewById(R.id.item_icon);
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);

                    iv.setBackgroundColor(getResources().getColor(R.color.txt_drawer_bg));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    map.put("calendar", R.drawable.temperatur);
                    imagex.setImageResource(map.get("calendar"));
                }break;
            case 6:
                if(state == 0) {
                    iv = mDrawerList.getChildAt(3);
                    imagex = (ImageView) iv.findViewById(R.id.item_icon);
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);

                    iv.setBackgroundColor(getResources().getColor(R.color.txt_drawer_bg));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    map.put("calendar", R.drawable.temperatur);
                    imagex.setImageResource(map.get("calendar"));
                }break;
            case 7:
                if(state == 0) {
                    iv = mDrawerList.getChildAt(4);
                    imagex = (ImageView) iv.findViewById(R.id.item_icon);
                    tv = (TextView) iv.findViewById(R.id.drawer_itemName);

                    iv.setBackgroundColor(getResources().getColor(R.color.txt_drawer_bg));
                    tv.setTextColor(getResources().getColor(R.color.colorAccent));
                    map.put("calendar", R.drawable.temperatur);
                    imagex.setImageResource(map.get("calendar"));
                }break;
        }
    }
//------------------------------------------------------------------------------------
// If Drawer opens -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public void onDrawerOpened(View drawerView) {
        if (isFirstRun) {
            if(drawerOpened == false) {
                //-----------------------------------------
                t1 = new ViewTarget(R.id.left_drawer_list, this);// Menu
                //-----------------------------------------
                sv = new ShowcaseView.Builder(this)
                        .setTarget(t1)
                        .setContentTitle(getResources().getString(R.string.sv_drawer_h1))
                        .setContentText(getResources().getString(R.string.sv_drawer_txt3))
                        .build();
                sv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sv.hide();
                    }
                });
                //-----------------------------------------
                drawerOpened = true;
            }
        }
    }
//------------------------------------------------------------------------------------
// If Settings Options is selected ---------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager3 = getSupportFragmentManager();
            settingsFragment = new SettingsFragment();
            fragmentManager3.beginTransaction().replace(R.id.frame_container, settingsFragment).commit();
            return true;
        }
        if (id == android.R.id.home){
                drawer.openDrawer(Gravity.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//------------------------------------------------------------------------------------
// If Backbutton is pressed ----------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if(back_pressed + 2000 >  System.currentTimeMillis())super.onBackPressed();
        else Toast.makeText(getBaseContext(),"Press again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
//------------------------------------------------------------------------------------
// ShowcaseView Buttonclick Switcher -------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch(zaehler){
            case 0: sv.setShowcase(t1, true);
                sv.setContentTitle(getResources().getString(R.string.sv_h2) );
                sv.setContentText(getResources().getString(R.string.sv_txt2));
                break;
            case 1: sv.setShowcase(t2, true);
                sv.setContentTitle(getResources().getString(R.string.sv_h3));
                sv.setContentText(getResources().getString(R.string.sv_txt3));
                sv.hide();
                break;
            case 2: sv.hide();
                break;
        }
        zaehler++;
    }
//------------------------------------------------------------------------------------
// Everything Else -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public MainActivity getMainActivity(){
        return ma;
    }
    //-----------------------------------------
    //-----------------------------------------
    public Context getMAContext(){
        return context;
    }
    //-----------------------------------------
    //-----------------------------------------
    public void showDialog() {
        new CustomDialog(context);
    }
    public void showDialog2() {
        new CustomDialog2(context);
    }
    //-----------------------------------------
    //-----------------------------------------

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            SelectItem(position);
        }
    }
    //-----------------------------------------
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }
    //--------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
}
