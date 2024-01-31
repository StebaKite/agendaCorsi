select
    t1.descrizione,
    t1.anno,
    coalesce(t1.tot_fascia, 0) as totale
  from
        (select
            corso.descrizione,
            corso.data_inizio_validita as anno,
            t2.tot_fascia

         from corso

            inner join fascia
                on fascia.id_corso = corso.id_corso

            left outer join
                (select id_fascia, count(id_iscrizione) as tot_fascia
                   from iscrizione
                   group by id_fascia
                ) as t2
                on t2.id_fascia = fascia.id_fascia

          where corso.id_corso = #IDCORSO#

        ) as t1
