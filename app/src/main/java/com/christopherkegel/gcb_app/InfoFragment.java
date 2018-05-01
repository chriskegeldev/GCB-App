package com.christopherkegel.gcb_app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christopherkegel on 23.12.14.
 */
public class InfoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
//------------------------------------------------------------------------------------
// Variables -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    private View                rootView;
    private MainActivity        mainActivity;
    private Context             context;
    private SwipeRefreshLayout  swipeLayout;
    private ProgressBar         spinner3;
    private String              url = "http://www.golfclub-badrappenau.de/";
    private CustomDialog        mDialog;
    private View                header;
    private ListView            mCompleteListView;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";
    // Animation
            Animation           animSlideUp;
//------------------------------------------------------------------------------------
// Set Constructor -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public InfoFragment(){
        /*        this.mainActivity   = (MainActivity) getArguments().getSerializable(
                ARG_MAIN_ACTIVTIY);
        //mainActivity   = ((MainActivity)getActivity());
        context = mainActivity.getApplicationContext();
        //mainActivity    = mainActivity1.getMainActivity();
        //context         = mainActivity.getMAContext();
        //context = mainActivity.getApplicationContext();*/
    }
    /*
    public static InfoFragment newInstance(MainActivity mainActivity1) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MAIN_ACTIVTIY, (Serializable) mainActivity1);
        fragment.setArguments(bundle);

        return fragment;
    }*/
//------------------------------------------------------------------------------------
// Sonstiges -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public View getView(){
        return rootView;
    }
//------------------------------------------------------------------------------------
// Set onCreateView ------------------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        //-----------------------------------------
        //-----------------------------------------
        //this.mainActivity   = (MainActivity) getArguments().getSerializable(ARG_MAIN_ACTIVTIY);
        //this.context = (Context) getArguments().getSerializable(ARG_Context);
        mainActivity   = ((MainActivity)getActivity());
        context         = mainActivity;
        //-----------------------------------------
        //-----------------------------------------
        rootView = inflater.inflate(R.layout.fragment_infos, container, false);
        //-----------------------------------------
        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        //-----------------------------------------
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_light); //holo_orange_light
        //-----------------------------------------
        //-----------------------------------------
        //header            = mainActivity.getLayoutInflater().inflate(R.layout.list_header, null);
        mCompleteListView   = (ListView) rootView.findViewById(R.id.listView);
        //-----------------------------------------
        //-----------------------------------------
        if(isNetworkAvailable() == true) {
            new Title(mainActivity).execute();
        }
        else{
            //mDialog = new CustomDialog(context);
            //mainActivity.showDialog();
        }
        //-----------------------------------------
        return rootView;
    }
//------------------------------------------------------------------------------------
// Set onRefresh ---------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public void onRefresh() {
        if(isNetworkAvailable() == true) {
            swipeLayout.setRefreshing(true);
            new Title(mainActivity).execute();
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
    private class Title extends AsyncTask<Void, Void, Void> {
        Elements days;
        String[] tage;
        String[] daten;
        String[] messages;
        String[] status;
        MainActivity mainActivtiy;

        public Title(MainActivity mainActivtiy1){
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
                days = document.select("p.offen, p.geschlossen");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //------------------------------------------
            int length = 0;
            if(days != null){
                length = days.size();
                tage        = new String[length];
                daten       = new String[length];
                messages    = new String[length];
                status      = new String[length];
            }else{
                length = -1;
            }
            //------------------------------------------
            for (int i = 0; i < length; i++) {
                //------------------------------------------
                String txt = null;
                String txt_klasse = null;
                String txt_datum_tag = null;
                String txt_tag = null;
                String txt_datum = null;
                String txt_message = null;
                boolean flag = false;
                //------------------------------------------
                txt = days.subList(i, i + 1).toString();
                //------------------------------------------
                txt_message = txt.replaceAll("\\[", "");
                txt_message = txt_message.replaceAll("\\]", "");
                //-------------------------------------------
                // Klasse auslesen --------------------------
                //-------------------------------------------
                if (txt.contains("offen")) {
                    txt_klasse = "Offen";
                }
                if (txt.contains("geschlossen")) {
                    txt_klasse = "Geschlossen";
                }
                //-------------------------------------------
                // Datum & Tag auslesen ---------------------
                //-------------------------------------------
                txt_datum_tag = txt.replace(" ", "");
                txt_datum = txt_datum_tag.replace(" ", "");
                //-------------------------------------------
                Pattern pattern = Pattern.compile("<b>(.*?)</b><br>");
                Matcher matcher = pattern.matcher(txt);
                while (matcher.find()) {
                    txt_datum_tag = matcher.group(1);
                }
                //-------------------------------------------
                if (txt_datum_tag.contains("Montag")) {
                    txt_tag = "Mo.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Dienstag")) {
                    txt_tag = "Di.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Mittwoch")) {
                    txt_tag = "Mi.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Donnerstag")) {
                    txt_tag = "Do.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Freitag")) {
                    txt_tag = "Fr.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Samstag")) {
                    txt_tag = "Sa.";
                    flag = true;
                }
                //-------------------
                if (txt_datum_tag.contains("Sonntag")) {
                    txt_tag = "So.";
                    flag = true;
                }
                //-------------------------------------------
                //-------------------------------------------
                if(flag == true){
                    //---------------------------------------
                    pattern = Pattern.compile(",(.*?)\\.");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt_datum = matcher.group(1);
                    }
                    txt_datum = txt_datum.replace(" ", "");
                    //---------------------------------------
                    pattern = Pattern.compile("<br>(.*?)</p>");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt_message = matcher.group(1);
                    }
                    //---------------------------------------
                    tage[i] = txt_datum;
                    daten[i] = txt_tag;
                    messages[i] = txt_klasse.replaceAll("\\[", "");
                    status[i] = txt_message;
                }
                //-------------------------------------------
                //-------------------------------------------
                if(flag == false){
                    //-----------------------
                    pattern = Pattern.compile("<b>(.*?)</b>");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt_klasse = matcher.group(1);
                    }
                    //-----------------------
                    pattern = Pattern.compile("<br>(.*?)</p>");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt_message = matcher.group(1);
                    }
                    //-----------------------
                    status[i]   = txt_message;
                    messages[i] = txt_klasse;
                    tage[i]     = "";
                    daten[i]    = "";
                }
                //-------------------------------------------
            }
            mCompleteListView.setAdapter(new LA_Simple(mainActivtiy, context, tage, daten, messages, status));
            //mCompleteListView.removeHeaderView(header);
            //mCompleteListView.addHeaderView(header);
            //spinner3.setVisibility(View.GONE);
            swipeLayout.setRefreshing(false);
            mCompleteListView.startAnimation(animSlideUp);
        }
    }
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------

}