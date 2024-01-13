select
    id_elemento,
    id_contatto,
    descrizione,
    sport,
    numero_lezioni,
    data_ultima_ricarica,
    stato,
    data_creazione,
    data_ultimo_aggiornamento
from #TABLENAME#
where id_contatto = #IDCONTATTO#