package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Credenziale;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class CredenzialeDAO implements Database_itf {

    private static CredenzialeDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;

    public static PropertyReader propertyReader;
    public static Properties properties;
    public SQLiteDatabase sqLiteDatabase;

    public static String utenteCorrente;
    public static String passwordCorrente;

    private CredenzialeDAO() {}

    public static CredenzialeDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CredenzialeDAO();
            propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
            properties = propertyReader.getMyProperties("config.properties");
            setUtenteCorrente("");
            setPasswordCorrente("");
        }
        return INSTANCE;
    }

    public void create(String tableName) {
        getSqLiteDatabase().execSQL(QueryComposer.getInstance().getQuery(tableName));
    }

    @Override
    public List<Object> getAll(String query) {
        return null;
    }

    @Override
    public boolean insert(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Credenziale credenziale = (Credenziale) entity;
            String sql = query.replace("#UTENTE#", credenziale.getUtente()).
                    replace("#PASSWD#", credenziale.getPassword());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            database.execSQL(sql);
            database.close();
            setUtenteCorrente(credenziale.getUtente());
            setPasswordCorrente(credenziale.getPassword());
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
            Credenziale credenziale = (Credenziale) entity;
            String sql = query.replace("#UTENTE#", credenziale.getUtente()).
                    replace("#PASSWD#", credenziale.getPassword());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            database.execSQL(sql);
            database.close();
            setPasswordCorrente(credenziale.getPassword());
            return true;
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    @Override
    public Object select(Object entity, String query) {
        Credenziale credenziale = (Credenziale) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Log.i(DatabaseHelper.DATABASE_NAME, query);
            final Cursor resultSet = database.rawQuery(query, null);

            if (resultSet.getCount() > 0) {
                resultSet.moveToNext();
                credenziale.setUtente(resultSet.getString(Credenziale.UTENTE));
                credenziale.setPassword(resultSet.getString(Credenziale.PASSWORD));
                credenziale.setDataCreazione(resultSet.getString(Credenziale.DATA_CREAZIONE));
                credenziale.setDataUltimoAggiornamento(resultSet.getString(Credenziale.DATA_ULTIMO_AGGIORNAMENTO));
            }
            else {
                credenziale.setUtente("");
                credenziale.setPassword("");
                credenziale.setDataCreazione("");
                credenziale.setDataUltimoAggiornamento("");
            }
            resultSet.close();
            database.close();

            setUtenteCorrente(credenziale.getUtente());
            setPasswordCorrente(credenziale.getPassword());
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            credenziale.setUtente("");
        }
        return credenziale;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Credenziale credenziale = (Credenziale) entity;
            String sql = query.replace("#UTENTE#", credenziale.getUtente());

            database.execSQL(sql);
            database.close();
            setUtenteCorrente("");
            setPasswordCorrente("");
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

    public String getUtenteCorrente() {
        return utenteCorrente;
    }

    public static void setUtenteCorrente(String utenteCorrente) {
        CredenzialeDAO.utenteCorrente = utenteCorrente;
    }

    public String getPasswordCorrente() {
        return passwordCorrente;
    }

    public static void setPasswordCorrente(String passwordCorrente) {
        CredenzialeDAO.passwordCorrente = passwordCorrente;
    }
}
