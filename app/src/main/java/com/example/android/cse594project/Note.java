package com.example.android.cse594project;

/**
 * Created by Matt on 1/20/2017.
 */

public class Note {
    private int id;
    private String text;


    public Note(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

}
