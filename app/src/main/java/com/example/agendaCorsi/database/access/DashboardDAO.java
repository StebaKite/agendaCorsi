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

    /*
    public List<Object> getTotals(String query) {
        List<Object> list = new ArrayList<>();

        Object dashboard = new Dashboard("Corso uno", "10.00-12.00", "1", "10");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "2", "12");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "3", "5");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "4", "3");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "5", "2");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "6", "7");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "10.00-12.00", "7", "1");
        list.add(dashboard);

        //******************************************************

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "1", "18");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "2", "11");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "3", "6");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "4", "7");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "5", "12");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "6", "13");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "12.00-14.00", "7", "5");
        list.add(dashboard);

        //******************************************************

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "1", "4");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "2", "11");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "3", "6");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "4", "7");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "5", "12");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "6", "13");
        list.add(dashboard);

        dashboard = new Dashboard("Corso uno", "16.00-18.00", "7", "5");
        list.add(dashboard);

        //******************************************************

        dashboard = new Dashboard("Corso due", "16.00-18.00", "1", "4");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "2", "11");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "3", "6");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "4", "7");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "5", "12");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "6", "13");
        list.add(dashboard);

        dashboard = new Dashboard("Corso due", "16.00-18.00", "7", "5");
        list.add(dashboard);

        return list;
    }
    */

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
