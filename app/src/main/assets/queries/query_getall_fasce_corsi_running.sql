select t2.*
  from (
    select
        corso.descrizione as descrizione_corso,
        corso.stato,
        corso.sport,
        fascia.giorno_settimana as numero_giorno,
        fascia.ora_inizio || '-' || fascia.ora_fine as descrizione_fascia,
        fascia.id_fascia,
        fascia.capienza,
        giorno_settimana.nome_giorno_esteso as giorno_settimana,
        coalesce(t1.totale_fascia, 0) as totale_fascia,
        corso.id_corso,
        corso.tipo

     from fascia

        inner join corso
            on corso.id_corso = fascia.id_corso

        left outer join (
            select
                id_fascia,
                count(*) as totale_fascia
              from iscrizione
              where stato != 'Chiuso'
              group by id_fascia
            ) as t1
              on t1.id_fascia = fascia.id_fascia

        left outer join giorno_settimana
            on giorno_settimana.numero_giorno = fascia.giorno_settimana

        where fascia.giorno_settimana = '#OGGI#'
          and fascia.ora_inizio <= '#ADESSO#'
          and fascia.ora_fine >= '#ADESSO#'

    ) as t2
    where t2.stato not in ('Chiuso', 'Sospeso')
      and t2.tipo != 'TEST'

order by t2.descrizione_corso, t2.numero_giorno, t2.descrizione_fascia