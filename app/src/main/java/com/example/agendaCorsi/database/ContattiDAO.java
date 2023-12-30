package com.example.agendaCorsi.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContattiDAO {

    private final DatabaseHelper databaseHelper;

    public ContattiDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Contatto> getAll() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Contatto> list = new ArrayList<>();
        String sql = String.format("select " +
                "id_contatto, " +
                "nome, " +
                "indirizzo, " +
                "telefono, " +
                "email " +
                "from contatto");

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = String.valueOf(cursor.getInt(Contatto.ID_CONTATTO));
            String nome = cursor.getString(Contatto.NOME);
            String indirizzo = cursor.getString(Contatto.INDIRIZZO);
            String telefono = cursor.getString(Contatto.TELEFONO);
            String email = cursor.getString(Contatto.EMAIL);
            Contatto contatto = new Contatto(id, nome, indirizzo, telefono, email);
            list.add(contatto);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return list;
    }


    public boolean insert(Contatto contatto) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = "insert into " + Contatto.TABLE_NAME + " " +
                    "(nome, indirizzo, telefono, email, data_creazione, data_ultimo_aggiornamento) values (" +
                    "'" + contatto.getNome() + "', " +
                    "'" + contatto.getIndirizzo() + "', " +
                    "'" + contatto.getTelefono() + "', " +
                    "'" + contatto.getEmail() + "', " +
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


    public boolean update(Contatto contatto) {
        try {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            String sql = String.format("update " + Contatto.TABLE_NAME + " " +
                    "set " +
                        "nome = '" + contatto.getNome() + "', " +
                        "indirizzo = '" + contatto.getIndirizzo() + "', " +
                        "telefono = '" + contatto.getTelefono() + "', " +
                        "email = '" + contatto.getEmail() + "', " +
                        "data_ultimo_aggiornamento = datetime('now') " +
                    "where id_contatto = " + contatto.getId());

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

    public Contatto select(Contatto contatto) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("select " +
                    "id_contatto, " +
                    "nome, "+
                    "indirizzo, " +
                    "telefono, " +
                    "email " +
                    "from " + Contatto.TABLE_NAME + " " +
                    "where id_contatto = " + contatto.getId());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                contatto.setNome(resultSet.getString(Contatto.NOME));
                contatto.setIndirizzo(resultSet.getString(Contatto.INDIRIZZO));
                contatto.setTelefono(resultSet.getString(Contatto.TELEFONO));
                contatto.setEmail(resultSet.getString(Contatto.EMAIL));
            }
            resultSet.close();
            database.close();
        }
        catch (SQLException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            contatto.setId("");
        }
        return contatto;
    }

    public Boolean delete(Contatto contatto) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = String.format("delete from " + Contatto.TABLE_NAME + " " +
                    "where id_contatto = " + contatto.getId());
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
