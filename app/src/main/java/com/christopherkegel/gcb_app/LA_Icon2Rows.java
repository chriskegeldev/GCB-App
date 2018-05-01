package com.christopherkegel.gcb_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christopherkegel on 07.06.14.
 */
public class LA_Icon2Rows extends BaseAdapter {

        private class SingleRow {
            String lblInfo;
            String lblHeader;

            public SingleRow(String info,String header ){
                this.lblInfo         = info;
                this.lblHeader       = header;
            }

        }

        class MyViewHolder{
            TextView    v_myInfo;
            TextView    v_myHeader;
            ImageView   v_myIcon;
            MyViewHolder(View v){
                v_myInfo     = (TextView)   v.findViewById(R.id.lblHeader2);
                v_myHeader   = (TextView)   v.findViewById(R.id.lblInfo2);
                v_myIcon     = (ImageView)  v.findViewById(R.id.lblIcon);
            }
        }

        private MainActivity mainActivity;
        private Context context;
        private String[] header;
        private String[] infos;
        private int             leange;
        private LayoutInflater inflater;
        private ArrayList<SingleRow> list;

        public LA_Icon2Rows(MainActivity mainActivity1, Context context, String[] header, String[] infos)
        {
            this.mainActivity   = mainActivity1;
            this.context        = context;
            this.header         = header;
            this.infos          = infos;
            list                = new ArrayList<SingleRow>();

            if(header != null){
                leange = header.length;
            }
            else{
                leange = 0;
            }
            String[] head      = new String[header.length];
            String[] info      = new String[header.length];

            for(int i = 0; i < header.length; i++){
                head[i]         = header[i];
                info[i]         = infos[i];
            }
            for(int i = 0; i < header.length; i++) {
                list.add(new SingleRow( head[i],info[i]));
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
                row = inflater1.inflate(R.layout.list_group_hcp, parent, false);
                holder = new MyViewHolder(row);
                row.setTag(holder);
            }
            else{
                holder = (MyViewHolder) row.getTag();
            }
            SingleRow temp = list.get(position);
            holder.v_myInfo.setText(temp.lblInfo);
            holder.v_myHeader.setText(temp.lblHeader);
            Map<String, Integer> map = new HashMap<String, Integer>();

            if(temp.lblHeader != null ){

            switch(temp.lblHeader){
                case "Temperatur":
                    map.put("temperatur", R.drawable.wetter_w);
                    holder.v_myIcon.setImageResource(map.get("temperatur"));
                    break;
                case "Details":
                    map.put("details", R.drawable.details);
                    holder.v_myIcon.setImageResource(map.get("details"));
                    break;
                case "Luftfeuchtigkeit":
                    map.put("luft", R.drawable.hum);
                    holder.v_myIcon.setImageResource(map.get("luft"));
                    break;
                case "Windgeschwindigkeit":
                    map.put("wind", R.drawable.wind);
                    holder.v_myIcon.setImageResource(map.get("wind"));
                    break;
                case "Heimatclub":
                    map.put("club", R.drawable.club);
                    holder.v_myIcon.setImageResource(map.get("club"));
                    break;
                case "HCP":
                    map.put("hcp", R.drawable.scorecard);
                    holder.v_myIcon.setImageResource(map.get("hcp"));
                    break;
                case "Angemeldet als":
                    map.put("account", R.drawable.account);
                    holder.v_myIcon.setImageResource(map.get("account"));
                    break;
                default:
                    break;
            }

            }
        return row;
        }
}
