package PROJECT_PRM.au.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventUpdateActivity extends AppCompatActivity
{
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private Button EditTimeButton;
    int holdHour,holdMinute;

    private String holdChangedTime;
    private String title0, date0, time0;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        Binding();
        getAndSetIntentData();


        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        break;
                    case R.id.dayListSchedule:
                        Toast.makeText(EventUpdateActivity.this, "Hiện day schedule", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        startActivity(new Intent(EventUpdateActivity.this, SettingActivity.class));
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

    public void getAndSetIntentData(){
        if(getIntent().hasExtra("title") && getIntent().hasExtra("date") && getIntent().hasExtra("time")){
            title0= getIntent().getStringExtra("title");
            date0= getIntent().getStringExtra("date");
            time0= getIntent().getStringExtra("time");

            eventNameET.setText(title0);
            eventDateTV.setText("Date: " + CalendarUtils.formattedDate(LocalDate.parse(date0)));
            eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(LocalTime.parse(time0)));
        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEventAction(View view)
    {
        DBOpenHelper mydb= new DBOpenHelper(this);
        String eventName = eventNameET.getText().toString();
        Event selectedEvent= Event.getEvent(title0, LocalDate.parse(date0), LocalTime.parse(time0));
        selectedEvent.setName(eventName);
        selectedEvent.setDate(LocalDate.parse(eventDateTV.getText().toString()));
        selectedEvent.setTime(LocalTime.parse(eventTimeTV.getText().toString()));

        //save xong tao thong bao
        LocalDateTime selectedDateTime = LocalDateTime.of(CalendarUtils.selectedDate, LocalTime.parse(eventTimeTV.getText().toString()));
        LocalDateTime currentDateTime = LocalDateTime.now();

        long notificationTimeMillis = Duration.between(currentDateTime, selectedDateTime).toMillis();
        scheduleNotification(notificationTimeMillis, eventName, Event.eventsList.size() - 1);

        //Cần thêm sqlite để save vô file
        mydb.updateEvent(title0, time0, date0, eventName, null, eventTimeTV.getText().toString(), eventDateTV.getText().toString(), null);
        finish();
    }

    public void popTimePick(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                holdHour = hour;
                holdMinute = minute;
                time = LocalTime.of(hour,minute);
                if(holdHour==0){
                    eventTimeTV.setText("Time: " +"00"+CalendarUtils.formattedTime(time).substring(2));
                }
                else {
                    eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
                }
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style,onTimeSetListener, holdHour,holdMinute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // tao thong bao
    private void scheduleNotification(long notificationTimeMillis, String eventName, int notificationId) {
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        notificationIntent.putExtra("eventName", eventName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //notificationId, notificationIntent, PendingIntent.FLAG_MUTABLE |PendingIntent.FLAG_MUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + notificationTimeMillis, pendingIntent);
    }
}