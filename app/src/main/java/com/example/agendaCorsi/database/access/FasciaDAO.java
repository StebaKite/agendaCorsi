package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;

import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.FasciaCorso;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    public void create(SQLiteDatabase sqLiteDatabase, String tableName) {
        try {
            sqLiteDatabase.execSQL(QueryComposer.getInstance().getQuery(tableName));
        }
        catch (SQLException e) {
            Log.e(">>> " + DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
    }

    public List<Object> getFasceCorso(String idCorsoToRead, String oraInizioFascia, String oraFineFascia, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();

        String sql = "";
        if (oraInizioFascia == null && oraFineFascia == null) {
            sql = query.replace("#IDCORSO#", String.valueOf(idCorsoToRead)).replace("#FILTRO_FASCIE#", "");
        } else {
            String filtroFascie = "and fascia.ora_inizio = '" + oraInizioFascia + "' and fascia.ora_fine = '" + oraFineFascia + "'";
            sql = query.replace("#IDCORSO#", String.valueOf(idCorsoToRead)).replace("#FILTRO_FASCIE#", filtroFascie);
        }

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String descrizioneCorso = cursor.getString(FasciaCorso.DESCRIZIONE_CORSO);
            String descrizioneFascia = cursor.getString(FasciaCorso.DESCRIZIONE_FASCIA);
            String idFascia = cursor.getString(FasciaCorso.ID_FASCIA);
            String giornoSettimana = cursor.getString(FasciaCorso.GIORNO_SETTIMANA);
            String capienza = cursor.getString(FasciaCorso.CAPIENZA);
            String totaleFascia = cursor.getString(FasciaCorso.TOTALE_FASCIA);

            Object fasciaCorso = new FasciaCorso(descrizioneCorso, null, descrizioneFascia, idFascia, capienza, giornoSettimana, totaleFascia, null);
            list.add(fasciaCorso);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<Object> getAllFasceDisponibili(String idCorso, String idFasciaAttuale, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query.replace("#IDCORSO#", idCorso).replace("#IDFASCIA#", idFasciaAttuale);

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String descrizioneCorso = cursor.getString(FasciaCorso.DESCRIZIONE_CORSO);
            String descrizioneFascia = cursor.getString(FasciaCorso.DESCRIZIONE_FASCIA);
            String idFascia = cursor.getString(FasciaCorso.ID_FASCIA);
            String giornoSettimana = cursor.getString(FasciaCorso.GIORNO_SETTIMANA);
            String capienza = cursor.getString(FasciaCorso.CAPIENZA);
            String totaleFascia = cursor.getString(FasciaCorso.TOTALE_FASCIA);

            Object fasciaCorso = new FasciaCorso(descrizioneCorso, null, descrizioneFascia, idFascia, capienza, giornoSettimana, totaleFascia, null);
            list.add(fasciaCorso);
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
            String numeroGiorno = cursor.getString(FasciaCorso.NUMERO_GIORNO);
            String descrizioneFascia = cursor.getString(FasciaCorso.DESCRIZIONE_FASCIA);
            String idFascia = cursor.getString(FasciaCorso.ID_FASCIA);
            String capienza = cursor.getString(FasciaCorso.CAPIENZA);
            String giornoSettimana = cursor.getString(FasciaCorso.GIORNO_SETTIMANA);
            String totaleFascia = cursor.getString(FasciaCorso.TOTALE_FASCIA);
            String idCorso = cursor.getString(FasciaCorso.ID_CORSO);

            Object fasciaCorso = new FasciaCorso(descrizioneCorso, numeroGiorno, descrizioneFascia, idFascia, capienza, giornoSettimana, totaleFascia, idCorso);
            list.add(fasciaCorso);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<Object> getAllFasceCorsiRunning(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String HHmm = new SimpleDateFormat("HH.mm").format(Calendar.getInstance().getTime());

        Calendar c = Calendar.getInstance(new Locale("en","UK"));
        c.setTime(new Date());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {dayOfWeek = 7;}

        String sql = query.replace("#OGGI#", String.valueOf(dayOfWeek)).replace("#ADESSO#", HHmm);

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String descrizioneCorso = cursor.getString(FasciaCorso.DESCRIZIONE_CORSO);
            String numeroGiorno = cursor.getString(FasciaCorso.NUMERO_GIORNO);
            String descrizioneFascia = cursor.getString(FasciaCorso.DESCRIZIONE_FASCIA);
            String idFascia = cursor.getString(FasciaCorso.ID_FASCIA);
            String capienza = cursor.getString(FasciaCorso.CAPIENZA);
            String giornoSettimana = cursor.getString(FasciaCorso.GIORNO_SETTIMANA);
            String totaleFascia = cursor.getString(FasciaCorso.TOTALE_FASCIA);
            String idCorso = cursor.getString(FasciaCorso.ID_CORSO);

            Object fasciaCorso = new FasciaCorso(descrizioneCorso, numeroGiorno, descrizioneFascia, idFascia, capienza, giornoSettimana, totaleFascia, idCorso);
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
            Fascia fascia = (Fascia) entity;
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
                    replace("#ORAFIN#", fascia.getOraFine()).
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
        Fascia fascia = (Fascia) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", Fascia.TABLE_NAME).
                    replace("#IDFASCIA#", fascia.getIdFascia()).
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
