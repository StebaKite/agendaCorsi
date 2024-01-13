insert into #TABLENAME# (
    id_corso,
    descrizione,
    giorno_settimana,
    ora_inizio,
    ora_fine,
    capienza,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    #IDCORSO#,
    '#DESC#',
    '#GIOSET#',
    '#ORAINI#',
    '#ORAFIN#',
    '#CAPIEN#',
    datetime('now'),
    datetime('now')
    )