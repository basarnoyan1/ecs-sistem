package noyansoft.ecssistem;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class StokKontrol extends AppCompatActivity {
    Random r = new Random();
    ConnectionClass conc = new ConnectionClass();
    QrReader2 qrReader2 = new QrReader2();
    String qrcode = qrReader2.qrcode;
    Boolean swbqr = qrReader2.swbcheckqr;
    Boolean swb = false;
    Switch sw1;
    ProgressBar pBar;
    CardView crd;
    Spinner spn;
    EditText edtskod1,edtstokno,edtstokadi,edtsatno,edtsatadi,edtcarino,edtcariadi,
            edtgrupadi,edtkod1,edtkod2,edtdepo1,edtdepo2,edtdepo3,edtdepo101,edtdepomik;
    String stokkodu,stokadi,saticikodu,saticiadi,carikodu,cariadi,
            grupadi,kod1,kod2,depo1,depo2,depo3,depo101,mSelection;
    String depo1_2,depo2_2,depo3_2,depo101_2;
    Boolean perm=false;
    Button ara,qrcbt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";
    private static final String[]paths = {"Depo 1","Depo 2", "Depo 3","Depo 101"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok_kontrol);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolkontrol);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        ConstraintLayout cons = (ConstraintLayout) findViewById(R.id.conkontrol);
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
        sw1 = (Switch) findViewById(R.id.stokserid);
        spn = (Spinner) findViewById(R.id.spinner2);
        crd = (CardView) findViewById(R.id.cardView10);
        edtskod1 = (EditText) findViewById(R.id.sskod);
        pBar = (ProgressBar) findViewById(R.id.progressBar2);
        edtstokno = (EditText) findViewById(R.id.stokno_et);
        edtstokno.setTextIsSelectable(true);
        edtstokadi = (EditText) findViewById(R.id.stokadi_et);
        edtstokadi.setTextIsSelectable(true);
        edtsatno = (EditText) findViewById(R.id.saticino_et);
        edtsatno.setTextIsSelectable(true);
        edtsatadi = (EditText) findViewById(R.id.saticiadi_et);
        edtsatadi.setTextIsSelectable(true);
        edtcarino = (EditText) findViewById(R.id.carikodu_et);
        edtcarino.setTextIsSelectable(true);
        edtcariadi = (EditText) findViewById(R.id.cariadi_et);
        edtcariadi.setTextIsSelectable(true);
        edtgrupadi = (EditText) findViewById(R.id.grupadi_et);
        edtgrupadi.setTextIsSelectable(true);
        edtkod1 = (EditText) findViewById(R.id.kod1_et);
        edtkod1.setTextIsSelectable(true);
        edtkod2 = (EditText) findViewById(R.id.kod2_et);
        edtkod2.setTextIsSelectable(true);
        edtdepo1 = (EditText) findViewById(R.id.depo1_et);
        edtdepo1.setTextIsSelectable(true);
        edtdepo2 = (EditText) findViewById(R.id.depo2_et);
        edtdepo2.setTextIsSelectable(true);
        edtdepo3 = (EditText) findViewById(R.id.depo3_et);
        edtdepo3.setTextIsSelectable(true);
        edtdepo101 = (EditText) findViewById(R.id.depo101_et);
        edtdepo101.setTextIsSelectable(true);
        edtdepomik = (EditText) findViewById(R.id.depomik_et);
        edtdepomik.setTextIsSelectable(true);
        ara = (Button) findViewById(R.id.arabutton);
        qrcbt = (Button) findViewById(R.id.qrread);
        if(swbqr)
        {sw1.setChecked(true);
            sw1.setText("Seri Numarası     ");
            swb = true;
        crd.setVisibility(View.VISIBLE);}

        if(qrcode != null)
        {edtskod1.setText(qrcode); Log.e("Barkod",qrcode);
            DoSearch doSearch = new DoSearch();
            doSearch.execute("");
            qrcode=null;
            qrReader2.qrcode = qrcode;
        }else {}
        qrcbt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PermissionControl();
            }
        });
        ara.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DoSearch doSearch = new DoSearch();
                doSearch.execute("");
            }
        });
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                swb = bChecked;
                if (bChecked) {
                    sw1.setText("Seri Numarası     ");
                    qrReader2.swbcheckqr = true;
                    crd.setVisibility(View.VISIBLE);
                } else {
                    sw1.setText("Stok Numarası     ");
                    qrReader2.swbcheckqr = false;
                    crd.setVisibility(View.GONE);
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StokKontrol.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                mSelection=spn.getSelectedItem().toString();
                switch (mSelection) {
                    case "Depo 1":
                        edtdepomik.setText(depo1_2);
                        break;
                    case "Depo 2":
                        edtdepomik.setText(depo2_2);
                        break;
                    case "Depo 3":
                        edtdepomik.setText(depo3_2);
                        break;
                    case "Depo 101":
                        edtdepomik.setText(depo101_2);
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
        Boolean isSuccess = false;
        String edkod = edtskod1.getText().toString();
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
                edtstokadi.setText(stokadi);
                edtsatno.setText(saticikodu);
                edtsatadi.setText(saticiadi);
                edtcarino.setText(carikodu);
                edtcariadi.setText(cariadi);
                edtgrupadi.setText(grupadi);
                edtkod1.setText(kod1);
                edtkod2.setText(kod2);
                edtdepo1.setText(depo1);
                edtdepo2.setText(depo2);
                edtdepo3.setText(depo3);
                edtdepo101.setText(depo101);
                switch (spn.getSelectedItem().toString()) {
                    case "Depo 1":
                        edtdepomik.setText(depo1_2);
                        break;
                    case "Depo 2":
                        edtdepomik.setText(depo2_2);
                        break;
                    case "Depo 3":
                        edtdepomik.setText(depo3_2);
                        break;
                    case "Depo 101":
                        edtdepomik.setText(depo101_2);
                        break;
                    default:
                        edtdepomik.setText(depo1_2);
                        break;
                }
                Log.w("Task","Task completed.");
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if(edkod.trim().equals(""))
                if(swb) {
                    z = "Lütfen seri numarası giriniz.";
                }
                else{
                    z = "Lütfen stok numarası giriniz.";
                }
            else
            {
                try {
                    Connection con = conc.CONN(StokKontrol.this);
                    if (con == null) {
                        z = "SQL sunucuya bağlanılamadı.";
                    } else {
                        String query = "SELECT ProName,ProDesc,Ondate,NewCol FROM Producttbl WHERE Id ='"+edkod+"'";
                        String query2 ="SELECT STOK_ADI FROM TBLSTSABIT WHERE STOK_KODU ='"+edkod+"'";
                        String query3 = "select " +
                                "t.STOK_KODU,t.STOK_ADI,t.SATICI_KODU as STOK_KARTI_SATICI_KODU,dbo.SQL_TO_TRK(c.CARI_ISIM) as STOK_KARTI_SATICI_ADI," +
                                "ct.CARI_KOD as MRP_SATICI_KODU,dbo.SQL_TO_TRK(c3.CARI_ISIM) as MRP_CARI_ADI," +
                                "gr.GRUP_ISIM," +
                                "kod1.GRUP_ISIM as KOD1,kod2.GRUP_ISIM as KOD2," +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,1) as DEPO_1," +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,2) as DEPO_2," +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,3) as DEPO_3," +
                                "dbo.ECS_DEPO_STOK_DURUMU(t.STOK_KODU,101) as DEPO_101, " +
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
                        String query3_1 = "where t.STOK_KODU='"+edkod+"'";
                        String query3_2 = "where t.STOK_KODU=(SELECT top 1 STOK_KODU  FROM TBLSERITRA where SERI_NO='"+edkod+"')";
                        if(swb){
                            query3 += query3_2;
                        }
                        else {
                            query3 += query3_1;
                        }
                        Log.d("Query",query3);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query3);
                        if(rs.next())
                        {
                            z = "Kayıt bulundu.";
                            stokkodu = rs.getString(1);
                            stokadi = rs.getString(2);
                            saticikodu = rs.getString(3);
                            saticiadi = rs.getString(4);
                            carikodu = rs.getString(5);
                            cariadi = rs.getString(6);
                            grupadi = rs.getString(7);
                            kod1 = rs.getString(8);
                            kod2 = rs.getString(9);
                            depo1 = rs.getString(10);
                            depo2 = rs.getString(11);
                            depo3 = rs.getString(12);
                            depo101 = rs.getString(13);
                            depo1_2 = rs.getString(14);
                            depo2_2 = rs.getString(15);
                            depo3_2 = rs.getString(16);
                            depo101_2 = rs.getString(17);
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Kayıt bulunamadı.";
                            isSuccess = false;
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
                Intent iy = new Intent(StokKontrol.this, QrReader2.class);
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
            }            else {perm=true;
                Intent iy = new Intent(StokKontrol.this, QrReader2.class);
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
                    perm=true;
                    Intent iy = new Intent(StokKontrol.this, QrReader2.class);
                    startActivity(iy);
                } else {
                    Snackbar.make(ara,"İzinlerinizi kontrol ediniz.",Snackbar.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act2, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share2:
                String printtext = edtstokno.getText().toString()+","+
                        edtstokadi.getText().toString()+"/"+
                        edtsatno.getText().toString()+","+edtsatadi.getText().toString()+"/"+
                        edtcarino.getText().toString()+","+edtcariadi.getText().toString()+"/"+
                        edtgrupadi.getText().toString()+","+edtkod1.getText().toString()+"/"+
                        edtdepo1.getText().toString()+","+edtdepo2.getText().toString()+"/"+
                        edtdepo3.getText().toString()+","+edtdepo101.getText().toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, printtext);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,"Bilgileri dışa aktar."));
                //IntentPrint("Test");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  /*  public void IntentPrint(String txtvalue)
    {
        byte[] buffer = txtvalue.getBytes();
        byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
        PrintHeader[3]=(byte) buffer.length;
        InitPrinter();
        if(PrintHeader.length>128)
        {
            value+="\nValue is more than 128 size\n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {

                outputStream.write(txtvalue.getBytes());
                outputStream.close();
                socket.close();
            }
            catch(Exception ex)
            {
                value+=ex.toString()+ "\n" +"Excep IntentPrint \n";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void InitPrinter()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("BlueTooth Printer")) //Note, you will need to change this to match the name of your device
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
                Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(bluetoothDevice, 1);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                beginListenForData();
            }
            else
            {
                value+="No Devices found";
                Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        }
    }
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                       handler.post(new Runnable() {
                                            public void run() {
                                                Log.d("e", data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
   public void IntentPrint(String txtvalue)
   {
       byte[] buffer = txtvalue.getBytes();
       byte[] PrintHeader = { (byte) 0xAA, 0x55,2,0 };
       PrintHeader[3]=(byte) buffer.length;
       InitPrinter();
       if(PrintHeader.length>128)
       {
           value+="\nValue is more than 128 size\n";
           Snackbar.make(qrcbt,value,Snackbar.LENGTH_SHORT);
       }
       else
       {
           try
           {
               for(int i=0;i<=PrintHeader.length-1;i++)
               {
                   outputStream.write(PrintHeader[i]);
               }
               for(int i=0;i<=buffer.length-1;i++)
               {
                   outputStream.write(buffer[i]);
               }
               outputStream.close();
               socket.close();
           }
           catch(Exception ex)
           {
               value+=ex.toString()+ "\n" +"Exception IntentPrint \n";
               Toast.makeText(StokKontrol.this,value,Toast.LENGTH_SHORT);
           }
       }
   }
    public void InitPrinter()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0)
            {
                for(BluetoothDevice device : pairedDevices)
                {
                    if(device.getName().equals("BlueTooth Printer"))
                    {
                        bluetoothDevice = device;
                        break;
                    }
                }

                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothAdapter.cancelDiscovery();
                if(bluetoothDevice.getBondState()==2)
                {
                    socket.connect();
                    outputStream = socket.getOutputStream();
                }
                else
                {
                    value+="Device not connected";
                    Snackbar.make(qrcbt,value,Snackbar.LENGTH_SHORT);
                }
            }
            else
            {
                value+="No Devices found";
                Snackbar.make(qrcbt,value,Snackbar.LENGTH_SHORT);
                return;
            }
        }
        catch(Exception ex)
        {
            value+=ex.toString()+ "\n" +" InitPrinter \n";
            Toast.makeText(StokKontrol.this,value,Toast.LENGTH_SHORT);
        }
    }
}
