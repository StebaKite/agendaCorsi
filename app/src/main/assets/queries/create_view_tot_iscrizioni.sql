create view if not exists tot_iscrizioni_view as
    select
        t1.id_corso,
        t1.descrizione as descrizione_corso,
        t1.anno as anno_svolgimento,
        sum(t1.tot_fascia) as valore_totale
      from
            (select
                corso.id_corso,
                corso.descrizione,
                substr(corso.data_inizio_validita, 1, 4) as anno,
                coalesce(t2.tot_fascia, 0) as tot_fascia

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

        group by t1.id_corso, t1.descrizione, t1.anno
