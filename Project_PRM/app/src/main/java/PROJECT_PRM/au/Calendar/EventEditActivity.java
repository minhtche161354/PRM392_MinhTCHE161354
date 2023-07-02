package PROJECT_PRM.au.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;

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
            String eventName = eventNameET.getText().toString();
            Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
            Event.eventsList.add(newEvent);
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
}