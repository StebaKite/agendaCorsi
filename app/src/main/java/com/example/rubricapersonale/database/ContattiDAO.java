package com.example.rubricapersonale.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContattiDAO {

    private final DatabaseHelper databaseHelper;

    public ContattiDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public List<Contatto> getAll() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Contatto> list = new ArrayList<>();

        String sql = String.format("select " +
                "contatti.id, " +
                "contatti.nome, " +
                "contatti.indirizzo, " +
                "contatti.telefono, " +
                "contatti.email " +
                "from contatti");

        Log.i(DatabaseHelper.DATABASE_NAME, sql);
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = String.valueOf(cursor.getInt(Contatto.ID));
            String nome = cursor.getString(Contatto.NOME);
            String indirizzo = cursor.getString(Contatto.INDIRIZZO);
            String telefono = cursor.getString(Contatto.TELEFONO);
            String email = cursor.getString(Contatto.EMAIL);
            Contatto contatti = new Contatto(id, nome, indirizzo, telefono, email);
            list.add(contatti);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();
        return list;
    }
}
