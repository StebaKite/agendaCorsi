package com.example.agendaCorsi.ui.contatti;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import com.example.agendaCorsi.database.ElementoPortfolio;
import com.example.agendaCorsi.database.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendacorsi.R;

import java.util.List;

public class ModificaContatto extends FunctionBase {

    int idContatto;
    String nome, indirizzo, telefono, email;
    EditText _nome, _indirizzo, _telefono, _email;
    Button annulla, esci, salva, elimina, nuovoElemPortfolio;

    Context modificaContatto;
    TableLayout tabellaElePortfolio;
    TableRow tableRow;
    TextView descrizione, stato, id_elemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_contatto);
        tabellaElePortfolio = findViewById(R.id.tabellaElePortfolio);

        Intent intent = getIntent();
        idContatto = intent.getIntExtra("id", 0);

        modificaContatto = this;

        _nome = findViewById(R.id.editNomeMod);
        _indirizzo = findViewById(R.id.editIndirizzoMod);
        _telefono = findViewById(R.id.editTelefonoMod);
        _email = findViewById(R.id.editEmailMod);

        annulla = findViewById(R.id.AnnullaModButton);
        esci = findViewById(R.id.ExitModButton);
        salva = findViewById(R.id.SalvaModButton);
        elimina = findViewById(R.id.EliminaModButton);
        nuovoElemPortfolio = findViewById(R.id.NuovoElemModButton);

        /**
         * Caricamento dati contatto selezionato
         */
        Contatto contatto = new Contatto(null, null, null, null, null);
        contatto.setId(String.valueOf(idContatto));
        new ContattiDAO(this).select(contatto);

        if (contatto.getId().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _nome.setText(contatto.getNome());
            _indirizzo.setText(contatto.getIndirizzo());
            _telefono.setText(contatto.getTelefono());
            _email.setText(contatto.getEmail());

            displayElencoElementiPortfolio(idContatto, contatto.getNome());
            esci.requestFocus();
        }
        /**
         * implemento i listener dei bottoni
         */
        annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAnnulla();
            }
        });

        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificaContatto.this, ElencoContatti.class);
                startActivity(intent);
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSalva();
            }
        });

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeElimina();
            }
        });

        nuovoElemPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModificaContatto.this, NuovoElementoPortfolio.class);
                intent.putExtra("idContatto", idContatto);
                intent.putExtra("nomeContatto", contatto.getNome());
                startActivity(intent);
            }
        });
    }


    private void displayElencoElementiPortfolio(int idContatto, String nomeContatto) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        List<ElementoPortfolio> elementiPortfoList = new ElementoPortfolioDAO(this).getContattoElements(idContatto);

        for (ElementoPortfolio elementoPortfolio : elementiPortfoList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            descrizione = new TextView(this);
            descrizione.setTextSize(16);
            descrizione.setPadding(10, 20, 10, 20);
            descrizione.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(elementoPortfolio.getDescrizione()));
            descrizione.setWidth(larghezzaColonna1);
            tableRow.addView(descrizione);

            stato = new TextView(this);
            stato.setTextSize(16);
            stato.setPadding(10, 20, 10, 20);
            stato.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            stato.setGravity(Gravity.CENTER);
            stato.setText(String.valueOf(elementoPortfolio.getStato()));
            stato.setWidth(larghezzaColonna2);
            tableRow.addView(stato);

            id_elemento = new TextView(this);
            id_elemento.setTextSize(16);
            id_elemento.setPadding(10, 20, 10, 20);
            id_elemento.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            id_elemento.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            id_elemento.setGravity(Gravity.CENTER);
            id_elemento.setText(String.valueOf(elementoPortfolio.getIdElemento()));
            id_elemento.setVisibility(View.INVISIBLE);
            tableRow.addView(id_elemento);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tableRow = (TableRow) view;        // in view c'è la riga selezionata
                    TextView textView = (TextView) tableRow.getChildAt(2);    // id_elemento
                    Integer idElementoSelezionato = Integer.parseInt(textView.getText().toString());

                    /**
                     * Passo all'attività ModificaElemento l'id dell' elemento selezionato
                     */
                    Intent intent = new Intent(ModificaContatto.this, ModificaElementoPortfolio.class);
                    intent.putExtra("idElemento", idElementoSelezionato);
                    intent.putExtra("idContatto", idContatto);
                    intent.putExtra("nomeContatto", nomeContatto);
                    startActivity(intent);
                }
            });
            /**
             * aggiungo la riga alla tabella degli elementi portfolio
             */
            tabellaElePortfolio.addView(tableRow);
        }
    }


    private void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando il contatto " + String.valueOf(idContatto) + "\n\nConfermi?");
        messaggio.setCancelable(false);

        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Contatto contatto = new Contatto(String.valueOf(idContatto), null, null, null, null);
                if (new ContattiDAO(modificaContatto).delete(contatto)) {
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(modificaContatto, "Attenzione!", "Cancellazione fallita, contatta il supporto tecnico");
                }
            }
        }).getContext();

        messaggio.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog ad = messaggio.create();
        ad.show();
    }

    private void makeSalva() {
        /**
         * dati immessi in un oggetto contatto
         */
        Contatto contatto = new Contatto(String.valueOf(idContatto),
                _nome.getText().toString(),
                _indirizzo.getText().toString(),
                _telefono.getText().toString(),
                _email.getText().toString());

        if (contatto.getNome().equals("") || contatto.getIndirizzo().equals("") ||
            contatto.getTelefono().equals("") || contatto.getEmail().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (new ContattiDAO(this).update(contatto)) {
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaContatto, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    private void makeAnnulla() {
        _nome.setText(nome);
        _indirizzo.setText(indirizzo);
        _telefono.setText(telefono);
        _email.setText(email);
    }
}
