package PROJECT_PRM.au.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
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
        Button editButtonCellTV = convertView.findViewById(R.id.EDITbutton);
        Button deleteButtonCellTV = convertView.findViewById(R.id.DELbutton);
        String eventTitle = event.getName() +" "+ CalendarUtils.formattedTime(event.getTime());
        editButtonCellTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        &&hold.getTime().equals(event.getTime())){
                            Event.eventsList.remove(hold);

                            break;
                        }
                    }
                }
            }
        });



        eventCellTV.setText(eventTitle);
        return convertView;
    }

    public interface  DataChange
    {
        void DELCLICK(int position, LocalDate date);
    }

//    private DataChangeListener dataChangeListener;
//
//    public void setDataChangeListener(DataChangeListener listener) {
//        this.dataChangeListener = listener;
//    }
//    private void updateData() {
//        // Cập nhật dữ liệu
//
//        // Gọi phương thức onDataChanged() để thông báo sự thay đổi
//        if (dataChangeListener != null) {
//            dataChangeListener.onDataChanged();
//        }
//    }
}
