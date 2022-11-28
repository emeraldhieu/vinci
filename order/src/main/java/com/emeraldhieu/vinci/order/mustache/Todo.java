package com.emeraldhieu.vinci.order.mustache;

import lombok.Data;

import java.util.Date;

@Data
public class Todo {
    private String title;
    private String text;
    private boolean done;
    private Date createdOn;
    private Date completedOn;
}