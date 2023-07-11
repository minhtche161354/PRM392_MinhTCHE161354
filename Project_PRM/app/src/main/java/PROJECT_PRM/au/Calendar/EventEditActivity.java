package PROJECT_PRM.au.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventEditActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private Button EditTimeButton;
    int holdHour,holdMinute;

    private String holdChangedTime;
    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        Binding();
        time = LocalTime.now();
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(EventEditActivity.this, MainActivity.class));
                        break;
                    case R.id.allListSchedule:
                        Toast.makeText(EventEditActivity.this, "Hiện list all schedule", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.dayListSchedule:
                        Toast.makeText(EventEditActivity.this, "Hiện day schedule", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void Binding()
    {
        EditTimeButton = findViewById(R.id.TimeEditButton);
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view)
    {
        DBOpenHelper mydb= new DBOpenHelper(EventEditActivity.this);
        String eventName = eventNameET.getText().toString();
        Event newEvent= new Event(eventName, CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);

        //save xong tao thong bao
        LocalDateTime localDateTime = LocalDateTime.of(CalendarUtils.selectedDate, time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timeNotify = localDateTime.format(formatter);
        makeNotification(timeNotify, eventName);

        //Cần thêm sqlite để save vô file
        mydb.saveEvent(eventName, null, time.toString(), CalendarUtils.selectedDate.toString(), null);
        finish();
    }

    public void popTimePick(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                holdHour = hour;
                holdMinute = minute;
                time = LocalTime.of(hour,minute);
                eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style,onTimeSetListener, holdHour,holdMinute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // tao thong bao
    public void makeNotification(String title, String text){
        String channelID = "CHANNEL_ID_NOTFICATION";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                channelID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if(notificationChannel == null){
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }
}