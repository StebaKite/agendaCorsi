package com.example.agendaCorsi.ui.presenze;

import com.example.agendaCorsi.database.ConcreteDataAccessor;
import com.example.agendaCorsi.database.Row;
import com.example.agendaCorsi.database.table.ElementoPortfolio;
import com.example.agendaCorsi.database.table.Iscrizione;
import com.example.agendaCorsi.database.table.Presenza;
import com.example.agendaCorsi.ui.base.FunctionBase;

import java.util.LinkedList;
import java.util.List;

public class CreaPresenzaContattoIscritto extends FunctionBase {

    private static CreaPresenzaContattoIscritto INSTANCE = null;

    private CreaPresenzaContattoIscritto() {}

    public static CreaPresenzaContattoIscritto getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreaPresenzaContattoIscritto();
        }
        return INSTANCE;
    }

    public int make(String idIscrizione, String idElemento) {
        try {
            int numLezioni = 0;
            Row insertColumn = new Row();
            insertColumn.addColumn(Presenza.presenzaColumns.get(Presenza.ID_ISCRIZIONE), idIscrizione);
            insertColumn.addColumn(Presenza.presenzaColumns.get(Presenza.DATA_CONFERMA), getNowTimestamp());

            List insertRows = new LinkedList();
            insertRows.add(insertColumn);

            ConcreteDataAccessor.getInstance().insert(Presenza.TABLE_NAME, insertRows);

            List<Row> elementiPortfolioList = null;
            elementiPortfolioList = ConcreteDataAccessor.getInstance().read(ElementoPortfolio.TABLE_NAME,
                    new String[]{
                            ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI),
                            ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)
                    },
                    new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento),
                    null);

            for (Row row : elementiPortfolioList) {
                numLezioni = Integer.parseInt(row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI)).toString()) - 1;
                String stato = (numLezioni == 0) ? STATO_ESAURITO : row.getColumnValue(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO)).toString();

                Row updateColumns = new Row();
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.NUMERO_LEZIONI), numLezioni);
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.STATO), stato);
                updateColumns.addColumn(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());

                ConcreteDataAccessor.getInstance().update(ElementoPortfolio.TABLE_NAME,
                        new Row(ElementoPortfolio.elementoPortfolioColumns.get(ElementoPortfolio.ID_ELEMENTO), idElemento),
                        updateColumns);

                if (numLezioni == 0) {
                    Row iscrizioneUpdateColumns = new Row();
                    iscrizioneUpdateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.STATO), STATO_DISATTIVA);
                    iscrizioneUpdateColumns.addColumn(Iscrizione.iscrizioneColumns.get(Iscrizione.DATA_ULTIMO_AGGIORNAMENTO), getNowTimestamp());
                    ConcreteDataAccessor.getInstance().update(Iscrizione.TABLE_NAME, new Row(Iscrizione.iscrizioneColumns.get(Iscrizione.ID_ISCRIZIONE), idIscrizione), iscrizioneUpdateColumns);
                }
            }
            return numLezioni;
        }
        catch (Exception e) {
            return -1;
        }
    }
}
