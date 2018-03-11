package noyansoft.ecssistem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Random;

public class DevSettings extends AppCompatActivity {
    Button mainm,testp;
    Random r = new Random();
    TextView txtnotif;
    SharedPreferences sharedPref;
    Switch sw1;
    Boolean swb = false;
    Boolean dsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_settings);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tooldev);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.condev);
        int i = r.nextInt(3-0)+0;
        switch (i){
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
        sharedPref = this.getSharedPreferences("SQLPref", Context.MODE_PRIVATE);
        dsave = sharedPref.getBoolean("datasaver",false);
        mainm = (Button) findViewById(R.id.mainmenu);
        testp = (Button) findViewById(R.id.testpage1);
        mainm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevSettings.this,ActSecim.class);
                startActivity(i);
            }
        });
        testp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevSettings.this,CihazBakim.class);
                startActivity(i);
            }
        });
        sw1 = (Switch) findViewById(R.id.switch3);
        if(dsave){sw1.setChecked(true); swb = true;}
        else{sw1.setChecked(false); swb=false;}
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                swb = bChecked;
                if (bChecked) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("datasaver",true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("datasaver",false);
                    editor.commit();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,  FirebaseInstanceId.getInstance().getToken());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Bildirim kodunu paylaş"));
                return true;
            case R.id.action_alert:
                Intent i = new Intent(DevSettings.this,Alerts.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
