package com.shar.attendance.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SubjectModel implements Serializable {
    public String subjectName;
    public List<OneClass> conducted;


    public SubjectModel(String subjectName){
        this.subjectName = subjectName;
        this.conducted = new ArrayList<>();

    }

    public int[] countConductedToday(){
        int[] i = new int[]{0,0};
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();



        for(OneClass myClass : conducted){
            cal1.setTime(myClass.date);
            boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            if (sameDay){
                if(myClass.attended){
                    i[0]++;
                }
                i[1]++;
            }
        }
        return i;
    }


    public void addClass(OneClass myClass)
    {
        this.conducted.add(myClass);
    }

    public int countConducted(){
        return this.conducted.size();
    }

    public int countAttended(){
        int i = 0;
        for(OneClass myClass : this.conducted){
            if(myClass.attended){
                i++;
            }
        }
        return i;
    }


}

