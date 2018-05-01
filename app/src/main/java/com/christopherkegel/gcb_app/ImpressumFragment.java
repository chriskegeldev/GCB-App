package com.christopherkegel.gcb_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by christopherkegel on 18.12.14.
 */
public class ImpressumFragment extends Fragment {
    //-----------------------------------------
    private View rootView;
    private ProgressBar spinner3;
    private String url = "http://www.golfclub-badrappenau.de/";
    private Dialog mDialog;
    private MainActivity        mainActivity;
    private Context             context;
    // Arguments
    private static final String ARG_MAIN_ACTIVTIY = "MA";
    private static final String ARG_Context = "CO";
    //-----------------------------------------
    public ImpressumFragment(){}
    /*
    @SuppressLint("ValidFragment")
    public ImpressumFragment(Context context2){
        this.context = context2;
    }*/
    //-----------------------------------------
    public View getView(){
        return rootView;
    }
    //-----------------------------------------
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
        rootView = inflater.inflate(R.layout.fragment_impressum, container, false);
        //-----------------------------------------
        return rootView;
        //-----------------------------------------
    }
//-----------------------------------------
}

