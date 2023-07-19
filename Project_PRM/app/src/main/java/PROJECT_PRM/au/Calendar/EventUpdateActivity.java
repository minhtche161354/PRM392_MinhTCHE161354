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
    private EditText etEventName;
    private TextView tvEventDate, tvEventTime;

    private Button TimeEditBtn;
    int holdHour,holdMinute;

    private String holdChangedTime;
    private String title0, date0, time0;

    private LocalTime time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

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
        TimeEditBtn = findViewById(R.id.TimeEditBtn);
        etEventName= findViewById(R.id.eventNameET);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTime = findViewById(R.id.tvEventTime);
    }

    public void getAndSetIntentData(){
        if(getIntent().hasExtra("title") && getIntent().hasExtra("date") && getIntent().hasExtra("time")){
            title0= getIntent().getStringExtra("title");
            date0= getIntent().getStringExtra("date");
            time0= getIntent().getStringExtra("time");

            etEventName.setText(title0);
            tvEventDate.setText("Date: " + CalendarUtils.formattedDate(LocalDate.parse(date0)));
            tvEventTime.setText("Time: " + CalendarUtils.formattedTime(LocalTime.parse(time0)));
        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEventAction(View view)
    {
        DBOpenHelper mydb= new DBOpenHelper(this);
        String eventName = etEventName.getText().toString();
        Event selectedEvent= Event.getEvent(title0, LocalDate.parse(date0), LocalTime.parse(time0));
        selectedEvent.setName(eventName);
        selectedEvent.setDate(LocalDate.parse(tvEventDate.getText().toString().substring(6), DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        selectedEvent.setTime(LocalTime.parse(tvEventTime.getText().toString().substring(6), DateTimeFormatter.ofPattern("hh:mm:ss a")));


        //Cần thêm sqlite để save vô file
        mydb.updateEvent(title0, time0, date0, eventName, null, tvEventTime.getText().toString(), tvEventDate.getText().toString(), null);
        finish();
    }
    public void deleteEventAction(View view){
        DBOpenHelper mydb= new DBOpenHelper(this);
        if(!Event.eventsList.isEmpty()){
            for(int i=0;i<Event.eventsList.size();i++){
                Event hold = Event.eventsList.get(i);
                Event holder = Event.getEvent(title0, LocalDate.parse(date0), LocalTime.parse(time0));
                if(hold.getName().equals(holder.getName())
                        &&hold.getTime().equals(holder.getTime())
                        &&hold.getDate().equals(holder.getDate())){
                    Event.eventsList.remove(i);
                    mydb.deleteEvent(hold.getName(), hold.getTime().toString(), hold.getDate().toString());
                    break;
                }
            }
        }
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
                    tvEventTime.setText("Time: " +"00"+CalendarUtils.formattedTime(time).substring(2));
                }
                else {
                    tvEventTime.setText("Time: " + CalendarUtils.formattedTime(time));
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