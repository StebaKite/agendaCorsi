--------------------------------------------------------
-- query_totals_corsi  (per poterla leggere facilmente)
--------------------------------------------------------

select
	coalesce(t3.descrizione_corso, '-') as descrizione_corso,
	coalesce(t3.descrizione_fascia, '-') as descrizione_fascia,
	t3.numero_giorno as giorno_settimana,
	coalesce(t3.totale_fascia,0) as totale_fascia
from (
    select *
      from giorno_settimana
         left outer join (
            select
                corso.id_corso,
                corso.descrizione as descrizione_corso,
                fascia.id_fascia,
                fascia.descrizione as descrizione_fascia,
                fascia.giorno_settimana,
                t1.totale_fascia
              from corso
                inner join fascia on fascia.id_corso = corso.id_corso
                left outer join
                    (select id_fascia, count(*) as totale_fascia
                       from iscrizione
                      group by id_fascia
                     ) as t1
                  on t1.id_fascia = fascia.id_fascia
         ) as t2
           on t2.giorno_settimana = giorno_settimana.numero_giorno
    ) as t3
	order by 1,2,3
