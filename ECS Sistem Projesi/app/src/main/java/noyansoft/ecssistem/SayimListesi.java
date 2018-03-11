package noyansoft.ecssistem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SayimListesi extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ListView lst;
    SearchView searchView;
    Random r = new Random();
    ProgressBar pbr;
    TextView chiptext;
    Button chipbutton;
    ConstraintLayout chipcon;
    ConnectionClass conc = new ConnectionClass();
    ArrayAdapter<String> strarray;
    ArrayList<String> arraylst = new ArrayList<String>();
    SimpleAdapter simpleAdapter;
    private DatePickerDialog datepicker;
    SimpleDateFormat dateFormat;
    Button dateslct;
    private static final String ACTION_VOICE_SEARCH = "com.google.android.gms.actions.SEARCH_ACTION";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sayim_listesi);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toollist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conlist);
        int i = r.nextInt(3 - 0) + 0;
        switch (i) {
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
        pbr = (ProgressBar) findViewById(R.id.progr1);
        lst = (ListView) findViewById(R.id.list);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        dateslct = (Button) findViewById(R.id.datebt);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        chipcon = (ConstraintLayout) findViewById(R.id.chip1);
        chipbutton = (Button) findViewById(R.id.chipbtn);
        chiptext = (TextView) findViewById(R.id.chiptxt);
        DateSet();
        searchView = (SearchView) findViewById(R.id.google);
        searchView.setQueryHint("Ara");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipcon.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dateslct.setVisibility(View.VISIBLE);
                return false;
            }
        });
        dateslct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datepicker.show();
            }
        });
        chipbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chipcon.setVisibility(View.GONE);
                searchView.setQuery("", true);
                dateslct.setVisibility(View.VISIBLE);
            }
        });
        chipcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datepicker.show();
            }
        });
        DoList doList = new DoList();
        doList.execute("");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        dateslct.setVisibility(View.VISIBLE);
        simpleAdapter.getFilter().filter(query);
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        simpleAdapter.getFilter().filter(newText);
        dateslct.setVisibility(View.GONE);
        chipcon.setVisibility(View.GONE);
        return true;
    }
    public class DoList extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(String r) {
            if(isSuccess) {
                Log.w("Task","Task completed.");
            }else{Snackbar.make(lst,r,Snackbar.LENGTH_SHORT).show();}

            String[] from = { "A", "B", "C","D","E","F"};
            int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc,R.id.lblmiktar,R.id.lbltarih,R.id.lblaciklama };
            simpleAdapter = new SimpleAdapter(SayimListesi.this,
                    prolist, R.layout.lstemplate, from,
                    views);
            lst.setAdapter(simpleAdapter);
            pbr.setVisibility(View.GONE);
            VoiceSearch(getIntent());
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = conc.CONN(SayimListesi.this);
                if (con == null) {
                    z = "SQL sunucuya bağlanılamadı.";
                    pbr.setVisibility(View.GONE);
                } else {
                    String query = "SELECT STOK_KODU,SERI_NO,DEPO_KODU,CAST(MIKTAR AS INT) as MIKTAR,TARIH,ACIKLAMA FROM ECS_YENI_SAYIM ORDER BY TARIH DESC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString(1));
                        datanum.put("B", rs.getString(2));
                        datanum.put("C", rs.getString(3));
                        datanum.put("D", rs.getString(4));
                        datanum.put("E", rs.getString(5));
                        datanum.put("F", rs.getString(6));
                        prolist.add(datanum);
                    }
                    isSuccess = true;
                    z = "Bağlantı başarılı.";
                }
            } catch (Exception ex) {
                z = "Veri alışverişi yapılamadı.";
                pbr.setVisibility(View.GONE);
            }
            return z;
        }
    }
    private void DateSet(){
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newDate.setFirstDayOfWeek(Calendar.MONDAY);
                searchView.setQuery(dateFormat.format(newDate.getTime()),true);
                chiptext.setText(dateFormat.format(newDate.getTime()));
                chipcon.setVisibility(View.VISIBLE);
                //searchView.setIconified(true);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    public void VoiceSearch(Intent in){
           if (in != null && ACTION_VOICE_SEARCH.equals(in.getAction())) {
        try {
            String query = in.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, true);}
        catch (Exception e) {
            String m = e.getMessage();}}
        }
    }




