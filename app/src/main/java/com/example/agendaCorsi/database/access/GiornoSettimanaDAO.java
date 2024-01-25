package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GiornoSettimanaDAO implements Database_itf {

    private static GiornoSettimanaDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private GiornoSettimanaDAO() {}

    public static GiornoSettimanaDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GiornoSettimanaDAO();
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

    @Override
    public List<Object> getAll(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();

        Log.i(DatabaseHelper.DATABASE_NAME, query);
        Cursor cursor = database.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String numeroGiorno = cursor.getString(GiornoSettimana.NUMERO_GIORNO);
            String nomeGiornoAbbreviato = cursor.getString(GiornoSettimana.NOME_GIORNO_ABBREVIATO);
            String nomeGiornoEsteso = cursor.getString(GiornoSettimana.NOME_GIORNO_ESTESO);

            GiornoSettimana giornoSettimana = new GiornoSettimana(numeroGiorno, nomeGiornoAbbreviato, nomeGiornoEsteso);
            list.add(giornoSettimana);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
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
        GiornoSettimana giornoSettimana = (GiornoSettimana) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#NUMGIO#", giornoSettimana.getNumeroGiorno());
            Log.i(DatabaseHelper.DATABASE_NAME, sql);

            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                giornoSettimana.setNumeroGiorno(resultSet.getString(GiornoSettimana.NUMERO_GIORNO));
                giornoSettimana.setNomeGiornoAbbreviato(resultSet.getString(GiornoSettimana.NOME_GIORNO_ABBREVIATO));
                giornoSettimana.setNomeGiornoEsteso(resultSet.getString(GiornoSettimana.NOME_GIORNO_ESTESO));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            giornoSettimana.setNumeroGiorno("");
        }
        return giornoSettimana;
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
