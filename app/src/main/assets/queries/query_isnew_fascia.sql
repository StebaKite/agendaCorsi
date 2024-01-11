select id_fascia
  from #TABLENAME#
 where id_corso = #IDCORSO#
   and id_fascia != #IDFASCIA#
   and giorno_settimana = '#GIOSET#'
   and ((ora_inizio between '#ORAINI#' and '#ORAFIN#') or (ora_fine between '#ORAINI#' and '#ORAFIN#'))