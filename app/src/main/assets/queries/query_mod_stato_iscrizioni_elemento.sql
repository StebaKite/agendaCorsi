update iscrizione  set
    stato = '#STATO#',
    data_ultimo_aggiornamento = datetime('now')
 where id_elemento = #IDELEM#
   and stato = "Disattiva"