package com.example.agendaCorsi.ui.iscrizioni;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.FasciaDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ContattoIscrivibile;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.PropertyReader;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendacorsi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NuovaIscrizione extends FunctionBase {

    String idFascia, idCorso, descrizioneCorso, giornoSettimana, descrizioneFascia, sport;
    EditText _descrizioneCorso, _descrizioneFascia, _giornoSettimana;
    TextView nome_contatto, id_elemento;
    TableLayout _tabellaContattiIscrivibili;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_iscrizione);

        esci = findViewById(R.id.bExit);

        Intent intent = getIntent();
        idFascia = intent.getStringExtra("idFascia");
        descrizioneCorso = intent.getStringExtra("descrizioneCorso");
        descrizioneFascia = intent.getStringExtra("descrizioneFascia");
        giornoSettimana = intent.getStringExtra("giornoSettimana");
        sport = intent.getStringExtra("sport");
        idCorso = intent.getStringExtra("idCorso");

        _descrizioneCorso = findViewById(R.id.editDescrizione);
        _giornoSettimana = findViewById(R.id.editGiornoSettimana);
        _descrizioneFascia = findViewById(R.id.editFascia);
        _tabellaContattiIscrivibili = findViewById(R.id.tabellaContattiIscrivibili);

        _descrizioneCorso.setText(descrizioneCorso);
        _giornoSettimana.setText(giornoSettimana);
        _descrizioneFascia.setText(descrizioneFascia);

        loadContattiIscrvibili();

        listenerEsci(AgendaCorsiApp.getContext(), ElencoFasceCorsi.class, null);
    }

    private void loadContattiIscrvibili() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);
        int larghezzaColonna3 = (int) (displayMetrics.widthPixels * 0.3);

        propertyReader = new PropertyReader(AgendaCorsiApp.getContext());
        properties = propertyReader.getMyProperties("config.properties");

        List<Object> contattiIscrivibiliList = new ContattiDAO(this).getIscrivibili(idCorso, idFascia, sport, properties.getProperty(QUERY_GET_CONTATTI_ISCRIVIBILI));

        for (Object object : contattiIscrivibiliList) {
            ContattoIscrivibile contattoIscrivibile = ContattoIscrivibile.class.cast(object);

            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            nome_contatto = new TextView(this);
            nome_contatto.setTextSize(16);
            nome_contatto.setPadding(10,20,10,20);
            nome_contatto.setBackground(ContextCompat.getDrawable(NuovaIscrizione.this, R.drawable.cell_border));
            nome_contatto.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            nome_contatto.setGravity(Gravity.CENTER);
            nome_contatto.setText(String.valueOf(contattoIscrivibile.getNomeContatto()));
            nome_contatto.setWidth(larghezzaColonna1);
            tableRow.addView(nome_contatto);

            id_elemento = new TextView(this);
            id_elemento.setText(String.valueOf(contattoIscrivibile.getIdElemento()));
            id_elemento.setVisibility(View.INVISIBLE);
            tableRow.addView(id_elemento);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;
                    TextView textView = (TextView) tableRow.getChildAt(1);
                    String idSelezionato = textView.getText().toString();

                    Iscrizione iscrizione = new Iscrizione(null, idFascia, idSelezionato, "Attiva", null, null);
                    if (new IscrizioneDAO(AgendaCorsiApp.getContext()).insert(iscrizione, properties.getProperty(QUERY_INS_ISCRIZIONE))) {
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Inserimento fallita, contatta il supporto tecnico");
                    }
                }
            });
            _tabellaContattiIscrivibili.addView(tableRow);
        }
    }


}
