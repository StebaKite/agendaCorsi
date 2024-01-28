select
    t1.descrizione,
    t1.anno,
    count(*) as totale
  from
        (select
            corso.descrizione,
            corso.data_inizio_validita as anno,
            iscrizione.id_iscrizione

         from corso

            inner join fascia
                on fascia.id_corso = corso.id_corso

            inner join iscrizione
                on iscrizione.id_fascia = fascia.id_fascia

        where corso.id_corso = #IDCORSO#
        ) as t1

group by t1.descrizione, t1.anno