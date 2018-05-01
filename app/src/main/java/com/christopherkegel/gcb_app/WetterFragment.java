package com.christopherkegel.gcb_app;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;

import org.json.JSONObject;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by christopherkegel on 18.12.14.
 */
public class WetterFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
//------------------------------------------------------------------------------------
// Variables -------------------------------------------------------------------------
//------------------------------------------------------------------------------------
    private View                rootView;
    private MainActivity        mainActivity;
    private Context             context;
    private CustomDialog        mDialog;
    private Typeface            weatherFont;
    private ListView            mCompleteListView;
    private View                listHeader;
    private SwipeRefreshLayout  swipeLayout;
    private Handler             handler;
    private ShowcaseView        sv;
    private Target              t1,t2;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY2 = "MA2";
    // Animation
            Animation           animSlideUp;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";
//------------------------------------------------------------------------------------
// Set Constructor -------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public WetterFragment(){
        mainActivity   = ((MainActivity)getActivity());
        context         = mainActivity;
    }
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
                             Bundle savedInstanceState) {
        //-----------------------------------------
        //-----------------------------------------
        //this.mainActivity   = (MainActivity) getArguments().getSerializable(ARG_MAIN_ACTIVTIY);
        //this.context = (Context) getArguments().getSerializable(ARG_Context);
        mainActivity   = ((MainActivity)getActivity());
        context         = mainActivity;
        //-----------------------------------------
        //-----------------------------------------
        rootView = inflater.inflate(R.layout.fragment_wetter, container, false);
        //-----------------------------------------
        listHeader          = mainActivity.getLayoutInflater().inflate(R.layout.list_header, null);
        mCompleteListView   = (ListView) rootView.findViewById(R.id.listViewWetter);
        //-----------------------------------------
        // load the animation
        animSlideUp = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        //-----------------------------------------
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container4);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_light);
        //-----------------------------------------
        //-----------------------------------------
        /*mainActivity.onDrawerClosed(rootView);
        t1 = new ViewTarget(R.id.listViewWetter, mainActivity);// Menu
        sv = new ShowcaseView.Builder(mainActivity)
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
        */
        return rootView;
        //-----------------------------------------
    }
//------------------------------------------------------------------------------------
// Set onCreate ----------------------------------------------------------------------
//------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
        new Title2(mainActivity, new CityPreference(((MainActivity)getActivity())).getCity()).execute();
       // updateWeatherData(new CityPreference(getActivity()).getCity());
    }
//------------------------------------------------------------------------------------
// Set onRefresh ---------------------------------------------------------------------
//------------------------------------------------------------------------------------
    public void onRefresh() {
                if (isNetworkAvailable() == true) {
                    new Title2(mainActivity,new CityPreference(((MainActivity)getActivity())).getCity()).execute();
                   // updateWeatherData(new CityPreference(getActivity()).getCity());
                } else {
                    //mDialog = new CustomDialog(context);
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
        String[] tage_nr ,tage_st, temp, details , lufts, winds;
        MainActivity mainActivtiy;
        String city2;
        String[][] Wetter = new String[4][2];

        public Title2(MainActivity mainActivtiy1, final String city){
            mainActivtiy = mainActivtiy1;
            city2 = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final JSONObject json = RemoteFetch.getJSON(getActivity(), city2);
            if(json == null){
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getActivity(),
                                getActivity().getString(R.string.place_not_found),
                                Toast.LENGTH_LONG).show();
                    }
                });
                /*Toast.makeText(getActivity(),
                        getActivity().getString(R.string.place_not_found),
                        Toast.LENGTH_LONG).show();*/
            } else {

                handler.post(new Runnable() {
                    public void run() {
                        Wetter = renderWeather(json);
                    }
                });
                /*Wetter = renderWeather(json);*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //------------------------------------------
            int length = 1;
            String txt = null;
            //------------------------------------------
            if(Wetter != null) {
                tage_nr = new String[length];
                tage_st = new String[length];
                temp = new String[length];
                details = new String[length];
                lufts = new String[length];
                winds = new String[length];
                //------------------------------------------
                for (int i = 0; i < 1; i++) {
                    //------------------------------------------
                    Calendar c = Calendar.getInstance();
                    int akt_Tag = c.get(Calendar.DAY_OF_MONTH);
                    String s_akt_Tag = Integer.toString(akt_Tag);
                    //------------------------------------------
                    tage_nr[i]  = s_akt_Tag;
                    tage_st[i]  = "Mo";
                    temp[i]     = Wetter[i][0];
                    details[i]  = Wetter[i][1];
                    lufts[i]    = Wetter[i][2];
                    winds[i]    = Wetter[i][3];
                    //------------------------------------------
                }
            }
            mCompleteListView.setAdapter(new LA_Wetter(mainActivtiy, context, tage_nr, tage_st  , temp, details, lufts, winds));
            mCompleteListView.removeHeaderView(listHeader);
            mCompleteListView.addHeaderView(listHeader);
            //spinner3.setVisibility(View.GONE);
            mCompleteListView.startAnimation(animSlideUp);
        }
    }

    private String[][] renderWeather(JSONObject json){
        String[][] Wetter = new String[1][6];
        for(int i = 0; i<1;i++){
            try {
                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                JSONObject main = json.getJSONObject("main");
                JSONObject wind = json.getJSONObject("wind");
                //JSONObject rain = json.getJSONObject("rain");

                double kmh = Double.parseDouble(wind.getString("speed"));
                kmh = kmh * 1.609344;

                Wetter[i][0]= String.format("%.2f", main.getDouble("temp"))+ " ℃";
                //Wetter[0][1]= "Temperatur";
                Wetter[i][1]= details.getString("description").toUpperCase(Locale.US);
                //Wetter[1][1]= "Details";
                Wetter[i][2]= main.getString("humidity") + "%" ;
                //Wetter[2][1]= "Luftfeuchtigkeit";
                Wetter[i][3]= Double.toString(kmh).substring(0, 4) + " km/h";
                //Wetter[3][1]= "Windgeschwindigkeit";
                //Wetter[i][4]=
            }catch(Exception e){
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }

        swipeLayout.setRefreshing(false);
        return Wetter;
    }
//--------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------
/*
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
//-----------------------------------------
    private void renderWeather(JSONObject json){
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            //JSONObject rain = json.getJSONObject("rain");

            detailsField.setText( details.getString("description").toUpperCase(Locale.US) );
            luftfeuchtigkeitField.setText( main.getString("humidity") + "%" );
            double kmh = Double.parseDouble(wind.getString("speed"));
            kmh = kmh * 1.609344;
            windField.setText( Double.toString(kmh).substring(0, 4) + " km/h");

            //currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp"))+ " ℃");
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
//-----------------------------------------
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        //weatherIcon.setText(icon);
    }
*/
//-----------------------------------------
}



