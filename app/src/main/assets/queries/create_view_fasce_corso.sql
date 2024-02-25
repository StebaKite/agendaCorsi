create view if not exists fasce_corso_view as
    select
        t2.descrizione_corso,
        t2.giorno_settimana as numero_giorno,
        t2.ora_inizio || '-' || t2.ora_fine as descrizione_fascia,
        t2.ora_inizio,
        t2.ora_fine,
        t2.id_fascia,
        t2.id_corso,
        t2.capienza,
        t2.nome_giorno_abbreviato,
        coalesce(t2.totale_fascia,0) as totale_fascia

      from (

        select
            corso.id_corso,
            corso.descrizione as descrizione_corso,
            fascia.id_fascia,
            fascia.ora_inizio,
            fascia.ora_fine,
            fascia.capienza,
            fascia.giorno_settimana,
            giorno_settimana.nome_giorno_abbreviato,
            t1.totale_fascia

          from corso

            inner join fascia
                on fascia.id_corso = corso.id_corso

            inner join giorno_settimana
                on fascia.giorno_settimana = giorno_settimana.numero_giorno

            left outer join
                (select id_fascia, count(*) as totale_fascia
                   from iscrizione
                   where stato != 'Chiuso'
                  group by id_fascia
                 ) as t1
              on t1.id_fascia = fascia.id_fascia

        ) as t2
