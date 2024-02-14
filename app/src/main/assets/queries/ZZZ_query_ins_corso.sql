insert into #TABLENAME# (
    descrizione,
    sport,
    stato,
    tipo,
    data_inizio_validita,
    data_fine_validita,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    '#DESC#',
    '#SPORT#',
    '#STATO#',
    '#TIPO#',
    '#DATINI#',
    '#DATFIN#',
    datetime('now'),
    datetime('now')
    )