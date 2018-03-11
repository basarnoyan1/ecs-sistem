package noyansoft.ecssistem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static android.content.Context.MODE_PRIVATE;

public class ConnectionClass {
    public static String ip = "192.168.0.1";//192.168.0.1 ecs2017 ecs_rapor Parola03!
    public static String classs = "net.sourceforge.jtds.jdbc.Driver";
    public static String db = "ecs2017";
    public static String un = "ecs_rapor";
    public static String password = "Parola03!";
    SharedPreferences sharedPref;
    @SuppressLint("NewApi")
    public Connection CONN(Context mContext) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedPref = mContext.getSharedPreferences("SQLPref", MODE_PRIVATE);
        kayıt_kontrol();
        Connection conn = null;
        String ConnURL = null;
        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
            Log.w("Connection URL",ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
    public void kayıt_kontrol(){
        String ipa = sharedPref.getString("ipadres",null);
        String dba = sharedPref.getString("database",null);
        String usern = sharedPref.getString("username",null);
        String passw = sharedPref.getString("password",null);
        Log.e("Kayıt-b",ipa);
        Log.e("Kayıt-b",dba);
        Log.e("Kayıt-b",usern);
        Log.e("Kayıt-b",passw);
        if(!TextUtils.isEmpty(ipa) && !TextUtils.isEmpty(db) &&
                !TextUtils.isEmpty(usern) &&!TextUtils.isEmpty(passw)){
            ip=ipa;
            db = dba;
            un = usern;
            password = passw;
        }
        else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("ipadres",ip);
            editor.putString("database",db);
            editor.putString("username",un);
            editor.putString("password",password);
        }
        }

    }

