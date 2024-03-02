update elemento_portfolio set
    numero_lezioni = '#NUMLEZ#',
    stato = '#STATO#',
    data_ultimo_aggiornamento = datetime('now')
 where id_elemento = #IDELEMENTO#