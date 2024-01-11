--------------------------------------------------------
-- query_totals_corsi  (per poterla leggere facilmente)
--------------------------------------------------------

select
    corso.descrizione_corso,
    t3.descrizione_fascia,
    t3.giorno_settimana,
    t3.totale_fascia
    from (
        select giorno_settimana.*, t2.*
          from giorno_settimana
             left outer join (
                select
                    fascia.id_corso,
                    fascia.id_fascia,
                    fascia.ora_inizio || '-' || fascia.ora_fine as descrizione_fascia,
                    fascia.giorno_settimana,
                    coalesce(t1.totale_fascia,0) as totale_fascia
                  from fascia
                    left outer join
                        (select id_fascia, count(*) as totale_fascia
                           from iscrizione
                          group by id_fascia
                         ) as t1
                      on t1.id_fascia = fascia.id_fascia
             ) as t2
               on t2.giorno_settimana = giorno_settimana.numero_giorno
    ) as t3
    inner join corso on corso.id_corso = t3.id_corso
	order by 1,2,3
