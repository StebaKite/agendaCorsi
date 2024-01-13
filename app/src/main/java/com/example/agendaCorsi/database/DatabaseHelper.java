package com.example.agendaCorsi.database;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.base.QueryComposer;

import java.util.Properties;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB = "contattiPersonali";
    public static final int SCHEMA_VERSION = 5;
    public static final String DATABASE_NAME = "contattiPersonali.db";

    public PropertyReader propertyReader;
    public Properties properties;

    public static String CREATE_TABLE_CONTATTO = "create_table_contatto";
    public static String CREATE_TABLE_ELEMENTO_PORTFOLIO = "create_table_elemento_portfolio";
    public static String CREATE_TABLE_CORSO = "create_table_corso";
    public static String CREATE_TABLE_FASCIA = "create_table_fascia";
    public static String CREATE_TABLE_ISCRIZIONE = "create_table_iscrizione";
    public static String CREATE_TABLE_GIORNO_SETTIMANA = "create_table_giorno_settimana";
    public static String QUERY_INS_GIORNO_SETTIMANA = "query_ins_giorno_settimana";

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
        /*
         * Sequenza di CREATE secondo le constraint dello schema (vedi modello ER)
         */
        sqLiteDatabase.execSQL(getContattoTableStructure());
        sqLiteDatabase.execSQL(getElementoPortfolioStructure());
        sqLiteDatabase.execSQL(getCorsoTableStructure());
        sqLiteDatabase.execSQL(getFasciaTableStructure());
        sqLiteDatabase.execSQL(getIscrizioneTableStructure());
        sqLiteDatabase.execSQL(getGiornoSettimanaTableStructure());
        /*
         * Initial Load
         */
        initialLoadGiornoSettimana(sqLiteDatabase);
    }

    @Override
    /**
     * onUpgrade() is only called when the database file exists
     * but the stored version number is lower than requested in the constructor.
     * The onUpgrade() should update the table schema to the requested version.
     * When changing the table schema in code (onCreate()), you should make sure the database is updated.
     * Increment the database version so that onUpgrade() is invoked.
     * This is slightly more complicated as more code is needed.
     * For released versions, you should implement data migration in onUpgrade() so your users don't lose their data.
     */

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(DatabaseHelper.DATABASE_NAME, "OnUpgrade invoked!");
        /*
         *  Sequenza di DROP secondo le constraint dello schema (vedi modello ER)
         */
        //sqLiteDatabase.execSQL("drop table if exists " + ElementoPortfolio.TABLE_NAME);
        //sqLiteDatabase.execSQL("drop table if exists " + Contatto.TABLE_NAME);
        //sqLiteDatabase.execSQL("drop table if exists " + Corso.TABLE_NAME);
        //sqLiteDatabase.execSQL("drop table if exists " + Fascia.TABLE_NAME);
        //sqLiteDatabase.execSQL("drop table if exists " + Iscrizione.TABLE_NAME);
        /*
         * Sequenza di CREATE secondo le constraint dello schema (vedi modello ER)
         */
        //sqLiteDatabase.execSQL(getContattoTableStructure());
        //sqLiteDatabase.execSQL(getElementoPortfolioStructure());
        //sqLiteDatabase.execSQL(getCorsoTableStructure());
        //sqLiteDatabase.execSQL(getFasciaTableStructure());
        sqLiteDatabase.execSQL(getIscrizioneTableStructure());
        sqLiteDatabase.execSQL(getGiornoSettimanaTableStructure());
        /*
         * Initial Load
         */
        initialLoadGiornoSettimana(sqLiteDatabase);
    }

    /**
     * Metodi di creazione delle tabelle
     */

    private String getContattoTableStructure() {
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_CONTATTO);
    }

    private String getElementoPortfolioStructure() {;
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_ELEMENTO_PORTFOLIO);
    }

    private String getCorsoTableStructure() {
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_CORSO);
    }

    private String getFasciaTableStructure() {
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_FASCIA);
    }

    private String getIscrizioneTableStructure() {
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_ISCRIZIONE);
    }

    private String getGiornoSettimanaTableStructure() {
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");
        return QueryComposer.getInstance().getQuery(CREATE_TABLE_GIORNO_SETTIMANA);
    }

    /**
     * Metodi per l'initial load di alcune tabelle.
     */
    private void initialLoadGiornoSettimana(SQLiteDatabase sqLiteDatabase) {
        Log.i(DatabaseHelper.DATABASE_NAME, "Table giorno_settimana initial load...");
        String query = QueryComposer.getInstance().getQuery(QUERY_INS_GIORNO_SETTIMANA);

        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","1").replace("#NGIOBV#","Lu").replace("#NGIOES#", "Lunedì"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","2").replace("#NGIOBV#","Ma").replace("#NGIOES#", "Martedì"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","3").replace("#NGIOBV#","Me").replace("#NGIOES#", "Mercoledì"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","4").replace("#NGIOBV#","Gi").replace("#NGIOES#", "Giovedì"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","5").replace("#NGIOBV#","Ve").replace("#NGIOES#", "Venerdì"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","6").replace("#NGIOBV#","Sa").replace("#NGIOES#", "Sabato"));
        sqLiteDatabase.execSQL(query.replace("#NUMGIO#","7").replace("#NGIOBV#","Do").replace("#NGIOES#", "Domenica"));
    }

}


