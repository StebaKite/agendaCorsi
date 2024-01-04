package com.example.agendaCorsi.database;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB = "contattiPersonali";
    public static final int SCHEMA_VERSION = 3;
    public static final String DATABASE_NAME = "contattiPersonali.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    /**
     * onCreate() is only run when the database file did not exist and was just created.
     * If onCreate() returns successfully (doesn't throw an exception), the database is assumed
     * to be created with the requested version number.
     * As an implication, you should not catch SQLExceptions in onCreate() yourself.
     */
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(DatabaseHelper.DATABASE_NAME, "OnCreate invoked!");
        sqLiteDatabase.execSQL(getContattoTableStructure());
        sqLiteDatabase.execSQL(getElementoPortfolioStructure());
    }

    @Override
    /**
     * onUpgrade() is only called when the database file exists
     * but the stored version number is lower than requested in the constructor.
     * The onUpgrade() should update the table schema to the requested version.
     *
     * When changing the table schema in code (onCreate()), you should make sure the database is updated.
     * Increment the database version so that onUpgrade() is invoked.
     * This is slightly more complicated as more code is needed.
     * For released versions, you should implement data migration in onUpgrade() so your users don't lose their data.
     *
     */
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(DatabaseHelper.DATABASE_NAME, "OnUpgrade invoked!");
        /*
         *  Sequenza di DROP secondo le constraint dello schema (vedi modello ER)
         */
        sqLiteDatabase.execSQL("drop table " + ElementoPortfolio.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table " + Contatto.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table " + Corso.TABLE_NAME);
        sqLiteDatabase.execSQL("drop table " + Fascia.TABLE_NAME);
        /*
         * Sequenza di CREATE secondo le constraint dello schema (vedi modello ER)
         */
        sqLiteDatabase.execSQL(getContattoTableStructure());
        sqLiteDatabase.execSQL(getElementoPortfolioStructure());
        sqLiteDatabase.execSQL(getCorsoTableStructure());
        sqLiteDatabase.execSQL(getFasciaTableStructure());
    }

    private String getContattoTableStructure() {
        return  "create table if not exists " + Contatto.TABLE_NAME + " (" +
                "id_contatto integer primary key autoincrement, " +
                "nome text, " +
                "indirizzo text, " +
                "telefono text, " +
                "email text, " +
                "data_creazione text, " +
                "data_ultimo_aggiornamento text)";
    }

    private String getElementoPortfolioStructure() {
        return  "create table if not exists " + ElementoPortfolio.TABLE_NAME + " (" +
                "id_elemento integer primary key autoincrement, " +
                "id_contatto integer, " +
                "descrizione text, " +
                "sport text, " +
                "numero_lezioni integer, " +
                "data_ultima_ricarica text, " +
                "stato text, " +
                "data_creazione text, " +
                "data_ultimo_aggiornamento text, " +
                "constraint fk_contatto foreign key (id_contatto) references contatto (id_contatto) on delete cascade)";
    }

    private String getCorsoTableStructure() {
        return  "create table if not exists " + Corso.TABLE_NAME + " (" +
                "id_corso integer primary key autoincrement, " +
                "descrizione text, " +
                "sport text, " +
                "stato text, " +
                "data_inizio_validita text, " +
                "data_fine_validita, " +
                "data_creazione text, " +
                "data_ultimo_aggiornamento text)";
    }

    private String getFasciaTableStructure() {
        return  "create table if not exists " + Fascia.TABLE_NAME + " (" +
                "id_fascia integer primary key autoincrement, " +
                "id_corso integer, " +
                "descrizione text, " +
                "giorno_settimana text, " +
                "ora_inizio text, " +
                "ora_fine text, " +
                "capienza text, " +
                "data_creazione text, " +
                "data_ultimo_aggiornamento text, " +
                "constraint fk_corso foreign key (id_corso) references corso (id_corso) on delete cascade)";
    }

}
