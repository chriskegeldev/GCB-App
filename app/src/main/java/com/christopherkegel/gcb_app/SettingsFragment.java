package com.christopherkegel.gcb_app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by christopherkegel on 24.04.15.
 */
public class SettingsFragment extends PreferenceFragment {

    private MainActivity        mainActivity;
    private Context             context;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";

    public SettingsFragment(){}

    /*@SuppressLint("ValidFragment")
    public SettingsFragment(MainActivity mainActivity1){
        this.mainActivity = mainActivity1;
    }*/

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //-----------------------------------------
        //-----------------------------------------
        //this.mainActivity   = (MainActivity) getArguments().getSerializable(ARG_MAIN_ACTIVTIY);
        //this.context = (Context) getArguments().getSerializable(ARG_Context);
        mainActivity   = ((MainActivity)getActivity());
        context         = mainActivity;
        //-----------------------------------------
        //-----------------------------------------
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.txt_line_2));
        return view;
    }
}
