insert into #TABLENAME# (
    nome,
    indirizzo,
    telefono,
    email,
    data_creazione,
    data_ultimo_aggiornamento)
values (
    '#NOME#',
    '#INDIR#',
    '#TEL#',
    '#EMAIL#',
    datetime('now'),
    datetime('now')
    )