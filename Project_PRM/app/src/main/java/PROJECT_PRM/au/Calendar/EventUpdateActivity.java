package PROJECT_PRM.au.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

public class EventUpdateActivity extends AppCompatActivity {

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private Button EditTimeButton;
    int holdHour,holdMinute;

    private String holdChangedTime;
    private LocalTime time;

    private String eventTitle0, eventTime0, eventDate0;

    DBOpenHelper db= new DBOpenHelper(this);

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
        EditTimeButton = findViewById(R.id.TimeEditButton);
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("title") && getIntent().hasExtra("time") && getIntent().hasExtra("date")){
            eventTitle0= getIntent().getStringExtra("title");
            eventTime0= getIntent().getStringExtra("time");
            eventDate0= getIntent().getStringExtra("date");

            eventNameET.setText(eventTitle0);
            eventTimeTV.setText((eventTime0));
            eventDateTV.setText((eventDate0));
        }else{
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateEventAction(View view)
    {
        Event selectedEvent= Event.getEvent(eventTitle0, LocalDate.parse(eventDate0), LocalTime.parse(eventTime0));
        Event.eventsList.remove(selectedEvent);

        String eventName= eventNameET.getText().toString();
        Event updatedEvent = new Event(eventName, LocalDate.parse(eventDateTV.getText()), LocalTime.parse(eventTimeTV.getText()));
        Event.eventsList.add(updatedEvent);

        //save xong tao thong bao
        LocalDateTime selectedDateTime = LocalDateTime.of(CalendarUtils.selectedDate, time);
        LocalDateTime currentDateTime = LocalDateTime.now();

        long notificationTimeMillis = Duration.between(currentDateTime, selectedDateTime).toMillis();
        scheduleNotification(notificationTimeMillis, eventName, Event.eventsList.size() - 1);

        //Cần thêm sqlite để save vô file
        db.updateEvent(eventTitle0, eventTime0, eventDate0,eventName, null, time.toString(), CalendarUtils.selectedDate.toString(), null);
        finish();
    }

    public void deleteEventAction(View view)
    {
        Event selectedEvent= Event.getEvent(eventTitle0, LocalDate.parse(eventDate0), LocalTime.parse(eventTime0));
        Event.eventsList.remove(selectedEvent);

        String eventName= eventNameET.getText().toString();

        //save xong tao thong bao
        LocalDateTime selectedDateTime = LocalDateTime.of(CalendarUtils.selectedDate, time);
        LocalDateTime currentDateTime = LocalDateTime.now();

        long notificationTimeMillis = Duration.between(currentDateTime, selectedDateTime).toMillis();
        scheduleNotification(notificationTimeMillis, eventName, Event.eventsList.size() - 1);

        //Cần thêm sqlite để save vô file
        db.deleteEvent(eventTitle0, eventTime0, eventDate0);
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

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + notificationTimeMillis, pendingIntent);
    }
}
