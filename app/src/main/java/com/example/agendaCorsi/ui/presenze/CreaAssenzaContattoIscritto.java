package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.access.AssenzaDAO;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.access.PresenzaDAO;
import com.example.agendaCorsi.database.table.Assenza;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;

public class CreaAssenzaContattoIscritto {

    private static CreaAssenzaContattoIscritto INSTANCE = null;

    private CreaAssenzaContattoIscritto() {}

    public static CreaAssenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreaAssenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public boolean make(String idIscrizione, ElementoPortfolio elementoPortfolio) {
        Assenza assenza = new Assenza(null, idIscrizione, null);
        if (AssenzaDAO.getInstance().insert(assenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_INS_ASSENZA))) {
            /**
             * Se il numero delle assenze effetuate è maggiore del limite massimo, configurato nel portfolio del contatto
             * relativo allo sport praticato, la lezione viene contata come valida.
             */
            assenza.setIdAssenza(null);
            assenza.setIdIscrizione(idIscrizione);
            assenza.setDataConferma(null);
            AssenzaDAO.getInstance().getTotAssenze(assenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_GET_ASSENZE_ISCRIZIONE));

            Iscrizione iscrizione = new Iscrizione(idIscrizione, null, null, FunctionBase.STATO_USATA, null, null);
            if (IscrizioneDAO.getInstance().updateStato(iscrizione, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_STATO_ISCRIZIONE))) {
                if (Integer.parseInt(assenza.getTotaleAssenza()) > Integer.parseInt(elementoPortfolio.getNumeroAssenzeRecuperabili())) {
                    if (ElementoPortfolioDAO.getInstance().decrementaNumeroLezioni(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_NUMERO_LEZIONI))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
