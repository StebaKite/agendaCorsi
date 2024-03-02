package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;

import java.util.List;

public class RimuoviPresenzaContattoIscritto extends FunctionBase {

    private static RimuoviPresenzaContattoIscritto INSTANCE = null;

    private RimuoviPresenzaContattoIscritto() {}

    public static RimuoviPresenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RimuoviPresenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public int make(String idPresenza, String idElemento) {
        try {
            int numLezioni = 0;
            ConcreteDataAccessor.getInstance().delete(Presenza.TABLE_NAME, new Row(Presenza.presenzaColumns.get(Presenza.ID_PRESENZA), idPresenza));

            List<Row> elementiPortfolioList = null;
            elementiPortfolioList = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME,
                    new String[]{
                            ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI),
                            ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)
                    },
                    new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento),
                    null);

            for (Row row : elementiPortfolioList) {
                numLezioni = Integer.parseInt(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI)).toString()) + 1;
                String stato = STATO_CARICO;

                Row updateColumns = new Row();
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI), numLezioni);
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), stato);
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

                ConcreteDataAccessor.getInstance().update(ElementoPortfolio.TABLE_NAME,
                        new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento),
                        updateColumns);
            }
            return numLezioni;
        }
        catch (Exception e) {
            return -1;
        }
    }
}
