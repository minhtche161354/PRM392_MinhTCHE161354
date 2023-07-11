package PROJECT_PRM.au.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;

import static PROJECT_PRM.au.Calendar.CalendarUtils.daysInMonthArray;
import static PROJECT_PRM.au.Calendar.CalendarUtils.monthYearFromDate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    DBOpenHelper db;
    private int NOTIFY_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        //lấy data từ database
        db= new DBOpenHelper(MainActivity.this);
        storeDataInArray();

       // tao permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
            if(ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED){
                requestNotificationPermission();
            }
        }

        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.allListSchedule:
                        Toast.makeText(MainActivity.this, "Hiện list all schedule", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.dayListSchedule:
                        Toast.makeText(MainActivity.this, "Hiện day schedule", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void requestNotificationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.FOREGROUND_SERVICE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for the app to work")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.FOREGROUND_SERVICE}, NOTIFY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.FOREGROUND_SERVICE}, NOTIFY_PERMISSION_CODE);
        }
    }

    // Ket qua chon sau khi hoi permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == NOTIFY_PERMISSION_CODE){
            if(grantResults.length > 0 & grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void storeDataInArray(){
        Cursor cursor= db.readEvents();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No events", Toast.LENGTH_SHORT).show();
        }else{
            if(Event.eventsList.isEmpty()){
                while (cursor.moveToNext()){

                    Event newEvent = new Event(cursor.getString(1), LocalDate.parse(cursor.getString(4)), LocalTime.parse(cursor.getString(3)));
                    Event.eventsList.add(newEvent);
                }
            }else{
                Event.eventsList.clear();
            }
        }
    }


    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    public void previousMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        if(date != null)
        {
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    public void weeklyAction(View view)
    {
        startActivity(new Intent(this, WeekViewActivity.class));
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

}








