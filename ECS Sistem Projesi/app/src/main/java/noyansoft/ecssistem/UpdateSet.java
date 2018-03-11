package noyansoft.ecssistem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class UpdateSet extends AppCompatActivity {
    Random r = new Random();
    Button netpref,beta;
    SharedPreferences sharedPref;
    CharSequence[] net_alert = {"Sadece kablosuz ağlar","Tüm ağlar"};
    TextView txt;
    int i = 1;
    String s;
    Boolean betab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_set);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolupd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conupd);
        int i2 = r.nextInt(3-0)+0;
        switch (i2){
            case 0:
                cons.setBackgroundResource(R.drawable.wall);
                break;
            case 1:
                cons.setBackgroundResource(R.drawable.wall2);
                break;
            case 2:
                cons.setBackgroundResource(R.drawable.wall3);
                break;
            default:
                cons.setBackgroundResource(R.drawable.wall);
                break;
        }
        sharedPref = this.getSharedPreferences("SQLPref", MODE_PRIVATE);
        i = sharedPref.getInt("netpref",1);
        betab = sharedPref.getBoolean("betatester",false);
        netpref = (Button) findViewById(R.id.network);
        beta = (Button) findViewById(R.id.joinbeta);
        if(betab){beta.setText("AYRIL");}
        txt = (TextView) findViewById(R.id.textView20);
        txt.setText(net_alert[i]);
        netpref.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSet.this);
                builder.setTitle("Otomatik Güncellemeler");
                builder.setSingleChoiceItems(net_alert,i,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            i = which;
                            s = net_alert[which].toString();
                            txt.setText(s);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("netpref", i);
                            editor.commit();
                        dialog.dismiss();

                    }
                });
                builder.setPositiveButton("KAPAT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
            }
        });
        beta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(betab){
                    betab = false;
                    beta.setText("KATIL");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("betatester", betab);
                    editor.commit();
                }else{
                    betab = true;
                    beta.setText("AYRIL");
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("betatester", betab);
                    editor.commit();
                }
            }});
    }
}
