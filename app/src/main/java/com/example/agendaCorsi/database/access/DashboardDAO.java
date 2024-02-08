package com.example.agendaCorsi.database.access;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Dashboard;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashboardDAO implements Database_itf {

    private static DashboardDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private DashboardDAO() {}

    public static DashboardDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DashboardDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
    }

    public void create(SQLiteDatabase sqLiteDatabase, String tableName) {
        try {
            sqLiteDatabase.execSQL(QueryComposer.getInstance().getQuery(tableName));
        }
        catch (SQLException e) {
            Log.e(">>> " + DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
    }

    public List<Object> getTotals(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();

        String sql = query;
        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idCorso = cursor.getString(Dashboard.ID_CORSO);
            String desCorso = cursor.getString(Dashboard.DESCRIZIONE_CORSO);
            String staCorso = cursor.getString(Dashboard.STATO_CORSO);
            String desFascia = cursor.getString(Dashboard.DESCRIZIONE_FASCIA);
            String gioSet = cursor.getString(Dashboard.GIORNO_SETTIMANA);
            String idFascia = cursor.getString(Dashboard.ID_FASCIA);
            String totFascia = cursor.getString(Dashboard.TOTALE_FASCIA);

            Object dashboard = new Dashboard(idCorso, desCorso, staCorso, desFascia, gioSet, idFascia, totFascia);
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

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }
}
