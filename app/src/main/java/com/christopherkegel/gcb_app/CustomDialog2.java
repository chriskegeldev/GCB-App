package com.christopherkegel.gcb_app;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by christopherkegel on 19.05.15.
 */


public class CustomDialog2 extends Dialog {


    public CustomDialog2(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_lollipop);
        TextView title       = (TextView)    findViewById(R.id.dialog_title);
        TextView    description = (TextView)    findViewById(R.id.dialog_description);
        Button btnOK       = (Button)      findViewById(R.id.button2);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        title.setText("Keine Ausweisdaten");
        description.setText("Du hast unter Settings noch keine Angaben zu deiner Ausweis- und Servicenummer gemacht.");
        show();
    }
}
