package com.christopherkegel.gcb_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by christopherkegel on 07.06.14.
 */
public class LA_Simple extends BaseAdapter {

        private class SingleRow {
            String lblTag;
            String lblDatum;
            String lblMessage;
            String lblKlasse;

            public SingleRow(String tag,String datum , String message, String klasse){
                this.lblTag         = tag;
                this.lblDatum       = datum;
                this.lblMessage     = message;
                this.lblKlasse      = klasse;
            }

        }

        class MyViewHolder{
            TextView v_myTag;
            TextView v_myDatum;
            TextView v_myMessage;
            TextView v_myKlasse;
            LinearLayout v_ll_bg;
            MyViewHolder(View v){
                v_myTag     = (TextView)        v.findViewById(R.id.lblTag);
                v_myDatum   = (TextView)        v.findViewById(R.id.lblDatum);
                v_myMessage = (TextView)        v.findViewById(R.id.lblMessage);
                v_myKlasse  = (TextView)        v.findViewById(R.id.lblKlasse);
                v_ll_bg     = (LinearLayout)    v.findViewById(R.id.ll_bg);
            }
        }

        private MainActivity    mainActivity;
        private Context         context;
        private String[]        tage;
        private String[]        daten;
        private String[]        messages;
        private String[]        klassen;
        private int             leange;

        private LayoutInflater inflater;
        private ArrayList<SingleRow> list;

        public LA_Simple(MainActivity mainActivity1, Context context, String[] tage, String[] daten, String[] messages, String[] klassen)
        {
            this.mainActivity   = mainActivity1;
            this.context        = context;
            this.tage           = tage;
            this.daten          = daten;
            this.messages       = messages;
            this.klassen        = klassen;
            list                = new ArrayList<SingleRow>();

            if(tage != null){
                leange = tage.length;
            }
            else{
                leange = 0;
            }
                String[] tag        = new String[leange];
                String[] datum      = new String[leange];
                String[] message    = new String[leange];
                String[] klasse     = new String[leange];

            for(int i = 0; i < leange; i++){
                tag[i]          = tage[i];
                datum[i]        = daten[i];
                message[i]      = messages[i];
                klasse[i]       = klassen[i];
            }
            for(int i = 0; i < leange; i++) {
                list.add(new SingleRow( datum[i],tag[i],message[i],klasse[i]));
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
                row = inflater1.inflate(R.layout.list_group, parent, false);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            }
            else{
                holder = (MyViewHolder) row.getTag();
            }
            SingleRow temp = list.get(position);
            holder.v_myTag.setText(temp.lblTag);
            holder.v_myDatum.setText(temp.lblDatum);
            holder.v_myMessage.setText(temp.lblMessage);
            holder.v_myKlasse.setText(temp.lblKlasse);
            if(temp.lblMessage.equals("Platzinformation")){
                holder.v_ll_bg.setBackground(mainActivity.getResources().getDrawable(R.drawable.ms_bg_dr));
            }
            if(temp.lblMessage.equals("Driving Range")){
                holder.v_ll_bg.setBackground(mainActivity.getResources().getDrawable(R.drawable.ms_bg_dr));
            }
            if(!temp.lblMessage.equals("Driving Range") && !temp.lblMessage.equals("Platzinformation")){
                //holder.v_ll_bg.setBackground(mainActivity.getResources().getDrawable(R.drawable.ms_bg_pi));
            }
            Calendar c = Calendar.getInstance();
            int akt_Tag = c.get(Calendar.DAY_OF_MONTH);
            String s_akt_Tag = Integer.toString(akt_Tag);

            if(s_akt_Tag.equals(temp.lblDatum)){
                holder.v_myDatum.setTextColor(mainActivity.getResources().getColor(R.color.txt_blau));

                holder.v_myTag.setTextColor(mainActivity.getResources().getColor(R.color.txt_blau));
            }
        return row;
        }
}
