package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.PresenzaDAO;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;

public class CreaPresenzaContattoIscritto {

    private static CreaPresenzaContattoIscritto INSTANCE = null;

    private CreaPresenzaContattoIscritto() {}

    public static CreaPresenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreaPresenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public boolean make(String idIscrizione, String idElemento) {
        Presenza presenza = new Presenza(null, idIscrizione, null);
        if (PresenzaDAO.getInstance().insert(presenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_INS_PRESENZA))) {
            ElementoPortfolio elementoPortfolio = new ElementoPortfolio(idElemento, null, null, null, null, null, null);
            ElementoPortfolioDAO.getInstance().select(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_GET_ELEMENTO));

            if (ElementoPortfolioDAO.getInstance().decrementaNumeroLezioni(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_NUMERO_LEZIONI))) {
                return true;
            }
        }
        return false;
    }
}
