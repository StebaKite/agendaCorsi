package com.example.agendaCorsi.ui.contatti;

import android.annotation.SuppressLint;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.database.ContattiDAO;
import com.example.agendaCorsi.database.Contatto;
import com.example.agendaCorsi.database.ElementoPortfolio;
import com.example.agendaCorsi.database.ElementoPortfolioDAO;
import com.example.agendacorsi.R;

import java.util.List;

public class ModificaContatto extends AppCompatActivity {

    int idContatto;
    String nome, indirizzo, telefono, email;
    EditText _nome, _indirizzo, _telefono, _email;
    Button annulla, esci, salva, elimina;

    Context modificaContatto;
    TableLayout tabellaElePortfolio;
    TableRow tableRow;
    TextView descrizione, stato, id_elemento;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_contatto);
        tabellaElePortfolio = findViewById(R.id.tabellaElePortfolio);
        Intent intent = getIntent();

        idContatto = intent.getIntExtra("id", 0);
        _nome = findViewById(R.id.editNomeMod);
        _indirizzo = findViewById(R.id.editIndirizzoMod);
        _telefono = findViewById(R.id.editTelefonoMod);
        _email = findViewById(R.id.editEmailMod);

        annulla = findViewById(R.id.AnnullaModButton);
        esci = findViewById(R.id.ExitModButton);
        salva = findViewById(R.id.SalvaModButton);
        elimina = findViewById(R.id.EliminaModButton);

        /**
         * Caricamento dati contatto selezionato
         */
        Contatto contatto = new Contatto(null, null, null, null, null);
        contatto.setId(String.valueOf(idContatto));
        new ContattiDAO(this).select(contatto);

        if (contatto.getId().equals("")) {
            AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_AlertDialog);
            messaggio.setTitle("Attenzione!");
            messaggio.setMessage("Lettura contatto fallito, contatta il supporto tecnico");
            messaggio.setCancelable(false);
            messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog ad = messaggio.create();
            ad.show();
        }
        else {
            _nome.setText(contatto.getNome());
            _indirizzo.setText(contatto.getIndirizzo());
            _telefono.setText(contatto.getTelefono());
            _email.setText(contatto.getEmail());

            displayElencoElementiPortfolio();

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

        if (contatto.getNome().contains("Barbi")) {
            elimina.setVisibility(View.GONE);
        }
        else {
            elimina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeElimina();
                }
            });
        }
    }


    private void displayElencoElementiPortfolio() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
        int larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

        List<ElementoPortfolio> elementiPortfoList = new ElementoPortfolioDAO(this).getAll();

        for (ElementoPortfolio elementoPortfolio : elementiPortfoList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            descrizione = new TextView(this);
            descrizione.setTextSize(10);
            descrizione.setPadding(10, 20, 10, 20);
            descrizione.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            descrizione.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            descrizione.setGravity(Gravity.CENTER);
            descrizione.setText(String.valueOf(elementoPortfolio.getDescrizione()));
            descrizione.setWidth(larghezzaColonna1);
            tableRow.addView(descrizione);

            stato = new TextView(this);
            stato.setTextSize(2);
            stato.setPadding(10, 20, 10, 20);
            stato.setBackground(ContextCompat.getDrawable(ModificaContatto.this, R.drawable.cell_border));
            stato.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            stato.setGravity(Gravity.CENTER);
            stato.setText(String.valueOf(elementoPortfolio.getStato()));
            stato.setWidth(larghezzaColonna2);
            tableRow.addView(stato);

            id_elemento = new TextView(this);
            id_elemento.setTextSize(2);
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
                    intent.putExtra("id", idElementoSelezionato);
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

        modificaContatto = this;

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
                    AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_AlertDialog);
                    messaggio.setTitle("Attenzione!");
                    messaggio.setMessage("Cancellazione contatto fallito, contatta il supporto tecnico");
                    messaggio.setCancelable(false);
                    messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
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
         * carico i dati della pagina in un oggetto contatto
         */
        Contatto contatto = new Contatto(String.valueOf(idContatto),
                _nome.getText().toString(),
                _indirizzo.getText().toString(),
                _telefono.getText().toString(),
                _email.getText().toString());
        /**
         * controllo validità dati immmessi
         */
        if (contatto.getNome().equals("") ||
                contatto.getIndirizzo().equals("") ||
                contatto.getTelefono().equals("") ||
                contatto.getEmail().equals("")) {

            AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_InfoDialog);
            messaggio.setTitle("Attenzione!");
            messaggio.setMessage("Inserire tutti i campi");
            messaggio.setCancelable(false);
            messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog ad = messaggio.create();
            ad.show();
        }
        else {
            /**
             * aggiorno il db con i dati cariati nell'oggetto contatto
             */
            if (new ContattiDAO(this).update(contatto)) {
                esci.callOnClick();
            }
            else {
                AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_AlertDialog);
                messaggio.setTitle("Attenzione!");
                messaggio.setMessage("Aggiornamento contatto fallito, contatta il supporto tecnico");
                messaggio.setCancelable(false);
                messaggio.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog ad = messaggio.create();
                ad.show();
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
