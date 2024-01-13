select
    id_fascia,
    id_corso,
    descrizione,
    '0' as numero_giorno,
    giorno_settimana,
    ora_inizio,
    ora_fine,
    capienza,
    data_creazione,
    data_ultimo_aggiornamento
from #TABLENAME#
where id_fascia = #IDFASCIA#