package com.vijayjaidewan01vivekrai.dynamic_app.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TableRecord {
    String data, url, dateTime;

    public TableRecord(String url) {
        this.url = url;

        //get the current date and time
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTime = simpledateformat.format(calender.getTime());
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
