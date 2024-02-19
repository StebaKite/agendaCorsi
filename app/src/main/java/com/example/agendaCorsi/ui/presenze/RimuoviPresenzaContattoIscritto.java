package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.access.ElementoPortfolioDAO;
import com.example.agendaCorsi.database.access.PresenzaDAO;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;
import com.example.agendaCorsi.ui.base.QueryComposer;

public class RimuoviPresenzaContattoIscritto {

    private static RimuoviPresenzaContattoIscritto INSTANCE = null;

    private RimuoviPresenzaContattoIscritto() {}

    public static RimuoviPresenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RimuoviPresenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public boolean make(String idPresenza, String idElemento) {
        Presenza presenza = new Presenza(idPresenza, null, null);
        if (PresenzaDAO.getInstance().delete(presenza, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_DEL_PRESENZA))) {
            ElementoPortfolio elementoPortfolio = new ElementoPortfolio(idElemento, null, null, null, null, null, null, null);
            ElementoPortfolioDAO.getInstance().select(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_GET_ELEMENTO));

            if (ElementoPortfolioDAO.getInstance().incrementaNumeroLezioni(elementoPortfolio, QueryComposer.getInstance().getQuery(FunctionBase.QUERY_MOD_NUMERO_LEZIONI))) {
                return true;
            }
        }
        return false;
    }
}
