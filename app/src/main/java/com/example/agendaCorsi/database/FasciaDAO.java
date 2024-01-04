package com.example.agendaCorsi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FasciaDAO extends DatabaseFacade {

    private final DatabaseHelper databaseHelper;

    public FasciaDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Object> getFasceCorso(int idCorsoToRead) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = String.format("select " +
                "id_fascia, " +
                "id_corso, " +
                "descrizione, " +
                "giorno_settimana, " +
                "ora_inizio, " +
                "ora_fine, " +
                "capienza, " +
                "data_creazione, " +
                "data_ultimo_aggiornamento " +
                "from " + Fascia.TABLE_NAME + " " +
                "where id_corso = " + String.valueOf(idCorsoToRead));

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idFascia = String.valueOf(cursor.getInt(Fascia.ID_FASCIA));
            String idCorso = String.valueOf(cursor.getInt(Fascia.ID_CORSO));
            String descrizione = String.valueOf(cursor.getString(Fascia.DESCRIZIONE));
            String giornoSettimana = String.valueOf(cursor.getString(Fascia.GIORNO_SETTIMANA));
            String oraInizio = String.valueOf(cursor.getString(Fascia.ORA_INIZIO));
            String oraFine = String.valueOf(cursor.getString(Fascia.ORA_FINE));
            String capienza = String.valueOf(cursor.getString(Fascia.CAPIENZA));
            String dataCreazione = String.valueOf(cursor.getString(Fascia.DATA_CREAZIONE));
            String dataUltimoAggiornamento = String.valueOf(cursor.getString(Fascia.DATA_ULTIMO_AGGIORNAMENTO));

            Object fascia = new Fascia(idFascia, idCorso, descrizione, giornoSettimana, oraInizio, oraFine, capienza, dataCreazione, dataUltimoAggiornamento);
            list.add(fascia);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public boolean insert(Fascia fascia) {
        return false;
    }

    public boolean update(Fascia fascia) {
        return false;
    }

    public Fascia select(Fascia fascia) {
        return null;
    }

    public Boolean delete(Fascia fascia) {
        return null;
    }
}
