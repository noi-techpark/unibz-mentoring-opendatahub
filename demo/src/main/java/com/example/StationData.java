package com.example;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StationData {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    @JsonProperty("_timestamp")
    private LocalDateTime timestamp;

    private int mperiod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    private LocalDateTime mvalidtime;

    private int mvalue;
    private String sname;
    private String sorigin;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public int getMperiod() {
        return mperiod;
    }
    public LocalDateTime getMvalidtime() {
        return mvalidtime;
    }
    public int getMvalue() {
        return mvalue;
    }
    public String getSname() {
        return sname;
    }
    public String getSorigin() {
        return sorigin;
    }

}
