insert into credenziale (
    utente,
    password,
    data_creazione,
    data_ultimo_aggiornamento
    )
values (
    '#UTENTE#',
    '#PASSWD#',
    datetime('now'),
    datetime('now')
    )