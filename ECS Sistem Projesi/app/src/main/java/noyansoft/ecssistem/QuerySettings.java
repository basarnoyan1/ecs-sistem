package noyansoft.ecssistem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class QuerySettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SharedPreferences sharedPref;
    private Spinner spinner;
    private static final String[]paths = {"Giriş Ekranı","Stok Sayım", "Stok Kontrol"};
    TextView text1,text2,text3;
    EditText edit1,edit2,edit3;
    Button save1;
    int pos;
    String queryLogin;
    Switch sw1;
    Boolean swb = false;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_settings);
        sharedPref = QuerySettings.this.getSharedPreferences("SQLPref", MODE_PRIVATE);
        queryLogin = sharedPref.getString("query_login","Kayıt yok!");
        sw1 = (Switch) findViewById(R.id.switch2);
        text1 = (TextView) findViewById(R.id.txt1);
        edit1 = (EditText) findViewById(R.id.edt1);
        text2 = (TextView) findViewById(R.id.txt2);
        edit2 = (EditText) findViewById(R.id.edt2);
        text3 = (TextView) findViewById(R.id.txt3);
        edit3 = (EditText) findViewById(R.id.edt3);
        save1 = (Button) findViewById(R.id.save);
        spinner = (Spinner)findViewById(R.id.spinner);
        edit1.setVisibility(View.INVISIBLE);
        edit2.setVisibility(View.INVISIBLE);
        edit3.setVisibility(View.INVISIBLE);
        edit1.setVisibility(View.INVISIBLE);
        edit2.setVisibility(View.INVISIBLE);
        edit3.setVisibility(View.INVISIBLE);
        spinner.setEnabled(false);
        save1.setVisibility(View.INVISIBLE);
        sw1.setText("Sorgu ayarlarını göster     ");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(QuerySettings.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                swb = bChecked;
                if (bChecked) {
                    spinner.setEnabled(true);
                    save1.setVisibility(View.VISIBLE);
                    sw1.setText("Sorgu ayarlarını gizle     ");
                    sw_ayar(pos);
                } else {
                    edit1.setVisibility(View.INVISIBLE);
                    edit2.setVisibility(View.INVISIBLE);
                    edit3.setVisibility(View.INVISIBLE);
                    edit1.setVisibility(View.INVISIBLE);
                    edit2.setVisibility(View.INVISIBLE);
                    edit3.setVisibility(View.INVISIBLE);
                    spinner.setEnabled(false);
                    save1.setVisibility(View.INVISIBLE);
                    sw1.setText("Sorgu ayarlarını göster     ");
                }
            }
        });
        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   switch (pos) {
                       case 0:
                           SharedPreferences.Editor editor = sharedPref.edit();
                           editor.putString("query_login", edit1.getText().toString());
                           editor.commit();
                           queryLogin = sharedPref.getString("query_login", "Kayıt yok!");
                           Log.e("QuerySet-1", queryLogin);
                           break;
                       case 1:

                           break;
                       case 2:

                           break;
                   }
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(swb) {sw_ayar(position);}
    }
    public void onNothingSelected(AdapterView ad) {

    }
public void sw_ayar(int p){

    switch (p) {
        case 0:
            text1.setVisibility(View.VISIBLE);
            edit1.setVisibility(View.VISIBLE);
            save1.setVisibility(View.VISIBLE);
            text1.setText("Giriş");
            edit1.setText(queryLogin);
            pos=0;
            break;
        case 1:
            text1.setVisibility(View.VISIBLE);
            edit1.setVisibility(View.VISIBLE);
            save1.setVisibility(View.VISIBLE);
            text1.setText("Stok Bilgileri");
            pos=1;
            break;
        case 2:
            text1.setVisibility(View.VISIBLE);
            edit1.setVisibility(View.VISIBLE);
            save1.setVisibility(View.VISIBLE);
            text1.setText("Stok Bilgileri");
            pos=2;
            break;
    }
}

}

