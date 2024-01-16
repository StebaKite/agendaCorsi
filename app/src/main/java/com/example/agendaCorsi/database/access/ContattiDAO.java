package com.example.agendaCorsi.database.access;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Database_itf;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ContattoIscritto;
import com.example.agendaCorsi.database.table.ContattoIscrivibile;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContattiDAO implements Database_itf {

    private static ContattiDAO INSTANCE = null;
    private static DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;

    private ContattiDAO() {}

    public static ContattiDAO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContattiDAO();
            databaseHelper = new DatabaseHelper(AgendaCorsiApp.getContext());
        }
        return INSTANCE;
    }

    @Override
    public void create(SQLiteDatabase sqLiteDatabase, String tableName) {
        sqLiteDatabase.execSQL(QueryComposer.getInstance().getQuery(tableName));
    }

    public List<Object> getIscrivibili(String idCorso, String idFascia, String sport, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query.replace("#SPORT#", sport).replace("#IDCORSO#", idCorso);

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String nomeContatto = cursor.getString(ContattoIscrivibile.NOME_CONTATTO);
            String idElemento = cursor.getString(ContattoIscrivibile.ID_ELEMENTO);
            String emailContatto = cursor.getString(ContattoIscrivibile.EMAIL_CONTATTO);

            ContattoIscrivibile contattoIscrivibile = new ContattoIscrivibile(nomeContatto, idElemento, emailContatto, idFascia, idCorso, sport);
            list.add(contattoIscrivibile);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<Object> getIscritti(String idFascia, String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query.replace("#IDFASCIA#", idFascia);

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String nomeContatto = cursor.getString(ContattoIscritto.NOME_CONTATTO);
            String idIscrizione = cursor.getString(ContattoIscritto.ID_ISCRIZIONE);

            ContattoIscritto contattoIscritto = new ContattoIscritto(nomeContatto,idIscrizione);
            list.add(contattoIscritto);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();
        return list;
    }

    public List<Object> getAll(String query) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        List<Object> list = new ArrayList<>();
        String sql = query.replace("#TABLENAME#", Contatto.TABLE_NAME);

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = String.valueOf(cursor.getInt(Contatto.ID_CONTATTO));
            String nome = cursor.getString(Contatto.NOME);
            String dataNascita = cursor.getString(Contatto.DATA_NASCITA);
            String indirizzo = cursor.getString(Contatto.INDIRIZZO);
            String telefono = cursor.getString(Contatto.TELEFONO);
            String email = cursor.getString(Contatto.EMAIL);

            Contatto contatto = new Contatto(id, nome, dataNascita, indirizzo, telefono, email);
            list.add(contatto);
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
            Contatto contatto = Contatto.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Contatto.TABLE_NAME).
                    replace("#NOME#", contatto.getNome()).
                    replace("#DATNAS#", contatto.getDataNascita()).
                    replace("#INDIR#", contatto.getIndirizzo()).
                    replace("#TEL#", contatto.getTelefono()).
                    replace("#EMAIL#", contatto.getEmail());

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
            Contatto contatto = Contatto.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Contatto.TABLE_NAME).
                    replace("#NOME#", contatto.getNome()).
                    replace("#DATNAS#", contatto.getDataNascita()).
                    replace("#INDIR#", contatto.getIndirizzo()).
                    replace("#TEL#", contatto.getTelefono()).
                    replace("#EMAIL#", contatto.getEmail()).
                    replace("#IDCONTATTO#", contatto.getId());

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
        Contatto contatto = (Contatto) entity;
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            String sql = query.replace("#TABLENAME#", Contatto.TABLE_NAME).replace("#IDCONTATTO#", contatto.getId());

            Log.i(DatabaseHelper.DATABASE_NAME, sql);
            final Cursor resultSet = database.rawQuery(sql, null);
            while (resultSet.moveToNext()) {
                contatto.setNome(resultSet.getString(Contatto.NOME));
                contatto.setDataNascita(resultSet.getString(Contatto.DATA_NASCITA));
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

    @Override
    public boolean delete(Object entity, String query) {
        try {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Contatto contatto = Contatto.class.cast(entity);
            String sql = query.replace("#TABLENAME#", Contatto.TABLE_NAME).replace("#IDCONTATTO#", contatto.getId());

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
