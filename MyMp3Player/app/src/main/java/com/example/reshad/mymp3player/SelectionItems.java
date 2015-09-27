package com.example.reshad.mymp3player;

/**
 * Created by Reshad on 2015-09-19.
 */
public class SelectionItems {
    private int id;
    private long dStart;
    private String event_name;
    public SelectionItems(int id, String event_name, long dStart)
    {
        this.event_name = event_name;
        this.dStart = dStart;
        this.id = id;
    }
    public String getEvent_name(){return event_name;}
    public int getId(){return id;}
    public long  getdStart(){return dStart;}
}
