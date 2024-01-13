insert into #TABLENAME# (
    descrizione,
    sport,
    stato,
    data_inizio_validita,
    data_fine_validita,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    '#DESC#',
    '#SPORT#',
    '#STATO#',
    '#DATINI#',
    '#DATFIN#',
    datetime('now'),
    datetime('now')
    )