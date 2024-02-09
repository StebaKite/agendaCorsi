package com.example.agendaCorsi.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Row {

    private Map contents = new HashMap();

    public Row() {
    }

    public Row(String column, Object columnValue) {
        contents.put(column, columnValue);
    }

    public void addColumn(String column, Object columnValue) {
        contents.put(column, columnValue);
    }

    public Object getColumnValue(String column) {
        return contents.get(column);
    }

    public Iterator columns() {
        return contents.keySet().iterator();
    }
}
