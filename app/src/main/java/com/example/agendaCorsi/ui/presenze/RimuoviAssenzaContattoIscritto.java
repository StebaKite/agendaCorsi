package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.access.AssenzaDAO;
import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.IscrizioneDAO;
import com.example.agendaCorsi.database.table.Assenza;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;

public class RimuoviAssenzaContattoIscritto {

    private static RimuoviAssenzaContattoIscritto INSTANCE = null;

    private RimuoviAssenzaContattoIscritto() {}

    public static RimuoviAssenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RimuoviAssenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public boolean make(String idAssenza, ElementoPortfolio elementoPortfolio, String idIscrizione) {
        Assenza assenza = new Assenza(idAssenza, null, null);
        if (AssenzaDAO.getInstance().delete(assenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_DEL_ASSENZA))) {

            /**
             * Se il numero delle assenze effetuate è minore o uguale al limite massimo, configurato nel portfolio del contatto
             * relativo allo sport praticato, la lezione viene ricaricata nel portfolio.
             */
            assenza.setIdAssenza(null);
            assenza.setIdIscrizione(idIscrizione);
            assenza.setDataConferma(null);
            AssenzaDAO.getInstance().getTotAssenze(assenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_GET_ASSENZE_ISCRIZIONE));

            Iscrizione iscrizione = new Iscrizione(idIscrizione, null, null, FunctionBase.STATO_ATTIVA, null, null);
            if (IscrizioneDAO.getInstance().updateStato(iscrizione, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_STATO_ISCRIZIONE))) {
                if (Integer.parseInt(assenza.getTotaleAssenza()) == Integer.parseInt(elementoPortfolio.getNumeroAssenzeRecuperabili())) {
                    if (ElementoPortfolioDAO.getInstance().incrementaNumeroLezioni(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_NUMERO_LEZIONI))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
