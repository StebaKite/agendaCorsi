package com.example.agendaCorsi.database.access;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.List;
import java.util.Objects;

public class PresenzaDAO implements Database_itf {

    private static PresenzaDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private PresenzaDAO() {}

    public static PresenzaDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PresenzaDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
    }

    @Override
    public void create(SQLiteDatabase sqLiteDatabase, String tableName) {
        try {
            sqLiteDatabase.execSQL(QueryComposer.getInstance().getQuery(tableName));
        }
        catch (SQLException e) {
            Log.e(">>> " + DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public List<Object> getAll(String query) {
        return null;
    }

    @Override
    public boolean insert(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Presenza presenza = (Presenza) entity;
            String sql = query.replace("#IDISCR#", presenza.getIdIscrizione());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            database.execSQL(sql);
            database.close();
            return true;
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
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
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Presenza presenza = (Presenza) entity;
            String sql = query.replace("#IDPRES#", presenza.getIdPresenza());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            database.execSQL(sql);
            database.close();
            return true;
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    @Override
    public boolean isNew(Object entity, String query) {
        return false;
    }
}
