package noyansoft.ecssistem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.service.quicksettings.TileService;

@SuppressLint("Override")
@TargetApi(Build.VERSION_CODES.O)
public class QuickSettingsService extends TileService{

    @Override
    public void onClick() {

        boolean isCurrentlyLocked = this.isLocked();
        if (!isCurrentlyLocked) {

            Resources resources = getApplication().getResources();
            Intent intent = new Intent(getApplicationContext(),
                    StokKontrol.class);
            startActivity(intent);
        }
    }

}