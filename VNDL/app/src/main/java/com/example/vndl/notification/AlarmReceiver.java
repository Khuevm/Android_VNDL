package com.example.vndl.notification;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.vndl.HomeActivity;
import com.example.vndl.MainActivity;
import com.example.vndl.R;
import com.example.vndl.database.DBHandler;
import com.example.vndl.model.Question;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;


public class AlarmReceiver extends BroadcastReceiver {
    final String CHANEL_ID = "10001";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("---noti---");
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getTime());
        DBHandler db = new DBHandler(context);
        List<Question> questions = db.getAllQuestion();

        long remainQuestion = questions.stream().filter(new Predicate<Question>() {
            @Override
            public boolean test(Question question) {
                return question.getPractice_answer() == 0;
            }
        }).count();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANEL_ID, "Ôn thi bằng lái A1", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Mieu ta");
            notificationManager.createNotificationChannel(channel);

        }

        Intent intentHome = new Intent(context, HomeActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentHome, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setContentTitle("Bạn vẫn còn " + remainQuestion + " câu hỏi chưa luyện tập.\n Hãy vào luyện tập")
                .setSmallIcon(R.drawable.ic_notifications)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(getNotificationid(), builder.build());

    }

    private int getNotificationid() {
        int time=(int) new Date().getTime();
        return time;
    }

}
