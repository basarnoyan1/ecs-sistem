package noyansoft.ecssistem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import java.util.Random;

public class ActSecim extends AppCompatActivity {
    Random r = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_secim);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.actcon);
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
        Button button = (Button) findViewById(R.id.button3);
        Button button2 = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent ia = new Intent(ActSecim.this, StokSayim.class);
                startActivity(ia);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent ic = new Intent(ActSecim.this, StokKontrol.class);
                startActivity(ic);
            }
        });
    }
}
