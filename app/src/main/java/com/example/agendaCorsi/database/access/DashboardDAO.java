package com.example.agendaCorsi.database.access;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.database.table.ElementoPortfolio;

import java.util.ArrayList;
import java.util.List;

public class DashboardDAO implements Database_itf {

    private final DatabaseHelper databaseHelper;

    public DashboardDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Object> getTotals(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();

        String sql = query;
        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String desCorso = String.valueOf(cursor.getString(Dashboard.DESCRIZIONE_CORSO));
            String desFascia = String.valueOf(cursor.getString(Dashboard.DESCRIZIONE_FASCIA));
            String gioSet = String.valueOf(cursor.getString(Dashboard.GIORNO_SETTIMANA));
            String totFascia = String.valueOf(cursor.getString(Dashboard.TOTALE_FASCIA));

            Object dashboard = new Dashboard(desCorso, desFascia, gioSet, totFascia);
            list.add(dashboard);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    @Override
    public List<Object> getAll(String query) {
        return null;
    }

    @Override
    public boolean insert(Object entity, String query) {
        return false;
    }

    @Override
    public boolean update(Object entity, String query) {
        return false;
    }

    @Override
    public boolean updateStato(Object entity, String query) {
        return false;
    }

    @Override
    public Object select(Object entity, String query) {
        return null;
    }

    @Override
    public boolean delete(Object entity, String query) {
        return false;
    }

    @Override
    public boolean isNew(Object entity, String query) {
        return false;
    }

    @Override
    public List<Object> getFasceCorso(String idCorsoToRead, String query) {
        return null;
    }

    @Override
    public List<ElementoPortfolio> getContattoElements(String idContattoToRead, String query) {
        return null;
    }
}
