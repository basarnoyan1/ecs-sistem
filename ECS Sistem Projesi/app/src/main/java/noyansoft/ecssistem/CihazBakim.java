package noyansoft.ecssistem;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class CihazBakim extends AppCompatActivity {
    TextView txt1,txt2,txt3,txt4,txt5;
    Button btn1,btn2,btn3,btn4,btn5;
    ConstraintLayout con1,con2,con3,con4,con5;
    View inc1,inc2,inc3,inc4,inc5;
    WebView web;
    CardView crd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cihaz_bakim);
        crd = (CardView) findViewById(R.id.cardView12);
        inc1 = findViewById(R.id.chip_1);
        inc2 = findViewById(R.id.chip_2);
        inc3 = findViewById(R.id.chip_3);
        inc4 = findViewById(R.id.chip_4);
        inc5 = findViewById(R.id.chip_5);
        web = (WebView) findViewById(R.id.webview);
        web.loadUrl("https://www.google.com.tr/?gws_rd=ssl");
        txt1 = (TextView) inc1.findViewById(R.id.chiptext);
        btn1 = (Button) inc1.findViewById(R.id.chipbutton);
        con1 = (ConstraintLayout) inc1.findViewById(R.id.chipcons);
        Test(txt1,btn1,con1);
        txt2 = (TextView) inc2.findViewById(R.id.chiptext);
        btn2 = (Button) inc2.findViewById(R.id.chipbutton);
        con2 = (ConstraintLayout) inc2.findViewById(R.id.chipcons);
        Test(txt1,btn2,con2);
        txt3 = (TextView) inc3.findViewById(R.id.chiptext);
        btn3 = (Button) inc3.findViewById(R.id.chipbutton);
        con3 = (ConstraintLayout) inc3.findViewById(R.id.chipcons);
        Test(txt3,btn3,con3);
        txt4 = (TextView) inc4.findViewById(R.id.chiptext);
        btn4 = (Button) inc4.findViewById(R.id.chipbutton);
        con4= (ConstraintLayout) inc4.findViewById(R.id.chipcons);
        Test(txt4,btn4,con4);
        txt5 = (TextView) inc5.findViewById(R.id.chiptext);
        btn5 = (Button) inc5.findViewById(R.id.chipbutton);
        con5 = (ConstraintLayout) inc5.findViewById(R.id.chipcons);
        Test(txt5,btn5,con5);
            txt1.setText("Google");
            txt2.setText("Android");
            txt3.setText("Android Developers");
            txt4.setText("Microsoft");
            txt5.setText("Apple");
            con1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web.loadUrl("https://www.google.com.tr/?gws_rd=ssl");
                }
            });
            con2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web.loadUrl("https://www.android.com/");
                }
            });
            con3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web.loadUrl("https://developer.android.com/index.html");
                }
            });
            con4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web.loadUrl("https://www.microsoft.com/tr-tr/");
                }
            });
            con5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web.loadUrl("https://www.apple.com/");
                }
            });
        }
    public void Test(final TextView txtt,final Button btnt,final ConstraintLayout cont){
        btnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar
                        .make(txtt, "Silindi.", Snackbar.LENGTH_LONG)
                        .setAction("GERİ AL", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(txtt, "Geri alındı.", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                                cont.setVisibility(View.VISIBLE);
                            }});snackbar.show();}
        });
    }
}

