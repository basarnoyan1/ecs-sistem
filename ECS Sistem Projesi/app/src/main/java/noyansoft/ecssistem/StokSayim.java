package noyansoft.ecssistem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class StokSayim extends AppCompatActivity {
    Random r = new Random();
    ConnectionClass conc = new ConnectionClass();
    QrReader qrReader = new QrReader();
    String qrcode = qrReader.qrcode;
    Boolean swbqr = qrReader.swbcheckqr;
    int spin = qrReader.spin;
    String descqr = qrReader.descqr;
    ProgressBar pBar;
    Switch sw1;
    CardView crd;
    String stokkodu,serikodu,depo1,depo2,depo3,depo101,desc,depo1_2,depo2_2,depo3_2,depo101_2;
    int depo=1;
    Boolean swb = false;
    private DatePickerDialog datepicker;
    Boolean st;
    EditText edtskod1,edtstokno,edtmiktar,edtacik,edtacik2,edtdepo,edtserino,edttarih,edtdepo2;
    Boolean perm = false;
    Spinner dspin1,dspin2,dspin3;
    Button ara,qrcbt,ekle;
    String mselection;
    SimpleDateFormat dateFormat;
    private static final String[]paths = {"Depo 1","Depo 2", "Depo 3","Depo 101"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_sayim);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolsayim);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.consayim);
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
        ara = (Button) findViewById(R.id.arabutton);
        qrcbt = (Button) findViewById(R.id.qrread);
        crd = (CardView) findViewById(R.id.cardView3);
        ekle = (Button) findViewById(R.id.btadd);
        edtskod1 = (EditText) findViewById(R.id.sskod);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        edtstokno = (EditText) findViewById(R.id.stokno_et);
        edtmiktar = (EditText) findViewById(R.id.miktar_et);
        edttarih = (EditText) findViewById(R.id.tarih_et);
        edtacik = (EditText) findViewById(R.id.aciklama_et1);
        edtacik2 = (EditText) findViewById(R.id.aciklama_et2);
        edtdepo = (EditText) findViewById(R.id.depomiktar_et);
        edtdepo2 = (EditText) findViewById(R.id.depomiktar_et2);
        edtserino = (EditText) findViewById(R.id.serino_et);
            dspin1 = (Spinner)findViewById(R.id.depokodu1);
            dspin2 = (Spinner)findViewById(R.id.depokodu2);
            dspin3 = (Spinner)findViewById(R.id.miktarspin);
            sw1 = (Switch) findViewById(R.id.stokserid);
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            if(qrReader.date == null) {
            edttarih.setText(dateFormat.format(new Date()));
        }
        else{
            edttarih.setText(qrReader.date);
        }
        DateSet();
        if(swbqr)
        {sw1.setChecked(true);
        sw1.setText("Stok Numarası     ");
        swb = true;
        crd.setVisibility(View.GONE);}
        if(qrReader.descqr != null){edtacik.setText(descqr);}
        if(qrcode != null)
        {edtskod1.setText(qrcode);
        Log.e("Barkod",qrcode);
        st=true;
            DoSearch doSearch = new DoSearch();
            doSearch.execute("");
            qrcode=null;
            qrReader.qrcode = qrcode;
        }else {}
        qrcbt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrReader.descqr = edtacik.getText().toString();
                PermissionControl();
            }
        });
        ara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                st = true;
                DoSearch doSearch = new DoSearch();
                doSearch.execute("");
            }
        });
        ekle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                st = true;
                DoSearch doSearch = new DoSearch();
                doSearch.execute("");
                st=false;
                DoSearch doSearch2 = new DoSearch();
                doSearch2.execute("");
            }
        });
        edttarih.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datepicker.show();
            }
        });
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                swb = bChecked;
                if (bChecked) {
                    sw1.setText("Stok Numarası     ");
                    qrReader.swbcheckqr = true;
                    crd.setVisibility(View.GONE);
                } else {
                    sw1.setText("Seri Numarası     ");
                    qrReader.swbcheckqr = false;
                    crd.setVisibility(View.VISIBLE);
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StokSayim.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dspin1.setAdapter(adapter);
        dspin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                mselection=dspin1.getSelectedItem().toString();
                switch (mselection) {
                    case "Depo 1":
                        depo = 1;
                        qrReader.spin = 0;
                        break;
                    case "Depo 2":
                        depo = 2;
                        qrReader.spin = 1;
                        break;
                    case "Depo 3":
                        depo = 3;
                        qrReader.spin = 2;
                        break;
                    case "Depo 101":
                        depo = 101;
                        qrReader.spin = 3;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        dspin1.setSelection(qrReader.spin);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(StokSayim.this,
                android.R.layout.simple_spinner_item,paths);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dspin2.setAdapter(adapter2);
        dspin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String  mselection=dspin2.getSelectedItem().toString();
                switch (mselection) {
                    case "Depo 1":
                        edtdepo.setText(depo1);
                        break;
                    case "Depo 2":
                        edtdepo.setText(depo2);
                        break;
                    case "Depo 3":
                        edtdepo.setText(depo3);
                        break;
                    case "Depo 101":
                        edtdepo.setText(depo101);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(StokSayim.this,
                android.R.layout.simple_spinner_item,paths);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dspin3.setAdapter(adapter3);
        dspin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String mselection=dspin3.getSelectedItem().toString();
                switch (mselection) {
                    case "Depo 1":
                        edtdepo2.setText(depo1_2);
                        break;
                    case "Depo 2":
                        edtdepo2.setText(depo2_2);
                        break;
                    case "Depo 3":
                        edtdepo2.setText(depo3_2);
                        break;
                    case "Depo 101":
                        edtdepo2.setText(depo101_2);
                        break;
                    default:
                        edtdepo2.setText(depo1_2);
                        break;

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    public class DoSearch extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false,chnul = false;
        String miktar = edtmiktar.getText().toString();
        String aciklama = edtacik.getText().toString();
        String edkod = edtskod1.getText().toString();
        String tarih = edttarih.getText().toString();
        @Override
        protected void onPreExecute() {
            pBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pBar.setVisibility(View.GONE);
            Snackbar.make(ara,r,Snackbar.LENGTH_SHORT).show();
            if(isSuccess) {
                edtstokno.setText(stokkodu);
                edtserino.setText(serikodu);
                dspin2.setClickable(true);

                switch (dspin2.getSelectedItem().toString()) {
                    case "Depo 1":
                        edtdepo.setText(depo1);
                        break;
                    case "Depo 2":
                        edtdepo.setText(depo2);
                        break;
                    case "Depo 3":
                        edtdepo.setText(depo3);
                        break;
                    case "Depo 101":
                        edtdepo.setText(depo101);
                        break;
                }

                switch (dspin3.getSelectedItem().toString()) {
                        case "Depo 1":
                            edtdepo2.setText(depo1_2);
                            break;
                        case "Depo 2":
                            edtdepo2.setText(depo2_2);
                            break;
                        case "Depo 3":
                            edtdepo2.setText(depo3_2);
                            break;
                        case "Depo 101":
                            edtdepo2.setText(depo101_2);
                            break;
                }

                if(chnul){
                    edtmiktar.setText("");
                    edtskod1.setText("");
                }

                edtacik2.setText(desc);
                Log.w("Task","Task completed.");
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if(edkod.trim().equals(""))
                if(swb) {
                    z = "Lütfen stok numarası giriniz.";
                }
                else{
                    z = "Lütfen seri numarası giriniz.";
                }
            else
            {
                try {
                    Connection con = conc.CONN(StokSayim.this);
                    if (con == null) {
                        z = "SQL sunucuya bağlanılamadı.";
                    } else {
                        String query = "select " +
                                "t.STOK_KODU, "+
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,1) as DEPO_1, " +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,2) as DEPO_2, " +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,3) as DEPO_3," +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,101) as DEPO_101," +
                                "t.STOK_ADI, " +
                                "dbo.ECS_DEPO_STOK_DURUMU_SERI("+edkod+",1) as SERI_DEPO_1,"+
                                "dbo.ECS_DEPO_STOK_DURUMU_SERI("+edkod+",2) as SERI_DEPO_2,"+
                                "dbo.ECS_DEPO_STOK_DURUMU_SERI("+edkod+",3) as SERI_DEPO_3,"+
                                "dbo.ECS_DEPO_STOK_DURUMU_SERI("+edkod+",101) as SERI_DEPO_101 "+
                                "from TBLSTSABIT as t " +
                                "LEFT JOIN TBLCASABIT AS c ON c.CARI_KOD = t.SATICI_KODU " +
                                "left join TBLCARISTOK as ct ON ct.STOK_KODU=t.STOK_KODU AND DAGITIM_ORANI=100 " +
                                "LEFT JOIN TBLCASABIT AS c3 ON c3.CARI_KOD = ct.CARI_KOD " +
                                "LEFT join TBLSTGRUP as gr on gr.GRUP_KOD=t.GRUP_KODU " +
                                "LEFT join TBLSTOKKOD1 as kod1 on kod1.GRUP_KOD=t.KOD_1 " +
                                "LEFT join TBLSTOKKOD2 as kod2 on kod2.GRUP_KOD=t.KOD_2 ";
                        String query_1 = "where t.STOK_KODU='"+edkod+"'";
                        String query_2= "where t.STOK_KODU=(SELECT top 1 STOK_KODU  FROM TBLSERITRA where SERI_NO='"+edkod+"')";
                        String query2;
                        String query2_1 = "INSERT INTO ECS_YENI_SAYIM(SUBE_KODU,CEVRIM, [STOK_KODU],[SERI_NO],"+
                                "[DEPO_KODU],[MIKTAR],[ACIKLAMA],[TARIH],[SAYIM_FISNO])" +
                                "VALUES(0,0,'"+edkod+"','',"+depo+","+
                                miktar+",'"+aciklama+"',(Convert(VARCHAR(30),'"+tarih+"',104)),"+
                                "'001')";
                        String query2_2 = "INSERT INTO ECS_YENI_SAYIM(SUBE_KODU,CEVRIM, [STOK_KODU],[SERI_NO]," +
                                "[DEPO_KODU],[MIKTAR],[ACIKLAMA],[TARIH],[SAYIM_FISNO])" +
                                "VALUES(0,0,'"+stokkodu+"','"+edkod+"',"+depo+","+
                                miktar+",'"+aciklama+"',(Convert(VARCHAR(30),'"+tarih+"',104))," +
                                "'001');";
                        if(st) {
                            if(swb){query += query_1;}
                            else {query += query_2;
                            serikodu=edkod;}
                            Log.d("Query", query);
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                z = "Kayıt bulundu.";
                                stokkodu = rs.getString(1);
                                depo1 = rs.getString(2);
                                depo2 = rs.getString(3);
                                depo3 = rs.getString(4);
                                depo101 = rs.getString(5);
                                desc = rs.getString(6);
                                depo1_2 = rs.getString(7);
                                depo2_2 = rs.getString(8);
                                depo3_2 = rs.getString(9);
                                depo101_2 = rs.getString(10);
                                //serikodu = rs.getString(7);
                                isSuccess = true;
                            } else {
                                z = "Kayıt bulunamadı.";
                                isSuccess = false;
                            }
                        }
                        else{
                            if(!miktar.equals("")) {
                                if (swb) {
                                    query2 = query2_1;
                                } else {
                                    query2 = query2_2;
                                }
                                Log.d("Query", query2);
                                try {
                                    PreparedStatement stmt = con.prepareStatement(query2);
                                    stmt.executeUpdate();
                                    z = "Kayıt eklendi.";
                                    chnul = true;
                                    isSuccess = true;
                                } catch (Exception e) {
                                    z = "Kayıt eklenemedi.";
                                    isSuccess = false;
                                }
                            }else{
                                z= "Miktar bilgisi giriniz.";
                                isSuccess = false;
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Hata";
                    Log.e("Hata",ex.getMessage());
                }
            }
            return z;
        }
    }
    public void PermissionControl(){
        int vercode = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(vercode<22){
            int permission = PermissionChecker.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                perm=true;
                Intent iy = new Intent(StokSayim.this, QrReader.class);
                startActivity(iy);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Lütfen ayarlardan uygulama izinlerini kontrol ediniz.")
                        .setTitle("Uyarı");
                AlertDialog dialog = builder.create();
            }
        }
        else{
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        1);
            }
            else {perm=true;
                Intent iy = new Intent(StokSayim.this, QrReader.class);
                startActivity(iy);}
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    perm = true;
                    Intent iy = new Intent(StokSayim.this, QrReader.class);
                    startActivity(iy);
                } else {
                    Snackbar.make(ara,"İzinlerinizi kontrol ediniz.",Snackbar.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void DateSet(){
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.setFirstDayOfWeek(Calendar.MONDAY);
                newDate.set(year, monthOfYear, dayOfMonth);
                edttarih.setText(dateFormat.format(newDate.getTime()));
                qrReader.date = dateFormat.format(newDate.getTime());
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act3, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                Intent il = new Intent(StokSayim.this,SayimListesi.class);
                startActivity(il);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
