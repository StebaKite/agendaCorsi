package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;

import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FasciaDAO implements Database_itf {

    private static FasciaDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private FasciaDAO() {}

    public static FasciaDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FasciaDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
    }

    @Override
    public void create(String tableName) {
        getSqLiteDatabase().execSQL(QueryComposer.getInstance().getQuery(tableName));
    }

    public List<Object> getFasceCorso(String idCorsoToRead, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).replace("#IDCORSO#", String.valueOf(idCorsoToRead));

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idFascia = String.valueOf(cursor.getInt(Fascia.ID_FASCIA));
            String idCorso = String.valueOf(cursor.getInt(Fascia.ID_CORSO));
            String descrizione = String.valueOf(cursor.getString(Fascia.DESCRIZIONE));
            String numeroGiorno = String.valueOf(cursor.getString(Fascia.NUMERO_GIORNO));
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

    public List<Object> getAllFasceCorsi(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query;

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String descrizioneCorso = cursor.getString(FasciaCorso.DESCRIZIONE_CORSO);
            String stato = cursor.getString(FasciaCorso.STATO);
            String sport = cursor.getString(FasciaCorso.SPORT); //no
            String numeroGiorno = cursor.getString(FasciaCorso.NUMERO_GIORNO);
            String descrizioneFascia = cursor.getString(FasciaCorso.DESCRIZIONE_FASCIA);
            String idFascia = cursor.getString(FasciaCorso.ID_FASCIA);
            String capienza = cursor.getString(FasciaCorso.CAPIENZA);
            String giornoSettimana = cursor.getString(FasciaCorso.GIORNO_SETTIMANA);
            String totaleFascia = cursor.getString(FasciaCorso.TOTALE_FASCIA);
            String idCorso = cursor.getString(FasciaCorso.ID_CORSO);
            String tipoCorso = cursor.getString((FasciaCorso.TIPO_CORSO));

            Object fasciaCorso = new FasciaCorso(descrizioneCorso, stato, sport, numeroGiorno, descrizioneFascia, idFascia, capienza, giornoSettimana, totaleFascia, idCorso, tipoCorso);
            list.add(fasciaCorso);
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
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Fascia fascia = Fascia.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).
                    replace("#IDCORSO#", fascia.getIdCorso()).
                    replace("#DESC#", fascia.getDescrizione()).
                    replace("#GIOSET#", fascia.getGiornoSettimana()).
                    replace("#ORAINI#", fascia.getOraInizio()).
                    replace("#ORAFIN#", fascia.getOraFine()).
                    replace("#CAPIEN#", fascia.getCapienza());

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
            Fascia fascia = Fascia.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).
                    replace("#DESC#", fascia.getDescrizione()).
                    replace("#GIOSET#", fascia.getGiornoSettimana()).
                    replace("#ORAINI#", fascia.getOraInizio()).
                    replace("##ORAFIN", fascia.getOraFine()).
                    replace("#CAPIEN#", fascia.getCapienza()).
                    replace("#IDFASCIA#", fascia.getIdFascia());

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
        Fascia fascia = Fascia.class.cast(entity);
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).replace("#IDFASCIA#", fascia.getIdFascia());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                fascia.setIdFascia(resultSet.getString(Fascia.ID_FASCIA));
                fascia.setIdCorso(resultSet.getString(Fascia.ID_CORSO));
                fascia.setDescrizione(resultSet.getString(Fascia.DESCRIZIONE));
                fascia.setGiornoSettimana(resultSet.getString(Fascia.GIORNO_SETTIMANA));
                fascia.setCapienza(resultSet.getString(Fascia.CAPIENZA));
                fascia.setDataCreazione(resultSet.getString(Fascia.DATA_CREAZIONE));
                fascia.setOraInizio(resultSet.getString(Fascia.ORA_INIZIO));
                fascia.setOraFine(resultSet.getString(Fascia.ORA_FINE));
                fascia.setDataUltimoAggiornamento(resultSet.getString(Fascia.DATA_ULTIMO_AGGIORNAMENTO));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            fascia.setIdFascia("");
        }
        return fascia;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Fascia fascia = Fascia.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).replace("#IDFASCIA#", fascia.getIdFascia());

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
        Fascia fascia = Fascia.class.cast(entity);
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).
                    replace("#IDCORSO#", fascia.getIdCorso()).
                    replace("#GIOSET#", fascia.getGiornoSettimana()).
                    replaceAll("#ORAINI#", fascia.getOraInizio()).
                    replaceAll("#ORAFIN#", fascia.getOraFine());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            if (resultSet.getCount() > 0) {
                return false;
            }
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            fascia.setIdCorso("");
        }
        return true;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }
}
