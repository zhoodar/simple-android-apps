package kg.jedi.jpomodoro;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MediaPlayerService extends Service {
    private MediaPlayer mPlayer;
    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(this, R.raw.notification_sound);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        mPlayer.release();
    }
}
