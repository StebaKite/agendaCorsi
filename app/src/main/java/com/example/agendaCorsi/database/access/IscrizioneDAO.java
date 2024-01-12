package com.example.agendaCorsi.database.access;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.Iscrizione;

import java.util.List;
import java.util.Objects;

public class IscrizioneDAO implements Database_itf {

    private final DatabaseHelper databaseHelper;

    public IscrizioneDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public List<Object> getAll(String query) {
        return null;
    }

    @Override
    public boolean insert(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Iscrizione iscrizione = Iscrizione.class.cast(entity);
            String sql = query.replace("#IDFASCIA#", iscrizione.getIdFascia()).
                    replace("#IDELEME#", iscrizione.getIdElemento()).
                    replace("#STATO#", iscrizione.getStato());

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
