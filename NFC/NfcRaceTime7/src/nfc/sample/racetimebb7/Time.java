package nfc.sample.racetimebb7;
/*
* Copyright (c) 2012 Research In Motion Limited.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import java.util.Calendar;
import java.util.Date;

public class Time {

    private static Time time;
    
//    private Calendar cal = Calendar.getInstance();
    private long started_at_ms=0;
    private long now=0;
    private long elapsed=0;
    
    private int hour1;
    private int hour2;
    private int minute1;
    private int minute2;
    private int second1;
    private int second2;
    
    private long MS_HOUR=3600000;
    private long MS_MINUTE=60000;
    private long MS_SECOND=1000;
    
    public static Time getInstance() {
        if (time == null) {
            time = new Time();
        }
        return time;
    }
    
    private Time() {        
    }
    
    public void start() {
        started_at_ms = System.currentTimeMillis();
        Utilities.log("XXXX started_at_ms set to:"+started_at_ms);
    }
    
    public void setNow() {
        now = System.currentTimeMillis();
        Utilities.log("XXXX started_at_ms:"+started_at_ms);
        Utilities.log("XXXX now:"+now);
        elapsed = now - started_at_ms;
        Utilities.log("XXXX elapsed:"+elapsed);
//        cal.setTime(new Date(elapsed));
        
//        int hours = cal.get(Calendar.HOUR_OF_DAY);
//        int minutes = cal.get(Calendar.MINUTE);
//        int seconds = cal.get(Calendar.SECOND);

        int hours = (int) (elapsed / MS_HOUR);
        int remainder  = (int) (elapsed - (hours * MS_HOUR));
        int minutes = (int) (remainder / MS_MINUTE);
        remainder  = (int) (remainder - (minutes * MS_MINUTE));
        int seconds = (int) (remainder / MS_SECOND);
        
        Utilities.log("XXXX hours="+hours+",minutes="+minutes+",seconds="+seconds);
        
        hour1 = hours / 10;
        hour2 = hours % 10;
        minute1 = minutes / 10;
        minute2 = minutes % 10;
        second1 = seconds / 10;
        second2 = seconds % 10;
        
        Utilities.log("XXXX hour1="+hour1+",hour2="+hour2+",minute1="+minute1+",minute2="+minute2+",second1="+second1+",second2="+second2);
    }

    public long getStarted_at_ms() {
        return started_at_ms;
    }

    public void setStarted_at_ms(long started_at_ms) {
        this.started_at_ms = started_at_ms;
    }

    public int getHour1() {
        return hour1;
    }

    public int getHour2() {
        return hour2;
    }

    public int getMinute1() {
        return minute1;
    }

    public int getMinute2() {
        return minute2;
    }

    public int getSecond1() {
        return second1;
    }

    public int getSecond2() {
        return second2;
    }
    
}
