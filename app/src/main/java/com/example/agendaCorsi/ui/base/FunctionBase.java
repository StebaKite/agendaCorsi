package com.example.agendaCorsi.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.database.DatabaseHelper;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.table.FasciaCorso;
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
     * Gli stati
     */
    public static String STATO_APERTO = "Aperto";
    public static String STATO_CHIUSO = "Chiuso";
    public static String STATO_ATTIVO = "Attivo";
    public static String STATO_SOSPESO = "Sospeso";
    /*
     * Le query
     */
    public static String QUERY_TOTALS_CORSI = "query_totals_corsi";
    public static String QUERY_GETALL_FASCE_CORSI = "query_getall_fasce_corsi";
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
    public static String QUERY_GET_CREDENZIALE = "query_get_credenziale";
    public static String QUERY_INS_CREDENZIALE = "query_ins_credenziale";
    public static String QUERY_MOD_CREDENZIALE = "query_mod_credenziale";
    public static String QUERY_DEL_CREDENZIALE = "query_del_credenziale";
    public static String QUERY_GET_FASCE_CORSO = "query_get_fasce_corso";
    public static String QUERY_MOD_STATO_ISCRIZIONE = "query_mod_stato_iscrizione";
    public static String QUERY_DEL_ISCRIZIONE = "query_del_iscrizione";
    public static String QUERY_MOD_ISCRIZIONE = "query_mod_iscrizione";
    /*
     * I bottoni
     */
    public Button annulla, esci, elimina, salva, chiudi, sospendi, apri, inserisci, ricarica5, ricarica10, sposta;
    public TableRow tableRow;
    public PropertyReader propertyReader;
    public Properties properties;
    public Mail mail;

    public void displayAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(context, R.style.Theme_AlertDialog);
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
        ricarica10.setOnClickListener(new View.OnClickListener() {
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

    public boolean sendEmail(String mailSubject, String body, String mailTo){
        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");

        mail = Mail.getInstance();

        String[] toArr = {mailTo};
        mail.setTo(toArr);
        mail.setFrom(properties.getProperty("EMAIL_FROM_ADDRESS"));
        mail.setSubject(mailSubject);
        mail.setBody(body);

        try {
            if(mail.send()) {
                return true;
            } else {
                return false;
            }
        } catch(Exception e) {
            return false;
        }
    }

    public void loadFasceOrarie(Class classTo, Map<String, String> intentMap, TableLayout tableLayout,
                                String idCorso) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.3);
        int larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.2);

        List<Object> fasceCorsoList = FasciaDAO.getInstance().getFasceCorso(idCorso, QueryComposer.getInstance().getQuery(QUERY_GET_FASCE_CORSO));

        for (Object object : fasceCorsoList) {
            FasciaCorso fasciaCorso = (FasciaCorso) object;

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            TextView giorno_settimana = new TextView(AgendaCorsiApp.getContext());
            giorno_settimana.setTextSize(16);
            giorno_settimana.setPadding(10,20,10,20);
            giorno_settimana.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            giorno_settimana.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            giorno_settimana.setGravity(Gravity.CENTER);
            giorno_settimana.setText(String.valueOf(fasciaCorso.getGiornoSettimana()));
            giorno_settimana.setWidth(larghezzaColonna1);
            tableRow.addView(giorno_settimana);

            TextView fascia_oraria = new TextView(AgendaCorsiApp.getContext());
            fascia_oraria.setTextSize(16);
            fascia_oraria.setPadding(10,20,10,20);
            fascia_oraria.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            fascia_oraria.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            fascia_oraria.setGravity(Gravity.CENTER);
            fascia_oraria.setText(fasciaCorso.getDescrizioneFascia());
            fascia_oraria.setWidth(larghezzaColonna2);
            tableRow.addView((fascia_oraria));

            TextView capienza = new TextView(AgendaCorsiApp.getContext());
            capienza.setTextSize(16);
            capienza.setPadding(10,20,10,20);
            capienza.setBackground(ContextCompat.getDrawable(AgendaCorsiApp.getContext(), R.drawable.cell_border));
            capienza.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            capienza.setGravity(Gravity.CENTER);
            capienza.setText(fasciaCorso.getCapienza());
            capienza.setWidth(larghezzaColonna3);
            tableRow.addView((fascia_oraria));

            TextView id_fascia = new TextView(AgendaCorsiApp.getContext());
            id_fascia.setText(String.valueOf(fasciaCorso.getIdFascia()));
            id_fascia.setVisibility(View.INVISIBLE);
            tableRow.addView(id_fascia);

            listenerTableRow(AgendaCorsiApp.getContext(), classTo, "idFascia", intentMap, 3);
            tableLayout.addView(tableRow);
        }
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
