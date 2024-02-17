select
    id_corso,
    descrizione,
    sport,
    stato,
    tipo,
    data_inizio_validita,
    data_fine_validita,
    data_creazione,
    data_ultimo_aggiornamento

from #TABLENAME#
where id_corso = #IDCORSO#