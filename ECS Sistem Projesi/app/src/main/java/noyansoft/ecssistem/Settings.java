package noyansoft.ecssistem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.util.Random;

public class Settings extends AppCompatActivity {
    Random r = new Random();
    Switch sw1;
    Button save1;
    EditText e1,e2,e3,e4;
    ConnectionClass con;
    SharedPreferences sharedPref;
    String savedString,savedString2,savedString3,savedString4;
    String ip2,db2,un2,ps2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conserver);
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
        con = new ConnectionClass();
        sharedPref = this.getSharedPreferences("SQLPref",Context.MODE_PRIVATE);
        savedString = sharedPref.getString("ipadres","");
        savedString2 = sharedPref.getString("database","");
        savedString3 = sharedPref.getString("username","");
        savedString4 = sharedPref.getString("password","");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        sw1 = (Switch) findViewById(R.id.switch1);
        save1 = (Button) findViewById(R.id.save);
        e1 = (EditText) findViewById(R.id.ipadres);
        e2 = (EditText) findViewById(R.id.database);
        e3 = (EditText) findViewById(R.id.usern);
        e4 = (EditText) findViewById(R.id.passw);
        e1.setText(savedString);
        e2.setText(savedString2);
        e3.setText(savedString3);
        e4.setText(savedString4);

        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    e1.setEnabled(true);
                    e2.setEnabled(true);
                    e3.setEnabled(true);
                    e4.setEnabled(true);
                    save1.setVisibility(View.VISIBLE);
                    sw1.setText("Sunucu ayarlarını gizle     ");
                } else {
                    e1.setEnabled(false);
                    e2.setEnabled(false);
                    e3.setEnabled(false);
                    e4.setEnabled(false);
                    save1.setVisibility(View.INVISIBLE);
                    sw1.setText("Sunucu ayarlarını göster     ");
                }
            }
        });
        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Save();
            }
        });
    }
    public void Save(){
        con.ip = e1.getText().toString();
        con.db = e2.getText().toString();
        con.un = e3.getText().toString();
        con.password = e4.getText().toString();
        Snackbar.make(save1,"Bilgiler kaydedildi.",Snackbar.LENGTH_SHORT).show();
        Log.e("Kayıt",con.db);
        SharedPreferences.Editor editor = sharedPref.edit();
        String loadedString = sharedPref.getString("ipadres","Kayıt Yok");
        Log.e("Kayıt-a",loadedString);
        editor.putString("ipadres",e1.getText().toString());
        editor.putString("database",e2.getText().toString());
        editor.putString("username",e3.getText().toString());
        editor.putString("password",e4.getText().toString());
        editor.commit();
        Log.e("Kayıt-a",savedString);
        Log.e("Kayıt-a",savedString2);
        Log.e("Kayıt-a",savedString3);
        Log.e("Kayıt-a",savedString4);
    }
}
