update credenziale set
    password = '#PASSWD#',
    data_ultimo_aggiornamento = datetime('now')
where utente = '#UTENTE#'