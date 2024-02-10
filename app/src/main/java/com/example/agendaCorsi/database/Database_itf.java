package com.example.agendaCorsi.database;

import android.database.sqlite.SQLiteDatabase;

import com.example.agendaCorsi.database.table.ElementoPortfolio;

import java.util.List;

public interface Database_itf {

    void create(SQLiteDatabase sqLiteDatabase, String tableName);

    List<Object> getAll(String query);

    boolean insert(Object entity, String query);

    boolean update(Object entity, String query);

    Object select(Object entity, String query);

    boolean delete(Object entity, String query);

    boolean isNew(Object entity, String query);

}
