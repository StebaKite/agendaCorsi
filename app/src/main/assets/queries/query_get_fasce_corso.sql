select
    fascia.id_fascia,
    fascia.id_corso,
    fascia.descrizione,
    giorno_settimana.nome_giorno_esteso as giorno_settimana,
    fascia.ora_inizio,
    fascia.ora_fine,
    fascia.capienza,
    fascia.data_creazione,
    fascia.data_ultimo_aggiornamento
from #TABLENAME#
    left outer join giorno_settimana
      on giorno_settimana.numero_giorno = fascia.giorno_settimana
where id_corso = #IDCORSO#