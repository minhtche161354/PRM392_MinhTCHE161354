package PROJECT_PRM.au.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import static PROJECT_PRM.au.Calendar.CalendarUtils.daysInWeekArray;
import static PROJECT_PRM.au.Calendar.CalendarUtils.monthYearFromDate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    private Button DEL,Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        Binding();
        setWeekView();


        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        break;
                    case R.id.dayListSchedule:
                        startActivity(new Intent(WeekViewActivity.this, AllEventActivity.class));
                        break;
                    case R.id.setting:
                        startActivity(new Intent(WeekViewActivity.this, SettingActivity.class));
                        break;
                }
            }
        });
    }


    private void ActionLmao(){
        DEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEventAdpater();
                //notifyDataSetChanged();
            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEventAdpater();
            }
        });
    }
    private void Binding()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        DEL = findViewById(R.id.DELbutton);
        Edit = findViewById(R.id.DELbutton);
    }

    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
        eventAdapter.setDataChange(new EventAdapter.DataChange() {
            @Override
            public void onEventDeleted() {
                eventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onEventUpdated() {
                eventListView.setAdapter(eventAdapter);
            }
        });
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }


}