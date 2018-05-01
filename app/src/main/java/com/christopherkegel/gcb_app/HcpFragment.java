package com.christopherkegel.gcb_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christopherkegel on 23.04.15.
 */
public class HcpFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
//------------------------------------------------------------------------------------
// Variables -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    private View                rootView;
    private MainActivity        mainActivity;
    private Context             context;
    private SwipeRefreshLayout  swipeLayout;
    private String              url;
    private String              ausweis,service;
    private CustomDialog        mDialog;
    private CustomDialog2       mDialog2;
    private ListView            mCompleteListView;
    private View                listHeader;
    private TextView            lbl_Name_HCP2,lbl_Club_HCP2,lbl_HCP_HCP2;
    private LinearLayout      RelLayout;
    // Animation
            Animation           animSlideUp;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";
//------------------------------------------------------------------------------------
// Set Constructor -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public HcpFragment(){}
//------------------------------------------------------------------------------------
// Set onCreate ----------------------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //-----------------------------------------
        //-----------------------------------------
        mainActivity   = ((MainActivity)getActivity());
        context         = mainActivity;
        //-----------------------------------------
        //-----------------------------------------
        rootView = inflater.inflate(R.layout.fragment_hcp2, container, false);
        RelLayout   =(LinearLayout) rootView.findViewById(R.id.HCPCard);
        //-----------------------------------------
        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(context, R.anim.expand);
        //-----------------------------------------
        //listHeader          = mainActivity.getLayoutInflater().inflate(R.layout.list_header, null);
        //mCompleteListView   = (ListView) rootView.findViewById(R.id.listViewHCP);
        //-----------------------------------------
        lbl_Name_HCP2   = (TextView) rootView.findViewById(R.id.lbl_Name_HCP2);
        lbl_Club_HCP2   = (TextView) rootView.findViewById(R.id.lbl_Club_HCP2);
        lbl_HCP_HCP2    = (TextView) rootView.findViewById(R.id.lbl_HCP_HCP2);
        //-----------------------------------------
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_light); //holo_orange_light
        //-----------------------------------------
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        ausweis = SP.getString("ausweis_nr", null);
        service = SP.getString("service_nr", null);
        url = "https://www.pccaddie.net/clubs/0497807/turnier.php?gebdat="+service+"&action=check_login&dgvausweisnummer="+ausweis;
        //-----------------------------------------
        if(ausweis == null  || service == null ){
            //mDialog2 = new CustomDialog2(context);
            mainActivity.showDialog2();
        }
        else{
            if(isNetworkAvailable() == true) {
                new Title2(mainActivity).execute();
            }
            else{
                mainActivity.showDialog();
                //mDialog = new CustomDialog(context);
            }
        }
        //-----------------------------------------
        //-----------------------------------------
        return rootView;
    }
//------------------------------------------------------------------------------------
// Sonstiges -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public View getView(){
        return rootView;
    }
//------------------------------------------------------------------------------------
// Set onRefresh ---------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public void onRefresh() {
                if(isNetworkAvailable() == true) {
                    //swipeLayout.setRefreshing(true);
                    new Title2(mainActivity).execute();
                }
                else{
                    //mDialog = new CustomDialog(context);
                    mainActivity.showDialog();
                }
    }
//------------------------------------------------------------------------------------
// Set if NetworkAvailable -----------------------------------------------------------
//------------------------------------------------------------------------------------
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
//--------------------------------------------------------------------------------------
// AsyncTask ---------------------------------------------------------------------------
//--------------------------------------------------------------------------------------

    // Title AsyncTask
    private class Title2 extends AsyncTask<Void, Void, Void> {
        Elements HCP;
        String[] header;
        String[] infos;
        MainActivity mainActivtiy;

        public Title2(MainActivity mainActivtiy1){
            mainActivtiy = mainActivtiy1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeLayout.setProgressViewOffset(false, 0,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(url).get();
                // Get the html document title
                HCP = document.select("table");
            } catch (IOException e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        //------------------------------------------
            int length = 0;
            String txt = null;
            Pattern pattern;
            Matcher matcher;

            if(HCP != null){
                length  =   HCP.size();
                header  =   new String[3];
                infos   =   new String[3];
            }else{
                length = -1;
            }
            //------------------------------------------
            for (int i = 0; i < length; i++) {
                //------------------------------------------
                txt = HCP.subList(i, i + 1).toString();
                pattern = Pattern.compile("als: <b> (.*?)</b>");
                matcher = pattern.matcher(txt);
                while (matcher.find()) {
                    txt = matcher.group(1);
                    header[0]   = "Angemeldet als";
                    infos[0]    = txt;
                }

                //------------------------------------------
                txt = HCP.subList(i, i + 1).toString();
                pattern = Pattern.compile("Heimatclub: (.*?)\\(");
                matcher = pattern.matcher(txt);
                while (matcher.find()) {
                    txt = matcher.group(1);
                    header[1]   = "Heimatclub";
                    infos[1]    = txt;
                }
                //------------------------------------------
                txt = HCP.subList(i, i + 1).toString();
                pattern = Pattern.compile("HCP: (.*?)\\(Stand:");
                matcher = pattern.matcher(txt);
                while (matcher.find()) {
                    txt = matcher.group(1);
                    header[2]   = "HCP";
                    infos[2]    = txt;
                }
                //------------------------------------------
            }
            //mCompleteListView.setAdapter(new LA_Icon2Rows(mainActivtiy, context,infos, header));
            //mCompleteListView.removeHeaderView(listHeader);
            //mCompleteListView.addHeaderView(listHeader);
            lbl_Name_HCP2.setText(infos[0]);
            lbl_Club_HCP2.setText(infos[1]);
            lbl_HCP_HCP2.setText(infos[2]);
            //
            RelLayout.setVisibility(View.VISIBLE);
            RelLayout.startAnimation(animSlideUp);
            /*
            lbl_Name_HCP2.startAnimation(animSlideUp);
            lbl_Club_HCP2.startAnimation(animSlideUp);
            lbl_HCP_HCP2.startAnimation(animSlideUp); */
            //
            swipeLayout.setRefreshing(false);
        }
    }
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------
}
