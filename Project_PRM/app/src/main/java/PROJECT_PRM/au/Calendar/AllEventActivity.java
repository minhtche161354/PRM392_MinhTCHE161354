package PROJECT_PRM.au.Calendar;

import static PROJECT_PRM.au.Calendar.CalendarUtils.daysInWeekArray;
import static PROJECT_PRM.au.Calendar.CalendarUtils.monthYearFromDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AllEventActivity extends AppCompatActivity {

    private ListView DoneEventListView , TodoEventListView;

    private Button DEL,Edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_list_event);
        Binding();
        setEventAdpater();
//        eventAdapter = new EventAdapter(this, eventList);
//        eventListView.setAdapter(eventAdapter);

        //tao foreground service
//        Intent serviceIntent = new Intent(this, ForegroundService.class);
//        ContextCompat.startForegroundService(this, serviceIntent);

        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        break;
                    case R.id.setting:
                        startActivity(new Intent(AllEventActivity.this, SettingActivity.class));
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
        //calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        TodoEventListView = findViewById(R.id.TodoEventListView);
        DEL = findViewById(R.id.DELbutton);
        Edit = findViewById(R.id.DELbutton);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.todoEvents(LocalDate.now());
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        TodoEventListView.setAdapter(eventAdapter);
        eventAdapter.setDataChange(new EventAdapter.DataChange() {
            @Override
            public void onEventDeleted() {
                TodoEventListView.setAdapter(eventAdapter);
            }

            @Override
            public void onEventUpdated() {
                TodoEventListView.setAdapter(eventAdapter);
            }
        });
    }






}