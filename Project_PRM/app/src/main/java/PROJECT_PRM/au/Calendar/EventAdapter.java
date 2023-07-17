package PROJECT_PRM.au.Calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event>
{
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
//        Button editButtonCellTV = convertView.findViewById(R.id.EDITbutton);
//        Button deleteButtonCellTV = convertView.findViewById(R.id.DELbutton);
        String eventTitle = event.getName() +" "+ CalendarUtils.formattedTime(event.getTime());
        eventCellTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext().getApplicationContext(), EventUpdateActivity.class);
                intent.putExtra("title", event.getName());
                intent.putExtra("date", event.getDate().toString());
                intent.putExtra("time", event.getTime().toString());
                getContext().startActivity(intent);
            }
        });
//        editButtonCellTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!Event.eventsList.isEmpty()){
//
//                }
//            }
//        });
//        deleteButtonCellTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!Event.eventsList.isEmpty()){
//                    for(int i=0;i<Event.eventsList.size();i++){
//                        Event hold = Event.eventsList.get(i);
//                        if(hold.getName().equals(event.getName())
//                        &&hold.getTime().equals(event.getTime())
//                        &&hold.getDate().equals(event.getDate())){
//                            //Event.eventsList.remove(hold);
//                            result = db.deleteEvent(hold.getName(), hold.getTime().toString(), hold.getDate().toString());
//                            break;
//                        }
//                    }
//                    if(result != -1){
//                        if(dataChange != null){
//                            dataChange.onEventDeleted();
//                        }
//                    }
//                }
//            }
//        });

        eventCellTV.setText(eventTitle);
        return convertView;
    }
}
