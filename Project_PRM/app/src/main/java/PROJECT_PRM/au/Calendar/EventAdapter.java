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
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import java.time.LocalDate;
import java.util.ArrayList;
=======
>>>>>>> f4cd5f9ed7cace2cf0b14bfd05b61ebee1377f52
import java.util.List;
class EventVH extends RecyclerView.ViewHolder{
    private EventAdapter adapter;
    public EventVH(@NonNull View itemView) {
        super(itemView);
        itemView = itemView.findViewById(R.id.eventCellTV);
        itemView.findViewById(R.id.DELbutton).setOnClickListener(view -> {

        });
    }
    public EventVH linkAdapter(EventAdapter adapter){
        this.adapter = adapter;
        return this;
    }
}
//public class EventAdapter extends RecyclerView.Adapter<EventVH>{
//    List<Event> events;
//    public EventAdapter(List<Event> events){
//        this.events = events;
//    }
//    @NonNull
//    @Override
//    public EventVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.event_cell,parent,false);
//        return new EventVH(view).linkAdapter(this);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull EventVH holder, int position) {
//        holder.itemView.text
//    }
//
//    @Override
//    public int getItemCount() {
//        return events.size();
//    }
//}
public class EventAdapter extends ArrayAdapter<Event>
{
<<<<<<< HEAD
    DBOpenHelper db;
    EventAdapter adapter = this;

    long result;
=======
>>>>>>> f4cd5f9ed7cace2cf0b14bfd05b61ebee1377f52
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
//        Button editButtonCellTV = convertView.findViewById(R.id.EDITbutton);
//        Button deleteButtonCellTV = convertView.findViewById(R.id.DELbutton);
        String eventTitle = event.getName() +" "+ CalendarUtils.formattedTime(event.getTime());
        eventCellTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                if(!Event.eventsList.isEmpty()){

                }
            }
        });
        deleteButtonCellTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Event.eventsList.isEmpty()){
                    for(int i=0;i<Event.eventsList.size();i++){
                        Event hold = Event.eventsList.get(i);
                        if(hold.getName().equals(event.getName())
                        &&hold.getTime().equals(event.getTime())
                        &&hold.getDate().equals(event.getDate())){

                            Event.eventsList.remove(i);
                            result = db.deleteEvent(hold.getName(), hold.getTime().toString(), hold.getDate().toString());
                            break;
                        }
                    }
                    if(result != -1){
                        if(dataChange != null){
                            dataChange.onEventDeleted();
                        }
                    }
                }
=======
                Intent intent= new Intent(getContext().getApplicationContext(), EventUpdateActivity.class);
                intent.putExtra("title", event.getName());
                intent.putExtra("date", event.getDate().toString());
                intent.putExtra("time", event.getTime().toString());
                getContext().startActivity(intent);
>>>>>>> f4cd5f9ed7cace2cf0b14bfd05b61ebee1377f52
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
