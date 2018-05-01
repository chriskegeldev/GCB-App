package com.christopherkegel.gcb_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by christopherkegel on 07.06.14.
 */
public class LA_2Rows extends BaseAdapter {

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
            MyViewHolder(View v){
                v_myInfo     = (TextView)   v.findViewById(R.id.lblHeader1);
                v_myHeader   = (TextView)   v.findViewById(R.id.lblInfo1);
            }
        }

        private MainActivity mainActivity;
        private Context context;
        private String[] header;
        private String[] infos;
        private int             leange;
        private LayoutInflater inflater;
        private ArrayList<SingleRow> list;

        public LA_2Rows(MainActivity mainActivity1, Context context, String[] header, String[] infos)
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
                row = inflater1.inflate(R.layout.list_group_2rows, parent, false);
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


        return row;
        }
}
