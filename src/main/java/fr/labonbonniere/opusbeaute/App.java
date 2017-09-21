package fr.labonbonniere.opusbeaute;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args )

    {
       // System.out.println( "Hello World!1" );
        Calendar gCal = Calendar.getInstance();
     // Month is based upon a zero index, January is equal to 0,
     // so we need to add one to the month for it to be in
     // a standard format
     int month = gCal.get(Calendar.MONTH) + 1;int day = gCal.get(Calendar.DATE);
     int yr = gCal.get(Calendar.YEAR);
     String dateStr = month + "/" + day + "/" + yr;
     System.out.println("1- Date : " + dateStr);
     int dayOfWeek = gCal.get(Calendar.DAY_OF_WEEK);
     // Print out the integer value for the day of the week
     System.out.println("2- Day of week : " + dayOfWeek);
     int hour = gCal.get(Calendar.HOUR);
     int min = gCal.get(Calendar.MINUTE);
     int sec = gCal.get(Calendar.SECOND);
     // Print out the time
     System.out.println("3- time : " + hour + ":" + min + ":" + sec);

     // Create new DateFormatSymbols instance to obtain the String
     // value for dates
     DateFormatSymbols symbols = new DateFormatSymbols();
     String[] days = symbols.getWeekdays();
     System.out.println("4- Jour de la semaine : " + days[dayOfWeek]);
     // Get crazy with the date!
     int dayOfYear = gCal.get(Calendar.DAY_OF_YEAR);
     System.out.println("5- Jour de l'année : " + dayOfYear);
     // Print the number of days left in the year
     System.out.println("6- nb de jours restant : " + yr + ": " + (365-dayOfYear));
     int week = gCal.get(Calendar.WEEK_OF_YEAR);
     // Print the week of the year
     System.out.println("7- Num de la semain : " + week);
    }
}
