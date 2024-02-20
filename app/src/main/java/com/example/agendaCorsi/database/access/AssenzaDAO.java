package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Assenza;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class AssenzaDAO implements Database_itf {

    private static AssenzaDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private AssenzaDAO() {}

    public static AssenzaDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AssenzaDAO();
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
            Assenza assenza = (Assenza) entity;
            String sql = query.replace("#IDISCR#", assenza.getIdIscrizione());

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
    public Object select(Object entity, String query) { return null; }


    public Object getTotAssenze(Object entity, String query) {
        Assenza assenza = (Assenza) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String oggi = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            String sql = query.replace("#IDISCR#", assenza.getIdIscrizione()).replace("#OGGI#", oggi);
            Log.i(DatabaseHelper.DATABASE_NAME, sql);

            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                assenza.setTotaleAssenza(resultSet.getString(Assenza.TOT_ASSENZE));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            assenza.setIdIscrizione("");
        }
        return assenza;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Assenza assenza = (Assenza) entity;
            String sql = query.replace("#IDASS#", assenza.getIdAssenza());

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

    public boolean deleteAllAssenzeIscrizione(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Assenza assenza = (Assenza) entity;
            String oggi = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String sql = query.replace("#IDISCR#", assenza.getIdIscrizione()).replace("#OGGI#", oggi);

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
