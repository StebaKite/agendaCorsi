create view if not exists tot_iscrizioni_view as
    select
        t1.id_corso,
        t1.descrizione as descrizione_corso,
        t1.anno as anno_svolgimenti,
        coalesce(t1.tot_fascia, 0) as valore_totale
      from
            (select
                corso.id_corso,
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

            ) as t1
