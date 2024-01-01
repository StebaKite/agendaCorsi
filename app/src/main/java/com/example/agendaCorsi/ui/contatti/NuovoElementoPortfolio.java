package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.agendaCorsi.database.ElementoPortfolio;
import com.example.agendaCorsi.database.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

public class NuovoElementoPortfolio extends FunctionBase {

    int idContatto;
    String nomeContatto;
    Button cancella, esci, salva;
    TextView labelScheda, descrizione, numeroLezioni;
    Context nuovoElementoPortfolio;
    RadioButton radio_skate, radio_basket, radio_pallavolo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_elemento_portfolio);

        cancella = findViewById(R.id.bReset);
        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);

        Intent intent = getIntent();
        idContatto = intent.getIntExtra("idContatto", 0);
        nomeContatto = intent.getStringExtra("nomeContatto");

        descrizione = findViewById(R.id.editDescrizione);
        numeroLezioni = findViewById(R.id.editNumeroLezioni);
        labelScheda = findViewById(R.id.lScheda);
        radio_skate = findViewById(R.id.radio_skate);
        radio_basket = findViewById(R.id.radio_basket);
        radio_pallavolo = findViewById(R.id.radio_pallavolo);

        labelScheda.setText(nomeContatto);

        nuovoElementoPortfolio = this;

        /**
         * listener sui bottoni
         */

        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NuovoElementoPortfolio.this, ModificaContatto.class);
                intent.putExtra("id", idContatto);
                startActivity(intent);
            }
        });

        cancella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descrizione.setText("");
                numeroLezioni.setText("0");
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataUltimaRicarica = "";
                String stato = "Aperto";
                String sport = "";

                if (radio_skate.isChecked()) { sport = Skate; }
                if (radio_basket.isChecked()) { sport = Basket; }
                if (radio_pallavolo.isChecked()) { sport = Pallavolo; }

                ElementoPortfolio elementoPortfolio = new ElementoPortfolio(null, String.valueOf(idContatto),
                        descrizione.getText().toString(), sport, numeroLezioni.getText().toString(), dataUltimaRicarica, stato);

                if (elementoPortfolio.getDescrizione().equals("") || elementoPortfolio.getNumeroLezioni().equals("")) {
                    displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserire tutti i campi");
                }
                else {
                    if (new ElementoPortfolioDAO(nuovoElementoPortfolio).insert(elementoPortfolio)) {
                        esci.callOnClick();
                    }
                    else {
                        displayAlertDialog(nuovoElementoPortfolio, "Attenzione!", "Inserimento fallito, contatta il supporto tecnico");
                    }
                }
            }
        });
    }
}
