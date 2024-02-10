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
import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.table.Contatto;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendaCorsi.ui.corsi.ModificaCorso;
import com.example.agendacorsi.R;

import java.util.Calendar;
import java.util.LinkedList;
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

        modificaContatto = this;
        makeToolBar(modificaContatto);

        tabellaElePortfolio = findViewById(R.id.tabellaElePortfolio);

        Intent intent = getIntent();
        idContatto = intent.getStringExtra("idContatto");

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
        try {
            List<Row> rows = ConcreteDataAccessor.getInstance().read(Contatto.TABLE_NAME, null, new Row(Contatto.contattoColumns.get(Contatto.ID_CONTATTO),idContatto), null);
            for (Row row : rows) {
                _nome.setText(row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString());
                _dataNascita.setText(dateFormat(row.getColumnValue(Contatto.contattoColumns.get(Contatto.DATA_NASCITA)).toString(), "yyyy-MM-dd", "dd-MM-yyyy"));
                _indirizzo.setText(coalesceValue(row.getColumnValue(Contatto.contattoColumns.get(Contatto.INDIRIZZO))));
                _telefono.setText(coalesceValue(row.getColumnValue(Contatto.contattoColumns.get(Contatto.TELEFONO))));
                _email.setText(coalesceValue(row.getColumnValue(Contatto.contattoColumns.get(Contatto.EMAIL))));
                nome = _nome.getText().toString();
                dataNascita = _dataNascita.getText().toString();
                indirizzo = _indirizzo.getText().toString();
                telefono = _telefono.getText().toString();
                email = _email.getText().toString();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                larghezzaColonna1 = (int) (displayMetrics.widthPixels * 0.5);
                larghezzaColonna2 = (int) (displayMetrics.widthPixels * 0.2);

                displayElencoElementiPortfolio(idContatto, row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString());
                esci.requestFocus();
                /**
                 * Listener dei bottoni
                 */
                listenerAnnulla();
                listenerEsci(ModificaContatto.this, ElencoContatti.class, null);
                listenerSalva();
                listenerElimina();

                Map<String, String> intentMap = new ArrayMap<>();
                intentMap.put("idContatto", idContatto);
                intentMap.put("nomeContatto", row.getColumnValue(Contatto.contattoColumns.get(Contatto.NOME)).toString());

                listenerInserisci(modificaContatto, NuovoElementoPortfolio.class, intentMap);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void displayElencoElementiPortfolio(String idContatto, String nomeContatto) throws Exception {
        List<Row> rows = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME,
                null,
                new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_CONTATTO), idContatto),
                null);

        for (Row row : rows) {
            tableRow = new TableRow(this);
            tableRow.setClickable(true);

            tableRow.addView(makeCell(this,new TextView(this),
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString(),
                    larghezzaColonna1,
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DESCRIZIONE)).toString(),
                    View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

            tableRow.addView(makeCell(this,new TextView(this),
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString(),
                    larghezzaColonna2,
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString(),
                    View.TEXT_ALIGNMENT_TEXT_START, View.VISIBLE));

            tableRow.addView(makeCell(this,new TextView(this), DETAIL, 0,
                    row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO)).toString(),
                    View.TEXT_ALIGNMENT_TEXT_START, View.GONE));

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
                try {
                    ConcreteDataAccessor.getInstance().delete(Contatto.TABLE_NAME, new Row(Contatto.contattoColumns.get(Contatto.ID_CONTATTO), idContatto));
                    esci.callOnClick();
                }
                catch (Exception e) {
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

        if (_nome.getText().toString().equals("") || _dataNascita.getText().toString().equals("") ||
                _telefono.getText().toString().equals("")) {
            displayAlertDialog(modificaContatto, "Attenzione!", "Inserire tutti i campi");
        }
        else {
            Row row = new Row();
            boolean anyChange = false;

            if (!_nome.getText().toString().equals(nome)) {
                anyChange = true;
                row.addColumn(Contatto.contattoColumns.get(Contatto.NOME), _nome.getText().toString());
            }
            if (!_dataNascita.getText().toString().equals(dataNascita)) {
                anyChange = true;
                row.addColumn(Contatto.contattoColumns.get(Contatto.DATA_NASCITA), dateFormat(_dataNascita.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"));
            }
            if (!_indirizzo.getText().toString().equals(indirizzo)) {
                anyChange = true;
                row.addColumn(Contatto.contattoColumns.get(Contatto.INDIRIZZO), _indirizzo.getText().toString());
            }
            if (!_telefono.getText().toString().equals(telefono)) {
                anyChange = true;
                row.addColumn(Contatto.contattoColumns.get(Contatto.TELEFONO), _telefono.getText().toString());
            }
            if (!_email.getText().toString().equals(email)) {
                anyChange = true;
                row.addColumn(Contatto.contattoColumns.get(Contatto.EMAIL), _email.getText().toString());
            }

            if (anyChange) {
                try {
                    ConcreteDataAccessor.getInstance().update(Contatto.TABLE_NAME, new Row(Contatto.contattoColumns.get(Contatto.ID_CONTATTO), idContatto), row);
                    makeToastMessage(AgendaCorsiApp.getContext(), "Contatto aggiornato con successo.").show();
                }
                catch (Exception e) {
                    displayAlertDialog(modificaContatto, "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                }
            }
            esci.callOnClick();
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
