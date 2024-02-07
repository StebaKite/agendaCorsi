select
    id_iscrizione,
    id_fascia,
    id_elemento,
    stato,
    data_creazione,
    data_ultimo_aggiornamento

 from iscrizione

 where id_iscrizione = #IDISCR#