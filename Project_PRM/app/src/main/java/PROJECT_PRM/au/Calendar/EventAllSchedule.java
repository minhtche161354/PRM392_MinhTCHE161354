package PROJECT_PRM.au.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EventAllSchedule extends AppCompatActivity {
    private ArrayList<Event> allSchedule = new ArrayList<>();
    private ListView listView;
    private Button previousPage, nextPage;
    private Pagination pagination;
    private int lastPage, currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_schedule);

        listView = findViewById(R.id.eventListViewAll);
        previousPage = findViewById(R.id.previousPage);
        nextPage = findViewById(R.id.nextPage);

        allSchedule = Event.getAllEventList();
        pagination = new Pagination(7, allSchedule);
        lastPage = pagination.getLastPage();
        updateData(currentPage);

//        updateButton();


        BottomNavigationView actionBar = findViewById(R.id.action_bar);
        actionBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        finish();
                        break;
                    case R.id.dayListSchedule:

                        break;
                    case R.id.setting:
                        finish();
                        startActivity(new Intent(EventAllSchedule.this, SettingActivity.class));
                        break;
                }
            }
        });


        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                updateData(currentPage);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                updateData(currentPage);
            }
        });
    }

    private void updateButton() {
        if(currentPage == 0){
            previousPage.setEnabled(false);
            nextPage.setEnabled(true);
        }
        else if(currentPage == lastPage){
            previousPage.setEnabled(true);
            nextPage.setEnabled(false);
        }
        else{
            previousPage.setEnabled(true);
            nextPage.setEnabled(true);
        }
    }

    private void updateData(int currentPage){
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pagination.generateData(currentPage));
        listView.setAdapter(adapter);

        updateButton();
    }
}
