package noyansoft.ecssistem;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
public class QrReader extends AppCompatActivity {
    public static String qrcode;
    TextView barcodeInfo;
    public static Boolean swbcheckqr = false;
    public static String descqr = null;
    public static int spin = 0;
    public static String date = null;
    Camera cam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreader);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarqr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        try{
        cam = Camera.open(0);
        Camera.Parameters param = cam.getParameters();
     //   param.setFocusMode(Camera.Parameters.SCENE_MODE_BARCODE);
        param.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        cam.setParameters(param);
        cam.startPreview();}
        catch(Exception e){
            String a = e.getMessage();
        }
        barcodeInfo = (TextView)findViewById(R.id.code_info);
        final SurfaceView cameraView = (SurfaceView)findViewById(R.id.camera_view);
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE | Barcode.EAN_8 | Barcode.EAN_13 | Barcode.CODE_128|Barcode.UPC_A|Barcode.UPC_E|
                        Barcode.CODE_39|Barcode.CODE_93|Barcode.ITF|Barcode.ISBN|Barcode.CODABAR|Barcode.DATA_MATRIX).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                 .setAutoFocusEnabled(true)      .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {@Override public void surfaceCreated(SurfaceHolder holder) {
            try {cameraSource.start(cameraView.getHolder());
            } catch (IOException ie) {
                Log.e("CAMERA SOURCE", ie.getMessage());
                Toast.makeText(QrReader.this,"Check your app permissions.",Toast.LENGTH_SHORT).show();
            }}
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }});
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override public void release() {}
            @Override public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {
                        public void run() {barcodeInfo.setText(barcodes.valueAt(0).displayValue);
                         try {
                             qrcode = barcodes.valueAt(0).displayValue;
                             cameraSource.stop();
                             Log.e("Barkod", qrcode);
                             Intent i = new Intent(QrReader.this, StokSayim.class);
                             startActivity(i);
                             finish();
                         }
                         catch (Exception e){
                             Snackbar.make(cameraView,e.getMessage(),Snackbar.LENGTH_SHORT);}
                        }
                    });}
            }});}
}



