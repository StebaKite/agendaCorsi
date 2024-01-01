package com.example.agendaCorsi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CorsoDAO {

    private final DatabaseHelper databaseHelper;

    public CorsoDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Corso> getAll() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Corso> list = new ArrayList<>();
        String sql = String.format("select " +
                "id_corso, " +
                "descrizione, " +
                "sport, " +
                "stato, " +
                "data_inizio_validita, " +
                "data_fine_validita, " +
                "data_creazione, " +
                "data_ultimo_aggiornamento " +
                "from " + Corso.TABLE_NAME );

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idCorso = String.valueOf(cursor.getInt(Corso.ID_CORSO));
            String descrizione = String.valueOf(cursor.getString(Corso.DESCRIZIONE));
            String sport = String.valueOf(cursor.getString(Corso.SPORT));
            String stato = String.valueOf(cursor.getString(Corso.STATO));
            String dataInizioValidita = String.valueOf(cursor.getString(Corso.DATA_INIZIO_VALIDITA));
            String dataFineValidita = String.valueOf(cursor.getString(Corso.DATA_FINE_VALIDITA));
            String dataCreazione = String.valueOf(cursor.getString(Corso.DATA_CREAZIONE));
            String dataUltimoAggiornamento = String.valueOf(cursor.getString(Corso.DATA_ULTIMO_AGGIORNAMENTO));

            Corso corso = new Corso(idCorso, descrizione, sport, stato, dataInizioValidita, dataFineValidita, dataCreazione, dataUltimoAggiornamento);
            list.add(corso);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public boolean insert(Corso corso) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = "insert into " + Corso.TABLE_NAME + " " +
                    "(descrizione, sport, stato, data_inizio_validita, data_fine_validita, data_creazione, data_ultimo_aggiornamento) values (" +
                    "'" + corso.getDescrizione() + "', " +
                    "'" + corso.getSport() + "', " +
                    "'" + corso.getStato() + "', " +
                    "'" + corso.getDataInizioValidita() + "', " +
                    "'" + corso.getDataFineValidita() + "', " +
                    "datetime('now'), " +
                    "datetime('now'))";

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

    public boolean update(Corso corso) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            String sql = String.format("update " + Corso.TABLE_NAME + " " +
                    "set " +
                        "descrizione = '" + corso.getDescrizione() + "', " +
                        "sport = '" + corso.getSport() + "', " +
                        "stato = '" + corso.getStato() + "', " +
                        "data_inizio_validita = '" + corso.getDataInizioValidita() + "', " +
                        "data_fine_validita = '" + corso.getDataFineValidita() + "', " +
                        "data_ultimo_aggiornamento = datetime('now') " +
                    "where id_corso = " + corso.getIdCorso());

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

    public Corso select(Corso corso) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("select " +
                    "id_corso, " +
                    "descrizione, " +
                    "sport, " +
                    "stato, " +
                    "data_inizio_validita, " +
                    "data_fine_validita, " +
                    "data_creazione, " +
                    "data_ultimo_aggiornamento " +
                    "from " + Corso.TABLE_NAME + " " +
                    "where id_corso = " + corso.getIdCorso());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                corso.setDescrizione(resultSet.getString(Corso.DESCRIZIONE));
                corso.setSport(resultSet.getString(Corso.SPORT));
                corso.setStato(resultSet.getString(Corso.STATO));
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

    public Boolean delete(Corso corso) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("delete from " + Corso.TABLE_NAME + " " +
                    "where id_corso = " + corso.getIdCorso());

            database.execSQL(sql);
            database.close();
            return true;
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }
}
