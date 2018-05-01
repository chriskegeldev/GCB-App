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
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by christopherkegel on 23.04.15.
 */
public class StartListeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
//------------------------------------------------------------------------------------
// Variables -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    private View                rootView;
    private MainActivity        mainActivity;
    private Context             context;
    private SwipeRefreshLayout  swipeLayout;
    private String              url;
    private String              ausweis,service;
    private CustomDialog        inetDialog;
    private CustomDialog2       mDialog2;
    private ListView            mCompleteListView;
    private View                listHeader;
    // Animation
            Animation           animSlideUp;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";
//------------------------------------------------------------------------------------
// Set Constructor -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public StartListeFragment(){
    }
//------------------------------------------------------------------------------------
// Sonstiges -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public View getView(){
        return rootView;
    }
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
        //------------------------------------------
        rootView = inflater.inflate(R.layout.fragment_startliste, container, false);
        //-----------------------------------------
        //listHeader        = mainActivity.getLayoutInflater().inflate(R.layout.list_header, null);
        mCompleteListView   = (ListView) rootView.findViewById(R.id.listViewStartliste);
        //-----------------------------------------
        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        //-----------------------------------------
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_light); //holo_orange_light
        //-----------------------------------------
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        ausweis = SP.getString("ausweis_nr", null);
        service = SP.getString("service_nr", null);
        url = "http://gcb-guide.christopherkegel.com/Parser/startliste_parser.php?";
               // "ausweis="+ausweis+"&service="+service;
        //https://www.pccaddie.net/clubs/0497746/turnier.php?action=show_startlist
        //-----------------------------------------
        if(ausweis == null  || service == null ){
            //mDialog2 = new CustomDialog2(context);
            mainActivity.showDialog2();
        }
        else{
            if(isNetworkAvailable() == true) {
                new Turniere(mainActivity).execute();
            }
            else{
                //inetDialog = new CustomDialog(context);
                mainActivity.showDialog();
            }
        }
        //-----------------------------------------
        //-----------------------------------------
        return rootView;
    }
//------------------------------------------------------------------------------------
// Set onRefresh ---------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public void onRefresh() {
                if(isNetworkAvailable() == true) {
                    new Turniere(mainActivity).execute();
                }
                else{
                    //inetDialog = new CustomDialog(context);
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
    private class Turniere extends AsyncTask<Void, Void, Void> {
        Elements Turniere;
        String[] date, infos, turnier, turnier_id;
        MainActivity mainActivtiy;

        public Turniere(MainActivity mainActivtiy1){
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
                Turniere = document.select("tr.tupel");
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

            if(Turniere != null){
                length      =   Turniere.size();
                date        =   new String[length];
                turnier     =   new String[length];
                turnier_id  =   new String[length];
            }else{
                length = -1;
            }
            //Log.i("Listeneintrag: ",Turniere.toString()+" Länge: "+Integer.toString(length));
            //------------------------------------------
            for (int i = 0; i < length; i++) {
                    //------------------------------------------
                    //Date
                    txt = Turniere.subList(i, i + 1).toString();
                    pattern = Pattern.compile("\\[Datum\\](.*?)\\[Datum2\\]");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt = matcher.group(1);
                        if(txt==" "){}
                        else{
                            date[i] = txt;
                        }
                    }
                    //------------------------------------------
                    //Turnier
                    txt = Turniere.subList(i, i + 1).toString();
                    pattern = Pattern.compile("\\[Name\\](.*?)\\[Name2\\]");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt = matcher.group(1);
                        if(txt==" "){}
                        else{
                            turnier[i] = txt; //.substring(0, 15);
                        }
                    }
                    //------------------------------------------
                    //ID
                    txt = Turniere.subList(i, i + 1).toString();
                    pattern = Pattern.compile("\\[ID\\](.*?)\\[ID2\\]");
                    matcher = pattern.matcher(txt);
                    while (matcher.find()) {
                        txt = matcher.group(1);
                        if(txt==" "){}
                        else{
                            turnier_id[i] = txt;

                        }
                    }
                    //------------------------------------------
            }
            LA_2Rows listA2rows = new LA_2Rows(mainActivtiy, context, turnier, date);
            mCompleteListView.setAdapter(listA2rows);
            //mCompleteListView.removeHeaderView(listHeader);
            //mCompleteListView.addHeaderView(listHeader);
            swipeLayout.setRefreshing(false);
            //mCompleteListView.setVisibility((listA2rows.isEmpty())?View.GONE:View.VISIBLE);
            mCompleteListView.startAnimation(animSlideUp);
        }
    }
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------
}
