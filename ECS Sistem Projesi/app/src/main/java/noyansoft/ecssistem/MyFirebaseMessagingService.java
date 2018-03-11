package noyansoft.ecssistem;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
Alerts alerts = new Alerts();
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody());
        if (Locale.getDefault().getLanguage().equals("en")) {
            FirebaseMessaging.getInstance().subscribeToTopic("dev");
            FirebaseMessaging.getInstance().subscribeToTopic("app");
        }
        else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic("dev");
            FirebaseMessaging.getInstance().subscribeToTopic("app");
        }
    }

    private void sendNotification(String messageBody) {
        AudioManager a =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        a.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                a.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo3_n2)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        int vercode = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(vercode>22){
            mBuilder.setColor(getColor(R.color.colorPrimaryDark));}
        if(vercode<25){
            mBuilder.setContentTitle("Sistem Uyarısı");
        }else{
            mBuilder.setSubText("Sistem Uyarısı");
        }
        alerts.alert.add(messageBody);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());
    }
}