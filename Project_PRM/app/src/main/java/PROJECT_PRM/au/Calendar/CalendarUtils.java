package PROJECT_PRM.au.Calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils
{
    public static LocalDate selectedDate;

    public static String formattedDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
    {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
        return  daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate)
    {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current)
    {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }
    public static LocalDate stringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        try{
            String[] items = date.split(" ");
//            switch (items[2]){
//                case "January":{
//                    items[2] = "01";
//                    break;
//                }
//                case "February":{
//                    items[2] = "02";
//                    break;
//                }
//                case "March":{
//                    items[2] = "03";
//                    break;
//                }
//                case "April":{
//                    items[2] = "04";
//                    break;
//                }
//                case "May":{
//                    items[2] = "05";
//                    break;
//                }
//                case "June":{
//                    items[2] = "06";
//                    break;
//                }
//                case "July":{
//                    items[2] = "07";
//                    break;
//                }
//                case "August":{
//                    items[2] = "08";
//                    break;
//                }
//                case "September":{
//                    items[2] = "09";
//                    break;
//                }
//                case "October":{
//                    items[2] = "10";
//                    break;
//                }
//                case "November":{
//                    items[2] = "11";
//                    break;
//                }
//                case "December":{
//                    items[2] = "12";
//                    break;
//                }
//            }
            String end = items[1]+" "+items[2]+" "+items[3];
            return LocalDate.parse(end, formatter);
        }catch (Exception ex){

        }
        return LocalDate.parse("01 January 2000",formatter);
    }
    public static LocalTime stringToLocalTime(String Time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        try{
            String[] items = Time.split(" ");
            String end = items[1]+" "+items[2];
            LocalTime hold = LocalTime.parse(end,formatter);
            return hold;

        }catch (Exception ex){

        }
        return LocalTime.parse("01:00:00 PM");
    }


}
