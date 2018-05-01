package com.christopherkegel.gcb_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by christopherkegel on 07.06.14.
 */
public class LA_Wetter extends BaseAdapter {

        private class SingleRow {
            String lblTag;
            String lblDatum;
            String lblTemp;
            String lblDetails;
            String lblLuft;
            String lblWind;

            public SingleRow(String tag,String datum , String temp, String details, String luft, String wind){
                this.lblTag         = tag;
                this.lblDatum       = datum;
                this.lblTemp        = temp;
                this.lblDetails     = details;
                this.lblLuft        = luft;
                this.lblWind        = wind;
            }

        }

        class MyViewHolder{
            TextView v_myTag;
            TextView v_myDatum;
            TextView v_myTemp;
            TextView v_myDetails;
            TextView v_myLuft;
            TextView v_myWind;
            LinearLayout v_ll_bg;
            MyViewHolder(View v){
                v_myTag     = (TextView)        v.findViewById(R.id.lblTag);
                v_myDatum   = (TextView)        v.findViewById(R.id.lblDatum);
                v_myTemp    = (TextView)        v.findViewById(R.id.lblTemp);
                v_myDetails = (TextView)        v.findViewById(R.id.lblDetails);
                v_myLuft    = (TextView)        v.findViewById(R.id.lblLuft);
                v_myWind    = (TextView)        v.findViewById(R.id.lblWind);
                v_ll_bg     = (LinearLayout)    v.findViewById(R.id.ll_bg);
            }
        }

        private MainActivity    mainActivity;
        private Context         context;
        private String[]        tage;
        private String[]        daten;
        private String[]        temper;
        private String[]        detailse;
        private String[]        lufte;
        private String[]        winde;

        private int             leange;

        private LayoutInflater inflater;
        private ArrayList<SingleRow> list;

        public LA_Wetter(MainActivity mainActivity1, Context context1, String[] tage1,
                         String[] daten1, String[] temper1, String[] detailse1, String[] lufte1,String[] winde1)
        {
            this.mainActivity   = mainActivity1;
            this.context        = context1;
            this.tage           = tage1;
            this.daten          = daten1;
            this.temper         = temper1;
            this.detailse       = detailse1;
            this.lufte          = lufte1;
            this.winde          = winde1;
            list                = new ArrayList<SingleRow>();

            if(tage != null){
                leange = tage.length;
            }
            else{
                leange = 0;
            }
                String[] tag        = new String[leange];
                String[] datum      = new String[leange];
                String[] temps      = new String[leange];
                String[] details    = new String[leange];
                String[] lufts      = new String[leange];
                String[] winds      = new String[leange];

            for(int i = 0; i < leange; i++){
                tag[i]          = tage[i];
                datum[i]        = daten[i];
                temps[i]        = temper[i];
                details[i]      = detailse[i];
                lufts[i]        = lufte[i];
                winds[i]        = winde[i];
            }
            for(int i = 0; i < leange; i++) {
                list.add(new SingleRow( datum[i],tag[i],temps[i],details[i],lufts[i],winds[i]));
            }

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            MyViewHolder holder = null;
            if(holder == null){
                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater1.inflate(R.layout.list_group_wetter, parent, false);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            }
            else{
                holder = (MyViewHolder) row.getTag();
            }
            SingleRow temp = list.get(position);
            holder.v_myTag.setText(temp.lblTag);
            holder.v_myDatum.setText(temp.lblDatum);
            holder.v_myTemp.setText(temp.lblTemp);
            holder.v_myDetails.setText(temp.lblDetails);
            holder.v_myLuft.setText(temp.lblLuft);
            holder.v_myWind.setText(temp.lblWind);

            /*
            Calendar c = Calendar.getInstance();
            int akt_Tag = c.get(Calendar.DAY_OF_MONTH);
            String s_akt_Tag = Integer.toString(akt_Tag);

            if(s_akt_Tag.equals(temp.lblDatum)){
                holder.v_myDatum.setTextColor(mainActivity.getResources().getColor(R.color.txt_blau));
                holder.v_myTag.setTextColor(mainActivity.getResources().getColor(R.color.txt_blau));
            }
            */
        return row;
        }
}
