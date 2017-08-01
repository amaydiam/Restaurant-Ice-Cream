package com.ad.restauranticecream.service;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.ad.restauranticecream.R;
import com.ad.restauranticecream.RestaurantIceCream;
import com.ad.restauranticecream.model.Refresh;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by binderbyte on 11/01/17.
 */
public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

    private MediaPlayer player;

    @Override
    public void notificationReceived(final OSNotification oSNotification) {
        //action

        EventBus.getDefault().postSticky(new Refresh(true));
        StopSound();
        PlaySound();
    }


    void PlaySound() {
        Uri alert = Uri.parse("android.resource://" + RestaurantIceCream.getContext().getPackageName() + "/" + R.raw.bell);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            if (alert == null) {
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // I can't see this ever being null (as always have a default notification)
                // but just incase
                if (alert == null) {
                    // alert backup is null, using 2nd backup
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
        }
        //   show();

        player = MediaPlayer.create(RestaurantIceCream.getContext(), alert);
        player.setLooping(false);
        player.start();
    }


    void StopSound() {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }
}


