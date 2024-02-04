package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.access.CorsoDAO;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.access.GiornoSettimanaDAO;
import com.example.agendaCorsi.database.table.Corso;
import com.example.agendaCorsi.database.table.Fascia;
import com.example.agendaCorsi.database.table.GiornoSettimana;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendaCorsi.ui.corsi.ModificaFascia;
import com.example.agendaCorsi.ui.iscrizioni.ElencoIscrizioni;
import com.example.agendacorsi.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
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
    public static String QUERY_GETALL_CORSI = "query_getall_corsi";
    public static String QUERY_GET_CORSO = "query_get_corso";
    public static String QUERY_DEL_CORSO = "query_del_corso";
    public static String QUERY_MOD_STATO_CORSO = "query_mod_stato_corso";
    public static String QUERY_MOD_CORSO = "query_mod_corso";
    public static String QUERY_INS_CORSO = "query_ins_corso";
    public static String QUERY_GETALL_CONTATTI = "query_getall_contatti";
    public static String QUERY_INS_CONTATTO = "query_ins_contatto";
    public static String QUERY_MOD_CONTATTO = "query_mod_contatto";
    public static String QUERY_GET_CONTATTO = "query_get_contatto";
    public static String QUERY_DEL_CONTATTO = "query_del_contatto";
    public static String QUERY_GET_ELEMENTS = "query_get_elements";
    public static String QUERY_INS_ELEMENTS = "query_ins_elements";
    public static String QUERY_MOD_ELEMENTS = "query_mod_elements";
    public static String QUERY_GET_ELEMENTO = "query_get_elemento";
    public static String QUERY_DEL_ELEMENTO = "query_del_elements";
    public static String QUERY_ISNEW_ELEMENTO = "query_isnew_elemento";
    public static String QUERY_INS_FASCIA = "query_ins_fascia";
    public static String QUERY_ISNEW_FASCIA = "query_isnew_fascia";
    public static String QUERY_GET_FASCIA = "query_get_fascia";
    public static String QUERY_MOD_FASCIA = "query_mod_fascia";
    public static String QUERY_DEL_FASCIA = "query_del_fascia";
    public static String QUERY_GET_CONTATTI_ISCRIVIBILI = "query_get_contatti_iscrivibili";
    public static String QUERY_GET_CONTATTI_ISCRIVIBILI_OPEN = "query_get_contatti_iscrivibili_open";
    public static String QUERY_INS_ISCRIZIONE = "query_ins_iscrizione";
    public static String QUERY_GETALL_GIORNI_SETTIMANA = "query_getall_giorni_settimana";
    public static String QUERY_GET_CONTATTI_ISCRITTI = "query_get_contatti_iscritti";
    public static String QUERY_GET_CONTATTI_ISCRITTI_RUNNING = "query_get_contatti_iscritti_running";
    public static String QUERY_GET_CREDENZIALE = "query_get_credenziale";
    public static String QUERY_INS_CREDENZIALE = "query_ins_credenziale";
    public static String QUERY_MOD_CREDENZIALE = "query_mod_credenziale";
    public static String QUERY_DEL_CREDENZIALE = "query_del_credenziale";
    public static String QUERY_GET_FASCE_CORSO = "query_get_fasce_corso";
    public static String QUERY_MOD_STATO_ISCRIZIONE = "query_mod_stato_iscrizione";
    public static String QUERY_DEL_ISCRIZIONE = "query_del_iscrizione";
    public static String QUERY_MOD_ISCRIZIONE = "query_mod_iscrizione";
    public static String QUERY_MOD_STATO_ISCRIZIONI_ELEMENTO = "query_mod_stato_iscrizioni_elemento";
    public static String QUERY_GETALL_FASCE_DISPONIBILI = "query_getall_fasce_disponibili";
    public static String QUERY_GETALL_ELEMENTO = "query_getall_elemento";
    public static String QUERY_INS_PRESENZA = "query_ins_presenza";
    public static String QUERY_DEL_PRESENZA = "query_del_presenza";
    public static String QUERY_MOD_NUMERO_LEZIONI = "query_mod_numero_lezioni";
    public static String QUERY_GET_GIORNO_SETTIMANA = "query_get_giorno_settimana";

    public static String QUERY_TOT_ISCRIZIONI = "query_tot_iscrizioni";
    public static String QUERY_INS_TOTALE_CORSO = "query_ins_totale_corso";
    public static String QUERY_GETALL_TOT_ISCRIZIONI = "query_getall_tot_iscrizioni";
    /*
     * I bottoni
     */
    public Button annulla, esci, elimina, salva, chiudi, sospendi, apri, inserisci, ricarica5, ricarica10, sposta;
    public TableRow tableRow;
    public PropertyReader propertyReader;
    public Properties properties;

    public TextView makeCell(Context context, TextView name, String type, int width, String value, int alignment, int visibility) {
        //name = new TextView(context);
        name.setTextSize(16);
        name.setPadding(10,10,10,10);
        name.setBackground(ContextCompat.getDrawable(context, R.drawable.cell_border));

        if (!type.equals(DETAIL_CLOSED) && !type.equals(HEADER) && !type.equals(HEADER_EVIDENCE)) {
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

                Fascia fascia = new Fascia(String.valueOf(idCellaSelezionata), null, null, null, null, null, null, null, null);
                FasciaDAO.getInstance().select(fascia, QueryComposer.getInstance().getQuery(QUERY_GET_FASCIA));

                intent.putExtra("capienza", fascia.getCapienza());
                intent.putExtra("descrizioneFascia", fascia.getOraInizio() + "-" + fascia.getOraFine());

                GiornoSettimana giornoSettimana = new GiornoSettimana(fascia.getGiornoSettimana(), null, null);
                GiornoSettimanaDAO.getInstance().select(giornoSettimana, QueryComposer.getInstance().getQuery(QUERY_GET_GIORNO_SETTIMANA));

                intent.putExtra("giornoSettimana", giornoSettimana.getNomeGiornoEsteso());

                Corso corso = new Corso(fascia.getIdCorso(), null, null, null, null, null, null, null, null);
                CorsoDAO.getInstance().select(corso, QueryComposer.getInstance().getQuery(QUERY_GET_CORSO));

                intent.putExtra("sport", corso.getSport());
                intent.putExtra("idCorso", corso.getIdCorso());
                intent.putExtra("statoCorso", corso.getStato());
                intent.putExtra("tipoCorso", corso.getTipo());

                startActivity(intent);
                finish();
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

                Fascia fascia = new Fascia(String.valueOf(idCellaSelezionata), null, null, null, null, null, null, null, null);
                FasciaDAO.getInstance().select(fascia, QueryComposer.getInstance().getQuery(QUERY_GET_FASCIA));

                intent.putExtra("idCorso", fascia.getIdCorso());
                intent.putExtra("oraInizioFascia", fascia.getOraInizio());
                intent.putExtra("oraFineFascia", fascia.getOraFine());

                startActivity(intent);
                finish();
            }
        });
        return name;
    }

    public Toast makeToastMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        toastView.setBackground(ContextCompat.getDrawable(context, R.drawable.toast_bg_gradient));

        TextView text = (TextView) toastView.findViewById(android.R.id.message);
        text.setTextColor(Color.BLACK);
        text.setTextSize(16);

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
                makeSalva();
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


    public void makeAnnulla() {}

    public void makeElimina() {}

    public void makeSalva() {}

    public void makeApri() {}

    public void makeSospendi() {}

    public void makeChiudi() {}

    public void makeRicarica(int value) {}

    public void makeSpostaIscrizione(String idIscrizione) {}
}
