package com.example.agendaCorsi.database.access;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.ElementoPortfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ElementoPortfolioDAO implements Database_itf {

    private final DatabaseHelper databaseHelper;

    public ElementoPortfolioDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<ElementoPortfolio> getContattoElements(int idContattoToRead, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<ElementoPortfolio> list = new ArrayList<>();
        String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).replace("#IDCONTATTO#", String.valueOf(idContattoToRead));

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String idElemento = String.valueOf(cursor.getInt(ElementoPortfolio.ID_ELEMENTO));
            String idContatto = String.valueOf(cursor.getInt(ElementoPortfolio.ID_CONTATTO));
            String descrizione = String.valueOf(cursor.getString(ElementoPortfolio.DESCRIZIONE));
            String sport = String.valueOf(cursor.getString(ElementoPortfolio.SPORT));
            String numeroLezioni = String.valueOf(cursor.getString(ElementoPortfolio.NUMERO_LEZIONI));
            String dataUltimaRicarica = String.valueOf(cursor.getString(ElementoPortfolio.DATA_ULTIMA_RICARICA));
            String stato = String.valueOf(cursor.getString(ElementoPortfolio.STATO));
            String dataCreazione = String.valueOf(cursor.getString(ElementoPortfolio.DATA_CREAZIONE));
            String dataUltimoAggiornamento = String.valueOf(cursor.getString(ElementoPortfolio.DATA_ULTIMO_AGGIORNAMENTO));

            ElementoPortfolio elementoPortfolio = new ElementoPortfolio(idElemento,idContatto,descrizione,sport,numeroLezioni,dataUltimaRicarica,stato);
            list.add(elementoPortfolio);
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
            ElementoPortfolio elementoPortfolio = ElementoPortfolio.class.cast(entity);
            String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).
                    replace("#IDCONTATTO#", elementoPortfolio.getIdContatto()).
                    replace("#DESC#", elementoPortfolio.getDescrizione()).
                    replace("#SPORT#", elementoPortfolio.getSport()).
                    replace("#NUMLEZ#", elementoPortfolio.getNumeroLezioni()).
                    replace("#ULTRIC#", elementoPortfolio.getDataUltimaRicarica()).
                    replace("STATO", elementoPortfolio.getStato());

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
            ElementoPortfolio elementoPortfolio = ElementoPortfolio.class.cast(entity);
            String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).
                    replace("#IDCONTATTO#", elementoPortfolio.getIdContatto()).
                    replace("#DESC#", elementoPortfolio.getDescrizione()).
                    replace("#SPORT#", elementoPortfolio.getSport()).
                    replace("#NUMLEZ#", elementoPortfolio.getNumeroLezioni()).
                    replace("#ULTRIC#", elementoPortfolio.getDataUltimaRicarica()).
                    replace("#STATO#", elementoPortfolio.getStato()).
                    replace("#IDELEMENTO#", elementoPortfolio.getIdElemento());

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
    public boolean updateStato(Object entity, String query) {
        return false;
    }

    @Override
    public Object select(Object entity, String query) {
        ElementoPortfolio elementoPortfolio = ElementoPortfolio.class.cast(entity);
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).replace("#IDELEMENTO#", elementoPortfolio.getIdElemento());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                elementoPortfolio.setIdContatto(resultSet.getString(ElementoPortfolio.ID_ELEMENTO));
                elementoPortfolio.setIdContatto(resultSet.getString(ElementoPortfolio.ID_CONTATTO));
                elementoPortfolio.setDescrizione(resultSet.getString(ElementoPortfolio.DESCRIZIONE));
                elementoPortfolio.setSport(resultSet.getString(ElementoPortfolio.SPORT));
                elementoPortfolio.setNumeroLezioni(resultSet.getString(ElementoPortfolio.NUMERO_LEZIONI));
                elementoPortfolio.setDataUltimaRicarica(resultSet.getString(ElementoPortfolio.DATA_ULTIMA_RICARICA));
                elementoPortfolio.setStato(resultSet.getString(ElementoPortfolio.STATO));
                elementoPortfolio.setDataCreazione(resultSet.getString(ElementoPortfolio.DATA_CREAZIONE));
                elementoPortfolio.setDataUltimoAggiornamento(resultSet.getString(ElementoPortfolio.DATA_ULTIMO_AGGIORNAMENTO));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            elementoPortfolio.setIdElemento("");
        }

        return elementoPortfolio;
    }

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            ElementoPortfolio elementoPortfolio = ElementoPortfolio.class.cast(entity);
            String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).replace("#IDELEMENTO#", elementoPortfolio.getIdElemento());

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
        ElementoPortfolio elementoPortfolio = ElementoPortfolio.class.cast(entity);
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", ElementoPortfolio.TABLE_NAME).replace("#IDCONTATTO#", elementoPortfolio.getIdContatto()).replace("#SPORT#", elementoPortfolio.getSport());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            if (resultSet.getCount() > 0) {
                return false;
            }
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            elementoPortfolio.setIdContatto("");
        }
        return true;
    }

    @Override
    public List<Object> getFasceCorso(int idCorsoToRead, String query) {
        return null;
    }
}
