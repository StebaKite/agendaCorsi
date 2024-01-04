package com.example.agendaCorsi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ElementoPortfolioDAO {

    private final DatabaseHelper databaseHelper;

    public ElementoPortfolioDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<ElementoPortfolio> getContattoElements(int idContattoToRead) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<ElementoPortfolio> list = new ArrayList<>();
        String sql = String.format("select " +
                "id_elemento, " +
                "id_contatto, " +
                "descrizione, " +
                "sport, " +
                "numero_lezioni, " +
                "data_ultima_ricarica, " +
                "stato, " +
                "data_creazione, " +
                "data_ultimo_aggiornamento " +
                "from elemento_portfolio " +
                "where id_contatto = " + String.valueOf(idContattoToRead));

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


    public boolean insert(ElementoPortfolio elementoPortfolio) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = "insert into " + ElementoPortfolio.TABLE_NAME + " " +
                    "(id_contatto, descrizione, sport, numero_lezioni, data_ultima_ricarica, stato, data_creazione, data_ultimo_aggiornamento) values (" +
                    "'" + elementoPortfolio.getIdContatto() + "', " +
                    "'" + elementoPortfolio.getDescrizione() + "', " +
                    "'" + elementoPortfolio.getSport() + "', " +
                    "'" + elementoPortfolio.getNumeroLezioni() + "', " +
                    "'" + elementoPortfolio.getDataUltimaRicarica() + "', " +
                    "'" + elementoPortfolio.getStato() + "', " +
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


    public boolean update(ElementoPortfolio elementoPortfolio) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            String sql = String.format("update " + ElementoPortfolio.TABLE_NAME + " " +
                    "set " +
                    "id_contatto = '" + elementoPortfolio.getIdContatto() + "', " +
                    "descrizione = '" + elementoPortfolio.getDescrizione() + "', " +
                    "sport = '" + elementoPortfolio.getSport() + "', " +
                    "numero_lezioni = '" + elementoPortfolio.getNumeroLezioni() + "', " +
                    "data_ultima_ricarica = '" + elementoPortfolio.getDataUltimaRicarica() + "', " +
                    "stato = '" + elementoPortfolio.getStato() + "', " +
                    "data_ultimo_aggiornamento = datetime('now') " +
                    "where id_elemento = " + elementoPortfolio.getIdElemento());

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


    public ElementoPortfolio select(ElementoPortfolio elementoPortfolio) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("select " +
                    "id_elemento, " +
                    "id_contatto, " +
                    "descrizione, " +
                    "sport, " +
                    "numero_lezioni, " +
                    "data_ultima_ricarica, " +
                    "stato, " +
                    "data_creazione, " +
                    "data_ultimo_aggiornamento " +
                    "from " + ElementoPortfolio.TABLE_NAME + " " +
                    "where id_elemento = " + elementoPortfolio.getIdElemento());

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

    public boolean delete(ElementoPortfolio elementoPortfolio) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("delete from " + ElementoPortfolio.TABLE_NAME + " " +
                    "where id_elemento = " + elementoPortfolio.getIdElemento());
            database.execSQL(sql);
            database.close();
            return true;
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public Boolean isNew(ElementoPortfolio elementoPortfolio) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("select " +
                    "id_elemento " +
                    "from " + ElementoPortfolio.TABLE_NAME + " " +
                    "where id_contatto = " + elementoPortfolio.getIdContatto() + " " +
                    "and sport = " + elementoPortfolio.getSport());

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

}
