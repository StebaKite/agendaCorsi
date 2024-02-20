select
    id_iscrizione

 from iscrizione

 where id_elemento = #IDELEM#
   and stato in ('Attiva', 'Disattiva')