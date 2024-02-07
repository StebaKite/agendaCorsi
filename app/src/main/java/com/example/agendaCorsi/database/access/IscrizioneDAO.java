package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.List;
import java.util.Objects;

public class IscrizioneDAO implements Database_itf {

    private static IscrizioneDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private IscrizioneDAO() {}

    public static IscrizioneDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IscrizioneDAO();
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

    public boolean updateStato(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            Iscrizione iscrizione = (Iscrizione) entity;
            String sql = query.replace("#STATO#", iscrizione.getStato()).replace("#IDISCR#", iscrizione.getIdIscrizione());

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
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            Iscrizione iscrizione = (Iscrizione) entity;
            String sql = query.replace("#IDFASCIA#", iscrizione.getIdFascia()).
                    replace("#IDISCR#", iscrizione.getIdIscrizione());

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

    public boolean updateStatoIscrizioni(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            Iscrizione iscrizione = (Iscrizione) entity;
            String sql = query.replace("#IDELEM#", iscrizione.getIdElemento()).replace("#STATO#", iscrizione.getStato());

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
    public Object select(Object entity, String query) {
        Iscrizione iscrizione = (Iscrizione) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#IDISCR#", iscrizione.getIdIscrizione());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                iscrizione.setIdIscrizione(resultSet.getString(Iscrizione.ID_ISCRIZIONE));
                iscrizione.setIdFascia(resultSet.getString(Iscrizione.ID_FASCIA));
                iscrizione.setIdElemento(resultSet.getString(Iscrizione.ID_ELEMENTO));
                iscrizione.setStato(resultSet.getString(Iscrizione.STATO));
                iscrizione.setDataCreazione(resultSet.getString(Iscrizione.DATA_CREAZIONE));
                iscrizione.setDataUltimoAggiornamento(resultSet.getString(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            iscrizione.setIdFascia("");
        }
        return iscrizione;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Iscrizione iscrizione = (Iscrizione) entity;
            String sql = query.replace("#IDISCR#", iscrizione.getIdIscrizione());

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

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }
}
