/*
 * Copyright (C) 2019  Moutasem Zakkar, www.linkedin.com/in/mzakkar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package DateTimeHelperLib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Moutasem Zakkar, moutasem.zakkar@gmail.com  
 */
public class UtcDateConverter {
    
     public static String ConvertTimeStampToStr(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
           
            TimeZone tz = TimeZone.getTimeZone("UTC");
            calendar.setTimeInMillis(timestamp * 1000); //Change the DBForm by Adding extra threee digits 
            //calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); sdf.setTimeZone(tz);
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
     
    public static Date ConvertTimeStampToDate(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
           
            TimeZone tz = TimeZone.getTimeZone("UTC");
            calendar.setTimeInMillis(timestamp * 1000); //Change the DBForm by Adding extra threee digits 
            //calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); sdf.setTimeZone(tz);
            
            return (Date) calendar.getTime();
        }catch (Exception e) {
            System.out.println(e);
        }
        // We will not reach here
        return null;
    } 
     public static long getYesterdayTimeStampDBForm () {
        Calendar cal = Calendar.getInstance();  // rightnow

        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set (Calendar.HOUR_OF_DAY, 23);
        cal.set (Calendar.MINUTE, 59);
        cal.set (Calendar.SECOND, 59);
        cal.add(Calendar.DATE, -1);
        long i = cal.getTimeInMillis()/1000;  // remove the last three digits to make the DBForm
       return i ;
        
    }
    
    public static long getTodayBeginningTimeStampDBForm () {
        Calendar cal = Calendar.getInstance();  // rightnow

        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set (Calendar.HOUR_OF_DAY, 0);
        cal.set (Calendar.MINUTE, 0);
        cal.set (Calendar.SECOND, 0);
        
        long i = cal.getTimeInMillis()/1000;  // remove the last three digits to make the DBForm
       return i ;
        
    }
}
