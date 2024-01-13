----------------------------------------------------------------------------------
-- query_get_contatti_iscrivibili
--
-- Stretegia di accesso
--
-- 1) tutti gli elementi di portfolio "Carichi" e relativi contatti per lo sport del corso  -> nested-table t1
-- 2) tutte gli elementi di portfolio consumati da iscrizioni di tutte le fasce del corso -> nested-table t2
-- 3) elenco dei contatti il cui elemento di portfolio per lo sport del corso, non è consumato da nessuna iscrizione
--
---------------------------------------------------------------------------

select
        t1.nome,
        t1.id_elemento
    from (
        select contatto.nome, elemento_portfolio.id_elemento
          from elemento_portfolio

                inner join contatto
                    on contatto.id_contatto = elemento_portfolio.id_contatto

            where elemento_portfolio.stato = 'Carico'
              and elemento_portfolio.sport = '#SPORT#'

        ) as t1

    where t1.id_elemento not in (

        select iscrizione.id_elemento
           from corso

                inner join fascia
                    on fascia.id_corso = corso.id_corso

                inner join iscrizione
                    on iscrizione.id_fascia = fascia.id_fascia

           where corso.id_corso = #IDCORSO#
        )

  order by t1.nome


