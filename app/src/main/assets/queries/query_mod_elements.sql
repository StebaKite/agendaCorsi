update #TABLENAME# set
    id_contatto = #IDCONTATTO#,
    descrizione = '#DESC#',
    numero_lezioni = '#NUMLEZ#',
    numero_assenze_recuperabili = '#NUMASS#',
    data_ultima_ricarica = '#ULTRIC#',
    stato = '#STATO#',
    data_ultimo_aggiornamento = datetime('now')
where id_elemento = #IDELEMENTO#