package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendaCorsi.ui.iscrizioni.ElencoIscrizioni;
import com.example.agendacorsi.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class FunctionBase extends AppCompatActivity {

    public static int versionCode;
    public static String versionName;
    public static Long MILLI_SECONDS_YEAR = 31558464000L;
    /*
     * Gli sport
     */
    public static String Skate = "SKATE";
    public static String Basket = "BASKET";
    public static String Pallavolo = "PALLAVOLO";
    public static String Pattini = "PATTINI";

    public static String Produzione = "PROD";
    public static String Test = "TEST";
    /*
     * Cell formats
     */
    public static String DETAIL = "Aperto";
    public static String DETAIL_SIMPLE = "None";
    public static String DETAIL_EVIDENCE = "DTEV";
    public static String DETAIL_CLOSED = "Chiuso";
    public static String DETAIL_OPENED = "Aperto";
    public static String DETAIL_CONFIRMED = "Confirmed";
    public static String DETAIL_EXHAUSTED = "Esaurito";
    public static String DETAIL_EXPIRED = "Scaduto";
    public static String DETAIL_SUSPENDED = "Sospeso";
    public static String DETAIL_INOPERATIVE = "";
    public static String HEADER = "HD";
    public static String HEADER_EVIDENCE = "HDEV";
    public static String HEADER_OFF = "HDOFF";
    /*
     * Gli stati
     */
    public static String STATO_APERTO = "Aperto";
    public static String STATO_CHIUSO = "Chiuso";
    public static String STATO_CHIUSA = "Chiusa";
    public static String STATO_ATTIVO = "Attivo";
    public static String STATO_ATTIVA = "Attiva";
    public static String STATO_DISATTIVA = "Disattiva";
    public static String STATO_SOSPESO = "Sospeso";
    public static String STATO_CARICO = "Carico";
    public static String STATO_SCADUTO = "Scaduto";
    public static String STATO_ESAURITO = "Esaurito";
    /*
     * Le query
     */
    public static String QUERY_TOTALS_CORSI = "query_totals_corsi";
    public static String QUERY_GETALL_FASCE_CORSI = "query_getall_fasce_corsi";
    public static String QUERY_GETALL_FASCE_CORSI_RUNNING = "query_getall_fasce_corsi_running";
    public static String QUERY_GET_CORSO = "query_get_corso";

    public static String QUERY_GET_ELEMENTO = "query_get_elemento";
    public static String QUERY_ISNEW_FASCIA = "query_isnew_fascia";
    public static String QUERY_GET_FASCIA = "query_get_fascia";
    public static String QUERY_MOD_FASCIA = "query_mod_fascia";
    public static String QUERY_DEL_FASCIA = "query_del_fascia";
    public static String QUERY_GET_CONTATTI_ISCRIVIBILI = "query_get_contatti_iscrivibili";
    public static String QUERY_GET_CONTATTI_ISCRIVIBILI_OPEN = "query_get_contatti_iscrivibili_open";
    public static String QUERY_GET_ISCRIZIONE = "query_get_iscrizione";
    public static String QUERY_GETALL_GIORNI_SETTIMANA = "query_getall_giorni_settimana";
    public static String QUERY_GET_CONTATTI_ISCRITTI = "query_get_contatti_iscritti";
    public static String QUERY_GET_CONTATTI_ISCRITTI_RUNNING = "query_get_contatti_iscritti_running";
    public static String QUERY_GET_CREDENZIALE = "query_get_credenziale";
    public static String QUERY_INS_CREDENZIALE = "query_ins_credenziale";
    public static String QUERY_MOD_CREDENZIALE = "query_mod_credenziale";
    public static String QUERY_DEL_CREDENZIALE = "query_del_credenziale";
    public static String QUERY_MOD_STATO_ISCRIZIONE = "query_mod_stato_iscrizione";
    public static String QUERY_DEL_ISCRIZIONE = "query_del_iscrizione";
    public static String QUERY_MOD_ISCRIZIONE = "query_mod_iscrizione";
    public static String QUERY_GETALL_FASCE_DISPONIBILI = "query_getall_fasce_disponibili";
    public static String QUERY_INS_PRESENZA = "query_ins_presenza";
    public static String QUERY_DEL_PRESENZA = "query_del_presenza";
    public static String QUERY_MOD_NUMERO_LEZIONI = "query_mod_numero_lezioni";
    public static String QUERY_GET_GIORNO_SETTIMANA = "query_get_giorno_settimana";
    public static String QUERY_GETALL_TOT_ISCRIZIONI = "query_getall_tot_iscrizioni";
    /*
     * I bottoni
     */
    public ImageButton esci, inserisci, annulla, salva, elimina, chiudi, sospendi, apri, ricarica5, ricarica10, sposta;
    public TableRow tableRow;
    public PropertyReader propertyReader;
    public Properties properties;

    public void makeToolBar(Context context) {

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Agenda " + packageInfo.versionName);

    }


    public TextView makeCell(Context context, TextView name, String type, int width, String value, int alignment, int visibility) {
        //name = new TextView(context);
        name.setTextSize(16);
        name.setPadding(10,10,10,10);
        name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border));

        if (!type.equals(DETAIL_CLOSED) && !type.equals(HEADER) && !type.equals(HEADER_EVIDENCE) && !type.equals(HEADER_OFF)) {
            if (type.equals(DETAIL_EXHAUSTED)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_exhausted));

            } else if (type.equals(DETAIL_EXPIRED)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_expired));

            } else if (type.equals(DETAIL_INOPERATIVE)) {
                name.setTextColor(getResources().getColor(R.color.grayligth, getTheme()));
                name.setTypeface(Typeface.DEFAULT_BOLD);

            } else if (type.equals(DETAIL_CONFIRMED)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_confirmed));

            } else if (type.equals(DETAIL_OPENED)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_opened));

            } else if (type.equals(DETAIL_EVIDENCE)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_heading_evidence));
            }
            else if (type.equals(DETAIL_SUSPENDED)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_closed));
            }
            else {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border));
            }
        } else if (type.equals(DETAIL_CLOSED)) {
            name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_closed));
        } else {
            if (type.equals(HEADER)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_heading));
                name.setTextColor(getResources().getColor(R.color.black, getTheme()));
                name.setTypeface(null, Typeface.BOLD);
            } else if (type.equals(HEADER_EVIDENCE)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_heading_evidence));
                name.setTextColor(getResources().getColor(R.color.black, getTheme()));
                name.setTypeface(null, Typeface.BOLD);
            } else if (type.equals(HEADER_OFF)) {
                name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border_heading_off));
                name.setTextColor(getResources().getColor(R.color.black, getTheme()));
                name.setTypeface(null, Typeface.BOLD);
            }
        }

        name.setTextColor(getResources().getColor(R.color.black, getTheme()));
        name.setTextAlignment(alignment);
        name.setGravity(Gravity.CENTER);
        name.setVisibility(visibility);
        name.setText(value);
        name.setWidth(width);

        return name;
    }

    public TextView listenerOnTotale(TextView name, int id, String corso, String totale, Context context) {
        name.setId(id);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCellaSelezionata = view.getId();      // l'id della cella è stato settato con l'id della fascia

                view.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_bg_gradient));

                Intent intent = new Intent(context, ElencoIscrizioni.class);
                intent.putExtra("idFascia", String.valueOf(idCellaSelezionata));
                intent.putExtra("descrizioneCorso", corso);
                intent.putExtra("totaleFascia", totale);

                List<Row> rows = null;

                try {
                    rows = ConcreteDataAccessor.getInstance().read(Fascia.TABLE_NAME,
                            null,
                            new Row(Fascia.fasciaColumns.get(Fascia.ID_FASCIA), idCellaSelezionata),
                            null);

                    for (Row row : rows) {
                        intent.putExtra("capienza", row.getColumnValue(Fascia.fasciaColumns.get(Fascia.CAPIENZA)).toString());
                        intent.putExtra("descrizioneFascia",
                                row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString() + "-" +
                                        row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString());

                        List<Row> giorniSettimana = ConcreteDataAccessor.getInstance().read(GiornoSettimana.TABLE_NAME,
                                new String[]{GiornoSettimana.giornoSettimanaColumns.get(GiornoSettimana.NOME_GIORNO_ESTESO)},
                                new Row(Fascia.fasciaColumns.get(Fascia.NUMERO_GIORNO), row.getColumnValue(Fascia.fasciaColumns.get(Fascia.NUMERO_GIORNO))),
                                null);

                        for (Row giorno : giorniSettimana ) {
                            intent.putExtra("giornoSettimana", giorno.getColumnValue(GiornoSettimana.giornoSettimanaColumns.get(GiornoSettimana.NOME_GIORNO_ESTESO)).toString());
                        }

                        List<Row> corsi = ConcreteDataAccessor.getInstance().read(Corso.TABLE_NAME,
                                null,
                                new Row(Corso.corsoColumns.get(Corso.ID_CORSO), row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_CORSO))),
                                null);

                        for (Row corso : corsi) {
                            intent.putExtra("sport", corso.getColumnValue(Corso.corsoColumns.get(Corso.SPORT)).toString());
                            intent.putExtra("idCorso", corso.getColumnValue(Corso.corsoColumns.get(Corso.ID_CORSO)).toString());
                            intent.putExtra("statoCorso", corso.getColumnValue(Corso.corsoColumns.get(Corso.STATO)).toString());
                            intent.putExtra("tipoCorso", corso.getColumnValue(Corso.corsoColumns.get(Corso.TIPO)).toString());
                        }
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return name;
    }

    public TextView listenerOnCorso(TextView name, int id, Context context) {
        name.setId(id);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCellaSelezionata = view.getId();      // l'id della cella è stato settato con l'id del corso

                view.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_bg_gradient));

                Intent intent = new Intent(context, ModificaCorso.class);
                intent.putExtra("idCorso", String.valueOf(idCellaSelezionata));

                startActivity(intent);
                finish();
            }
        });
        return name;
    }

    public TextView listenerOnFascia(TextView name, int id, String corso, Context context) {
        name.setId(id);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idCellaSelezionata = view.getId();      // l'id della cella è stato settato con l'id della fascia

                view.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_bg_gradient));

                Intent intent = new Intent(context, ModificaCorso.class);
                intent.putExtra("idFascia", String.valueOf(idCellaSelezionata));
                intent.putExtra("descrizioneCorso", corso);

                List<Row> rows = null;

                try {
                    rows = ConcreteDataAccessor.getInstance().read(Fascia.TABLE_NAME,
                            null,
                            new Row(Fascia.fasciaColumns.get(Fascia.ID_FASCIA), idCellaSelezionata),
                            null);

                    for (Row row : rows) {
                        intent.putExtra("idCorso", row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_CORSO)).toString());
                        intent.putExtra("oraInizioFascia", row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString());
                        intent.putExtra("oraFineFascia", row.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString());
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(intent);
                finish();
            }
        });
        return name;
    }

    public Toast makeToastMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        return toast;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem contattiItem = menu.findItem(R.id.navigation_contatti);
        contattiItem.setVisible(false);

        MenuItem corsiItem = menu.findItem(R.id.navigation_corsi);
        corsiItem.setVisible(false);

        MenuItem iscrizioniItem = menu.findItem(R.id.navigation_iscrizioni);
        iscrizioniItem.setVisible(false);

        MenuItem presenzeItem = menu.findItem(R.id.navigation_presenze);
        presenzeItem.setVisible(false);

        MenuItem totaliItem = menu.findItem(R.id.navigation_totali);
        totaliItem.setVisible(false);

        MenuItem exitItem = menu.findItem(R.id.navigation_esci);
        exitItem.setVisible(false);

        return true;
    }

    public void displayAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(context, R.style.Theme_InfoDialog);
        messaggio.setTitle(title);
        messaggio.setMessage(message);
        messaggio.setCancelable(false);
        messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog ad = messaggio.create();
        ad.show();
    }

    public String dateFormat(String date, String patternFrom, String patternTo) {
        try {
            Date dateParse = new SimpleDateFormat(patternFrom).parse(date);
            return new SimpleDateFormat(patternTo).format(dateParse);
        }
        catch (ParseException e) {
            Log.e(DatabaseHelper.DATABASE_NAME, Objects.requireNonNull(e.getMessage()));
            return "";
        }

    }

    public void listenerEsci(Context contextFrom, Class classDestination, Map<String, String> intentMap) {
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contextFrom, classDestination);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void listenerAnnulla() {
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnnulla();
            }
        });
    }

    public void listenerElimina() {
        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeElimina();
            }
        });
    }

    public void listenerInserisci(Context contextFrom, Class classDestination, Map<String, String> intentMap) {
        inserisci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contextFrom, classDestination);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void listenerSalva() {
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    makeSalva();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void listenerChiudi() {
        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChiudi();
            }
        });
    }

    public void listenerSospendi() {
        sospendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSospendi();
            }
        });
    }

    public void listenerApri() {
        apri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeApri();
            }
        });
    }

    public void listenerTableRow(Context contextFrom, Class classDestination, String idName, Map<String, String> intentMap, Integer idColNum) {
        tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableRow tableRow = (TableRow) view;
                TextView textView = (TextView) tableRow.getChildAt(idColNum);
                String idSelezionato = textView.getText().toString();

                tableRow.setBackground(ContextCompat.getDrawable(contextFrom, R.drawable.cell_bg_gradient));

                /**
                 * Passo alla classe destinazione l'id della riga selezionata più tutti gli item inseriti nella intentMap
                 */
                Intent intent = new Intent(contextFrom, classDestination);
                intent.putExtra(idName, idSelezionato);
                if (intentMap != null) {
                    for (Map.Entry<String, String> entry : intentMap.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue());
                    }
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void listenerRicarica5() {
        ricarica5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRicarica(5);
            }
        });
    }

    public void listenerRicarica10() {
        ricarica10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRicarica(10);
            }
        });
    }

    public void listenerSpostaIscrizione(String idIscrizione) {
        sposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSpostaIscrizione(idIscrizione);
            }
        });
    }
    public void updateLabel(EditText data, Calendar calendar){
        String myFormat="dd-MM-yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        data.setText(dateFormat.format(calendar.getTime()));
    }

    public boolean isNumberOfWeek(Integer dayNumber) {
        if ((dayNumber >= 1) && (dayNumber <= 7)) {
            return true;
        }
        return false;
    }

    public static String rightPad(String input, int length, String fill){
        String pad = input.trim() + String.format("%"+length+"s", "").replace(" ", fill);
        return pad.substring(0, length);
    }

    public static String leftPad(String input, int length, String fill){
        String pad = String.format("%"+length+"s", "").replace(" ", fill) + input.trim();
        return pad.substring(pad.length() - length, pad.length());
    }

    public boolean isFasciaCapiente(String totaleIscrizioni, String capienza) {
        if (totaleIscrizioni == null || capienza == null) {
            return false;
        } else {
            if (Integer.parseInt(totaleIscrizioni) < Integer.parseInt(capienza)) {
                return true;
            }
        }
        return false;
    }

    public int computeAge(String sDate) {
        try {
            Date dbDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dbDate = (Date)dateFormat.parse(sDate);
            long timeDiff = System.currentTimeMillis() - dbDate.getTime();
            int age = (int)(timeDiff / MILLI_SECONDS_YEAR);
            return age;
        }
        catch(ParseException e) {
            Log.e("AgendaCorsi","Can not compute age from date:"+sDate,e);
            return 0;
        }
    }

    public boolean checkObsolescenceDate(String dataFineValidita) {
        try {
            Date fineDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            fineDate = (Date)dateFormat.parse(dataFineValidita.replaceAll("#", ""));

            long now = System.currentTimeMillis();

            if (now > fineDate.getTime()) {
                return true;        // ok, la data è obsolescente
            }
        }
        catch(ParseException e) {
            Log.e("AgendaCorsi","Can not check course date:",e);
            return false;
        }
        return false;
    }

    public String addYearToDate(String data, int yearQuantity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.YEAR, yearQuantity);
        Date modifiedDate = cal.getTime();

        return dateFormat.format(modifiedDate);
    }

    public int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance(new Locale("en","UK"));
        calendar.setTime(new Date());
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (weekDay == 0) {return 7;} else {return weekDay;}
    }

    public boolean isFasciaRunning(String fascia) {
        String HHmm = new SimpleDateFormat("HH.mm").format(Calendar.getInstance().getTime());
        String[] estremiFascia = fascia.split("-");
        if (Float.parseFloat(HHmm) >= Float.parseFloat(estremiFascia[0]) &&
            Float.parseFloat(HHmm) <= Float.parseFloat(estremiFascia[1])) {
            return true;
        }
        return false;
    }

    public String coalesceValue(Object object) {
        String value = (String) object;
        if (value == null) {
            value = "";
        }
        return value;
    }


    public boolean isFasciaNonSovrapposta(String idCorso, String idFascia, String giornoSettimana, String _oraI, String _oraF) {
        try {
            Float _oraInizio = Float.parseFloat(_oraI);
            Float _oraFine = Float.parseFloat(_oraF);
            idFascia = (idFascia == null) ? "" : idFascia;

            Row selectColumn = new Row();
            selectColumn.addColumn(Fascia.fasciaColumns.get(Fascia.ID_CORSO), idCorso);
            selectColumn.addColumn(Fascia.fasciaColumns.get(Fascia.GIORNO_SETTIMANA), giornoSettimana);

            List<Row> fasceCorso = ConcreteDataAccessor.getInstance().read(Fascia.TABLE_NAME,
                    null,
                    new Row(Fascia.fasciaColumns.get(Fascia.ID_CORSO), idCorso),
                    null);

            for (Row fascia : fasceCorso) {
                if (!fascia.getColumnValue(Fascia.fasciaColumns.get(Fascia.ID_FASCIA).toString()).equals(idFascia)) {
                    Float oraInizio = Float.parseFloat(fascia.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_INIZIO)).toString());
                    Float oraFine = Float.parseFloat(fascia.getColumnValue(Fascia.fasciaColumns.get(Fascia.ORA_FINE)).toString());
                    if ((oraInizio >= _oraInizio && oraInizio <= _oraFine) || (oraFine >= _oraInizio && _oraFine <= _oraFine)) {
                        return false;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public void makeAnnulla() {}

    public void makeElimina() {}

    public void makeSalva() {}

    public void makeApri() {}

    public void makeSospendi() {}

    public void makeChiudi() {}

    public void makeRicarica(int value) {}

    public void makeSpostaIscrizione(String idIscrizione) {}
}
