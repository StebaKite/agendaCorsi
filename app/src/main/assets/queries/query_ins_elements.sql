insert into #TABLENAME# (
    id_contatto,
    descrizione,
    sport,
    numero_lezioni,
    data_ultima_ricarica,
    stato,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    '#IDCONTATTO#',
    '#DESC#',
    '#SPORT#',
    '#NUMLEZ#',
    '#ULTRIC#',
    '#STATO#',
    datetime('now'),
    datetime('now')
    )