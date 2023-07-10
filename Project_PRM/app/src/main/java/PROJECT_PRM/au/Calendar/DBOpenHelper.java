package PROJECT_PRM.au.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME= "CalendarDatabase.db";
    private static final int DATABASE_VERSION= 1;

    private static final String TABLE_NAME= "event";
    private static final String COLUMN_TITLE= "title";
    private static final String COLUMN_DESCRIPTION= "description";
    private static final String COLUMN_DATE= "date";
    private static final String COLUMN_TIME= "time";
    private static final String COLUMN_LOCATION="location";

    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context= context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query=
                "CREATE TABLE "+ TABLE_NAME+
                        " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        COLUMN_TITLE+" TEXT, "+
                        COLUMN_DESCRIPTION+ " TEXT, "+
                        COLUMN_TIME+ " TEXT, "+
                        COLUMN_DATE+ " TEXT, "+
                        COLUMN_LOCATION+ " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    void saveEvent(String title, String description, String time, String date, String location){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_LOCATION, location);

        long result= db.insert(TABLE_NAME, null, cv);
        if(result == -1 ){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Add successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readEventsByDay(String date) {
        SQLiteDatabase db= this.getReadableDatabase();
        String query= "SELECT * FROM "+ TABLE_NAME + " WHERE date = '"+ date +"'";

        Cursor cursor= null;
        if(db != null){
            cursor= db.rawQuery(query, null);
        }
        return cursor;
    }

}