insert into #TABLENAME# (
    nome,
    data_nascita,
    indirizzo,
    telefono,
    email,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    '#NOME#',
    '#DATNAS#'
    '#INDIR#',
    '#TEL#',
    '#EMAIL#',
    datetime('now'),
    datetime('now')
    )