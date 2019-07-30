package com.shar.attendance.models;

import java.io.Serializable;
import java.util.Date;

public class OneClass implements Serializable {
    public Date date;
    public boolean attended;

    public OneClass(Date date, boolean attended) {
        this.date = date;
        this.attended = attended;
    }
}
