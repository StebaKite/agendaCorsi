package com.example.agendaCorsi.ui.settaggi;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendaCorsi.AgendaCorsiApp;
import com.example.agendaCorsi.MainActivity;
import com.example.agendaCorsi.database.access.ContattiDAO;
import com.example.agendaCorsi.database.access.CredenzialeDAO;
import com.example.agendaCorsi.database.table.Credenziale;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;
import com.example.agendacorsi.R;

public class ModificaSettaggi extends FunctionBase {

    String utente, password;
    EditText _utente, _password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_settaggi);

        _utente = findViewById(R.id.editUtente);
        _password = findViewById(R.id.editPassword);

        esci = findViewById(R.id.bExit);
        salva = findViewById(R.id.bSalva);
        /**
         * Caricamento credenziali
         */
        Credenziale credenziale = new Credenziale(null, null, null, null);
        CredenzialeDAO.getInstance().select(credenziale, QueryComposer.getInstance().getQuery(QUERY_GET_CREDENZIALE));

        if (credenziale.getUtente().equals("")) {
            _utente.setText("");
            _password.setText("");
            password = "";
            utente = "";
        }
        else {
            _utente.setText(credenziale.getUtente());
            _utente.setClickable(false);
            _utente.setFocusable(false);
            _password.setText(credenziale.getPassword());
            utente = credenziale.getUtente();
            password = credenziale.getPassword();
        }
        esci.requestFocus();
        /**
         * Listener sui bottoni
         */
        listenerEsci(ModificaSettaggi.this, MainActivity.class, null);
        listenerSalva();
    }

    public void makeSalva() {
        Credenziale credenziale = new Credenziale(_utente.getText().toString(), _password.getText().toString(), null,null);
        /**
         * Se le credenziali non esistevano le inserisco altrimenti aggiorno quelle esistenti
         */
        if (password.equals("") && !_password.getText().toString().equals("")) {
            /**
             * Creo le redenziali
             */
            if (credenziale.getUtente().equals("") || credenziale.getPassword().equals("")) {
                displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Inserire tutti i campi");
            }
            else {
                if (CredenzialeDAO.getInstance().insert(credenziale, QueryComposer.getInstance().getQuery(QUERY_INS_CREDENZIALE))) {
                    makeToastMessage(AgendaCorsiApp.getContext(), "Settaggi creati con successo.").show();
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                }
            }
        } else if (!password.equals("") && !_password.getText().toString().equals("")) {
            /**
             * Aggiorno le credenziali
             */

            if (credenziale.getPassword().equals("")) {
                displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Inserire tutti i campi");
            }
            else {
                if (CredenzialeDAO.getInstance().update(credenziale, QueryComposer.getInstance().getQuery(QUERY_MOD_CREDENZIALE))) {
                    makeToastMessage(AgendaCorsiApp.getContext(), "Settaggi aggiornati con successo.").show();
                    esci.callOnClick();
                }
                else {
                    displayAlertDialog(AgendaCorsiApp.getContext(), "Attenzione!", "Aggiornamento fallito, contatta il supporto tecnico");
                }
            }
        }
        else if (!password.equals("") && _password.getText().toString().equals("")) {
            /**
             * Elimino le credenziali
             */
            if (CredenzialeDAO.getInstance().delete(credenziale, QueryComposer.getInstance().getQuery(QUERY_DEL_CREDENZIALE))) {
                makeToastMessage(AgendaCorsiApp.getContext(), "Settaggi eliminati con successo.").show();
                esci.callOnClick();
            }
        }
    }
}
