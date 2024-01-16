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
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorsoDAO implements Database_itf {

    private static CorsoDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private CorsoDAO() {}

    public static CorsoDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CorsoDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
    }

    public void create(SQLiteDatabase sqLiteDatabase, String tableName) {
        sqLiteDatabase.execSQL(QueryComposer.getInstance().getQuery(tableName));
    }

    public List<Object> getAll(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();

        String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME);
        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idCorso = String.valueOf(cursor.getInt(Corso.ID_CORSO));
            String descrizione = String.valueOf(cursor.getString(Corso.DESCRIZIONE));
            String sport = String.valueOf(cursor.getString(Corso.SPORT));
            String stato = String.valueOf(cursor.getString(Corso.STATO));
            String tipo = String.valueOf(cursor.getString(Corso.TIPO));
            String dataInizioValidita = String.valueOf(cursor.getString(Corso.DATA_INIZIO_VALIDITA));
            String dataFineValidita = String.valueOf(cursor.getString(Corso.DATA_FINE_VALIDITA));
            String dataCreazione = String.valueOf(cursor.getString(Corso.DATA_CREAZIONE));
            String dataUltimoAggiornamento = String.valueOf(cursor.getString(Corso.DATA_ULTIMO_AGGIORNAMENTO));

            Object corso = new Corso(idCorso, descrizione, sport, stato, tipo, dataInizioValidita, dataFineValidita, dataCreazione, dataUltimoAggiornamento);
            list.add(corso);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    @Override
    public boolean insert(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Corso corso = Corso.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME).
                    replace("#DESC#", corso.getDescrizione()).
                    replace("#SPORT#", corso.getSport()).
                    replace("#STATO#", corso.getStato()).
                    replace("TIPO", corso.getTipo()).
                    replace("#DATINI", corso.getDataInizioValidita()).
                    replace("#DATFIN", corso.getDataFineValidita());

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
            Corso corso = Corso.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME).
                    replace("#DESC#", corso.getDescrizione()).
                    replace("#STATO#", corso.getStato()).
                    replace("#DATINI#", corso.getDataInizioValidita()).
                    replace("#DATFIN#", corso.getDataFineValidita()).
                    replace("#IDCORSO#", corso.getIdCorso());

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
            Corso corso = Corso.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME).replace("#STATO#", corso.getStato()).replace("#IDCORSO#", corso.getIdCorso());

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
        Corso corso = Corso.class.cast(entity);
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME).replace("#IDCORSO#", corso.getIdCorso());
            Log.i(DatabaseHelper.DATABASE_NAME, sql);

            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                corso.setDescrizione(resultSet.getString(Corso.DESCRIZIONE));
                corso.setSport(resultSet.getString(Corso.SPORT));
                corso.setStato(resultSet.getString(Corso.STATO));
                corso.setTipo(resultSet.getString(Corso.TIPO));
                corso.setDataInizioValidita(resultSet.getString(Corso.DATA_INIZIO_VALIDITA));
                corso.setDataFineValidita(resultSet.getString(Corso.DATA_FINE_VALIDITA));
                corso.setDataCreazione(resultSet.getString(Corso.DATA_CREAZIONE));
                corso.setDataUltimoAggiornamento(resultSet.getString(Corso.DATA_ULTIMO_AGGIORNAMENTO));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            corso.setIdCorso("");
        }
        return corso;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Corso corso = Corso.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Corso.TABLE_NAME).replace("#IDCORSO#", corso.getIdCorso());

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
