update #TABLENAME# set
    descrizione = '#DESC#',
    giorno_settimana = '#GIOSET#',
    ora_inizio = '#ORAINI#',
    ora_fine = '#ORAFIN#',
    capienza = '#CAPIEN#',
    data_ultimo_aggiornamento = datetime('now')
where id_fascia = #IDFASCIA#