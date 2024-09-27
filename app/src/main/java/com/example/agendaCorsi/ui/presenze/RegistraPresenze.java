package com.example.agendaCorsi.ui.presenze;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.ContattoIscritto;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class RegistraPresenze extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport, statoCorso, tipoCorso;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TableLayout tabellaContattiIscritti, headerTabellaContattiIscritti;
    Context registraPresenze;

    int larghezzaColonna1, larghezzaColonna2, larghezzaColonna3, larghezzaColonna4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registra_presenze);

        registraPresenze = this;
        makeToolBar(registraPresenze);

        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");
        statoCorso = intent.getStringExtra("statoCorso");
        tipoCorso = intent.getStringExtra("tipoCorso");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);

        headerTabellaContattiIscritti = findViewById(R.id.headerTabellaContattiIscrivibili);
        tabellaContattiIscritti = findViewById(R.id.tabellaContattiIscritti);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.1);
        larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.067);
        larghezzaColonna4 = (int) (displayMetrics.widthPixels * 0.067);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        makeIntestazioneTabella();
        loadContattiIscritti();

        listenerEsci(registraPresenze, ElencoFasceCorsiRunning.class, null);
    }


    private void loadContattiIscritti() {

        List<Object> contattiIscrittiList = ContattiDAO.getInstance().getIscrittiRunning(idFascia, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTI_ISCRITTI_RUNNING));

        for (Object object : contattiIscrittiList) {
            ContattoIscritto contattoIscritto = (ContattoIscritto) object;

            /*
             Per evitare di avere sull'elenco tutte le presenza anche vecchie, le filtro e presento solo
             quelle del giorno corrente
             */

            String dataConfermaPresenza = String.valueOf(contattoIscritto.getDataConfermaPresenza());
            String dataConfermaAssenza = String.valueOf(contattoIscritto.getDataConfermaAssenza());
            String oggi = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            if (dataConfermaPresenza.contains(oggi)
                    || (dataConfermaAssenza.contains(oggi))
                    || ((dataConfermaAssenza.equals("")) && (dataConfermaPresenza.equals("")))
            ) {
                String detailType = "";
                if (contattoIscritto.getStato().equals(STATO_CHIUSO) || contattoIscritto.getStatoElemento().equals(STATO_ESAURITO)) {
                    detailType = DETAIL_CLOSED;
                } else {
                    detailType = DETAIL_SIMPLE;
                }

                tableRow = new TableRow(this);
                tableRow.setClickable(true);

                tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna1, contattoIscritto.getNomeContatto(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
                tableRow.addView(makeCell(this, new TextView(this), detailType, larghezzaColonna2, String.valueOf(computeAge(contattoIscritto.getDataNascita())), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

                Map<String, String> intentMap = new ArrayMap<>();
                intentMap.put("descrizioneCorso", descrizioneCorso);
                intentMap.put("descrizioneFascia", descrizioneFascia);
                intentMap.put("giornoSettimana", giornoSettimana);
                intentMap.put("sport", sport);
                intentMap.put("idCorso", idCorso);
                intentMap.put("idFascia", idFascia);
                intentMap.put("idIscrizione", contattoIscritto.getIdIscrizione());
                intentMap.put("statoCorso", statoCorso);
                intentMap.put("nomeIscritto", contattoIscritto.getNomeContatto());
                intentMap.put("statoIscrizione", contattoIscritto.getStato());
                intentMap.put("tipoCorso", tipoCorso);
                intentMap.put("idElemento", contattoIscritto.getIdElemento());

                if ((contattoIscritto.getStato().equals(STATO_ATTIVA) || (contattoIscritto.getStato().equals(STATO_USATA)))
                && contattoIscritto.getStatoElemento().equals(STATO_CARICO)) {

                    /*
                    Le due celle per la conferma della presenza o assenza sono mutualmente esclusive.
                    Una contatto iscritto potrà essere : presente, assente o ne uno ne l'altro a discrezione dell'istruttore.
                    Sia la presenza sia l'assenza vanno e decrementare il portfolio del contatto.
                     */

                    boolean isPresenzaNotExist = contattoIscritto.getIdPresenza().equals("");
                    boolean isAssenzaNotExist  = contattoIscritto.getIdAssenza().equals("");

                    boolean isPresenzaExist = !contattoIscritto.getIdPresenza().equals("");
                    boolean isAssenzaExist  = !contattoIscritto.getIdAssenza().equals("");

                    if (isPresenzaNotExist && isAssenzaNotExist) {
                        tableRow.addView(listenerOnConfermaPresenza(makeCell(registraPresenze, new TextView(this),
                                        detailType, larghezzaColonna3, "false", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE),
                                Integer.parseInt(contattoIscritto.getIdIscrizione()), this, intentMap));

                        tableRow.addView(listenerOnConfermaAssenza(makeCell(registraPresenze, new TextView(this),
                                        detailType, larghezzaColonna4, "false", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE),
                                Integer.parseInt(contattoIscritto.getIdIscrizione()), this, intentMap));
                    }
                    else if (isPresenzaExist && isAssenzaNotExist) {
                        tableRow.addView(listenerOnRimuoviPresenza(makeCell(registraPresenze, new TextView(this),
                                detailType, larghezzaColonna3, "true", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE),
                                Integer.parseInt(contattoIscritto.getIdIscrizione()), this, intentMap, contattoIscritto.getIdPresenza()));

                        tableRow.addView(makeCell(this, new TextView(this),
                                detailType, larghezzaColonna4, "", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
                    }
                    else if (isPresenzaNotExist && isAssenzaExist) {
                        tableRow.addView(makeCell(this, new TextView(this),
                                detailType, larghezzaColonna3, "", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));

                        tableRow.addView(listenerOnRimuoviAssenza(makeCell(registraPresenze, new TextView(this),
                                detailType, larghezzaColonna4, "true", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE),
                                Integer.parseInt(contattoIscritto.getIdIscrizione()), this, intentMap, contattoIscritto.getIdAssenza()));
                    }
                } else {
                    tableRow.addView(makeCell(this, new TextView(this),
                            detailType, larghezzaColonna3, "", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));

                    tableRow.addView(makeCell(this, new TextView(this),
                            detailType, larghezzaColonna3, "", View.TEXT_ALIGNMENT_TEXT_END, View.VISIBLE));
                }

                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdElemento(), 0, View.GONE));
                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdPresenza(), 0, View.GONE));
                tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, contattoIscritto.getIdIscrizione(), 0, View.GONE));

                tabellaContattiIscritti.addView(tableRow);

            }
        }
    }


    private void makeIntestazioneTabella() {
        tableRow = new TableRow(registraPresenze);
        tableRow.setClickable(false);
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna1,"Nome", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna2,"Età", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
        //tableRow.addView(makeCell(this,new TextView(this), HEADER, larghezzaColonna3,"Stato", View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

        ImageView viewAccept = new ImageView(this);
        viewAccept.setImageResource(R.drawable.baseline_check_24);
        viewAccept.setBaselineAlignBottom(true);
        tableRow.addView(viewAccept);

        ImageView viewClear = new ImageView(this);
        viewClear.setImageResource(R.drawable.baseline_clear_24);
        viewClear.setBaselineAlignBottom(true);
        tableRow.addView(viewClear);

        headerTabellaContattiIscritti.addView(tableRow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(RegistraPresenze.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
