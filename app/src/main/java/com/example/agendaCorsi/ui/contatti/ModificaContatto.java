package com.example.agendaCorsi.ui.contatti;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ModificaContatto extends FunctionBase {

    String idContatto;
    String nome, indirizzo, telefono, email, dataNascita;
    EditText _nome, _indirizzo, _telefono, _email, _dataNascita;
    Context modificaContatto;
    TableLayout tabellaElePortfolio;
    final Calendar myCalendar = Calendar.getInstance();
    int larghezzaColonna1, larghezzaColonna2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_contatto);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
        myToolbar.setLogo(R.mipmap.vibes3_logo);

        tabellaElePortfolio = findViewById(R.id.tabellaElePortfolio);

        Intent intent = getIntent();
        idContatto = intent.getStringExtra("idContatto");

        modificaContatto = this;

        _nome = findViewById(R.id.editNomeMod);
        _indirizzo = findViewById(R.id.editIndirizzoMod);
        _telefono = findViewById(R.id.editTelefonoMod);
        _email = findViewById(R.id.editEmailMod);

        annulla = findViewById(R.id.AnnullaModButton);
        esci = findViewById(R.id.ExitModButton);
        salva = findViewById(R.id.SalvaModButton);
        elimina = findViewById(R.id.EliminaModButton);
        inserisci = findViewById(R.id.NuovoElemModButton);

        /**
         * Date picker data nascita
         */
        _dataNascita = findViewById(R.id.editDataNascita);

        DatePickerDialog.OnDateSetListener dataNasc = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel(_dataNascita, myCalendar);
            }
        };
        _dataNascita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificaContatto.this, dataNasc, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /**
         * Caricamento dati contatto selezionato
         */
        Contatto contatto = new Contatto(null, null, null,null, null, null, null);
        contatto.setId(idContatto);

        ContattiDAO.getInstance().select(contatto, QueryComposer.getInstance().getQuery(QUERY_GET_CONTATTO));

        if (contatto.getId().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Lettura fallita, contatta il supporto tecnico");
        }
        else {
            _nome.setText(contatto.getNome());
            _dataNascita.setText(dateFormat(contatto.getDataNascita(), "yyyy-MM-dd", "dd-MM-yyyy"));
            _indirizzo.setText(contatto.getIndirizzo());
            _telefono.setText(contatto.getTelefono());
            _email.setText(contatto.getEmail());
            nome = _nome.getText().toString();
            dataNascita = _dataNascita.getText().toString();
            indirizzo = _indirizzo.getText().toString();
            telefono = _telefono.getText().toString();
            email = _email.getText().toString();
            
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
            larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

            displayElencoElementiPortfolio(idContatto, contatto.getNome());
            esci.requestFocus();
        }
        /**
         * Listener dei bottoni
         */
        listenerAnnulla();
        listenerEsci(ModificaContatto.this, ElencoContatti.class, null);
        listenerSalva();
        listenerElimina();

        Map<String, String> intentMap = new ArrayMap<>();
        intentMap.put("idContatto", idContatto);
        intentMap.put("nomeContatto", contatto.getNome());

        listenerInserisci(modificaContatto, NuovoElementoPortfolio.class, intentMap);
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

        MenuItem exitItem = menu.findItem(R.id.navigation_esci);
        exitItem.setVisible(false);

        return true;
    }


    private void displayElencoElementiPortfolio(String idContatto, String nomeContatto) {
        List<ElementoPortfolio> elementiPortfoList = ElementoPortfolioDAO.getInstance().getContattoElements(idContatto, QueryComposer.getInstance().getQuery(QUERY_GET_ELEMENTS));

        for (ElementoPortfolio elementoPortfolio : elementiPortfoList) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);
            tableRow.addView(makeCell(this,new TextView(this), elementoPortfolio.getStato(), larghezzaColonna1, elementoPortfolio.getDescrizione(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), elementoPortfolio.getStato(), larghezzaColonna2, elementoPortfolio.getStato(), View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));
            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0, elementoPortfolio.getIdElemento(), View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

            Map<String, String> intentMap = new ArrayMap<>();
            intentMap.put("idContatto", idContatto);
            intentMap.put("nomeContatto", nomeContatto);

            listenerTableRow(ModificaContatto.this, ModificaElementoPortfolio.class, "idElemento", intentMap, 2);
            tabellaElePortfolio.addView(tableRow);
        }
    }

    public void makeElimina() {
        AlertDialog.Builder messaggio = new AlertDialog.Builder(ModificaContatto.this, R.style.Theme_InfoDialog);
        messaggio.setTitle("Attenzione");
        messaggio.setMessage("Stai eliminando il contatto " + nome + "\n\nConfermi?");
        messaggio.setCancelable(false);

        /**
         * implemento i listener sui bottoni della conferma eliminazione
         */
        messaggio.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Contatto contatto = new Contatto(String.valueOf(idContatto), null, null,null, null, null, null);

                if (ContattiDAO.getInstance().delete(contatto, QueryComposer.getInstance().getQuery(QUERY_DEL_CONTATTO))) {
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

    public void makeSalva() {
        /**
         * dati immessi in un oggetto contatto
         */
        Contatto contatto = new Contatto(idContatto,
                _nome.getText().toString(),
                dateFormat(_dataNascita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"),
                _indirizzo.getText().toString(),
                _telefono.getText().toString(),
                _email.getText().toString(),
                null);

        if (contatto.getNome().equals("") || contatto.getDataNascita().equals("") ||
            contatto.getTelefono().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            if (ContattiDAO.getInstance().update(contatto, QueryComposer.getInstance().getQuery(QUERY_MOD_CONTATTO))) {
                Toast.makeText(AgendaCorsiApp.getContext(), "Contatto aggiornato con successo.", Toast.LENGTH_LONG).show();
                esci.callOnClick();
            }
            else {
                displayAlertDialog(modificaContatto, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Home")) {
            Intent intent = new Intent(ModificaContatto.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeAnnulla() {
        _nome.setText(nome);
        _dataNascita.setText(dataNascita);
        _indirizzo.setText(indirizzo);
        _telefono.setText(telefono);
        _email.setText(email);
    }
}
