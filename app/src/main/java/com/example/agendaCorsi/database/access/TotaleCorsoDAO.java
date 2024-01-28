package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.TotaleCorso;
import com.example.agendaCorsi.database.table.TotaleIscrizioniCorso;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TotaleCorsoDAO implements Database_itf {

    private static TotaleCorsoDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private TotaleCorsoDAO() {}

    public static TotaleCorsoDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TotaleCorsoDAO();
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
            TotaleCorso totaleCorso = (TotaleCorso) entity;
            String sql = query.replace("#DESCR#", totaleCorso.getDescrizioneCorso()).
                    replace("#ANNO#", totaleCorso.getAnnosvolgimento()).
                    replace("#NOMTOT#", totaleCorso.getNomeTotale()).
                    replace("#VALTOT#", totaleCorso.getValoreTotale());

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
    public Object select(Object entity, String query) {
        return null;
    }

    public Object selectTotaleIscrizioni(Object entity, String query) {
        TotaleIscrizioniCorso totaleIscrizioniCorso = (TotaleIscrizioniCorso) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#IDCORSO#", totaleIscrizioniCorso.getIdCorso());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                totaleIscrizioniCorso.setDescrizioneCorso(resultSet.getString(TotaleIscrizioniCorso.DESCRIZIONE_CORSO));
                totaleIscrizioniCorso.setAnnoSvolgimento(getYearFromDate(resultSet.getString(TotaleIscrizioniCorso.ANNO_SVOLGIMENTO)));
                totaleIscrizioniCorso.setTotaleIscrizioni(resultSet.getString(TotaleIscrizioniCorso.TOTALE_ISCRIZIONI));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            totaleIscrizioniCorso.setIdCorso("");
        }
        return totaleIscrizioniCorso;
    }


    @Override
    public boolean delete(Object entity, String query) {
        return false;
    }

    @Override
    public boolean isNew(Object entity, String query) {
        return false;
    }

    private String getYearFromDate(String data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(data.replaceAll("#", ""));
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            return String.valueOf(cal.get(Calendar.YEAR));
        }
        catch (ParseException e) {
            return null;
        }
    }
}
