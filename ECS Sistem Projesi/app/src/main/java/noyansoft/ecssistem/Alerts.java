package noyansoft.ecssistem;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Alerts extends AppCompatActivity {
    Random r = new Random();
    ListView lst;
    public ArrayAdapter<String> strarray;
    public static ArrayList<String> alert = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        TextView txt = (TextView) findViewById(R.id.noalert);
        CardView crd = (CardView) findViewById(R.id.cardView11);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolalert);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conalert);
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
        lst = (ListView) findViewById( R.id.alview);
        ArrayList<String> arrayList = new ArrayList<String>();
        strarray = new ArrayAdapter<String>(this, R.layout.list_item,arrayList);
        try{
        if(!alert.isEmpty()){
        arrayList.addAll(alert);
        txt.setVisibility(View.GONE);
        crd.setVisibility(View.VISIBLE);
        }
        else {
        txt.setVisibility(View.VISIBLE);
        crd.setVisibility(View.GONE);
        }}
        catch (Exception e){}
        lst.setAdapter(strarray);
    }
}
