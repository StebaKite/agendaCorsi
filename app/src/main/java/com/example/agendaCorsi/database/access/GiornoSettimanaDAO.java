package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.GiornoSettimana;

import java.util.ArrayList;
import java.util.List;

public class GiornoSettimanaDAO implements Database_itf {

    private static GiornoSettimanaDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;

    private GiornoSettimanaDAO() {}

    public static GiornoSettimanaDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GiornoSettimanaDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
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
