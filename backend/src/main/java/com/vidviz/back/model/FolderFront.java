package com.vidviz.back.model;

import java.util.Date;

public class FolderFront {
    public String name;
    public int itemNumbers;
    public Date created;

    public FolderFront(String name, int itemNumbers, Date date) {
        this.name = name;
        this.itemNumbers = itemNumbers;
        this.created = date;
    }
}
