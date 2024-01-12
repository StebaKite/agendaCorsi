----------------------------------------------------------------------------------
-- query_get_contatti_iscrivibili
--
-- Stretegia di accesso
--
-- 1) tutti gli elementi di portfolio "Carichi" e relativi contatti per lo sport del corso  -> nested-table t1
-- 2) tutte le iscrizioni di tutte le fasce del corso -> nested-table t2
-- 3) elenco dei contatti i cui elementi di portfolio non sono consumati da nessuna iscrizione
--
---------------------------------------------------------------------------

select
        t1.nome,
        t1.id_elemento
    from (
        select
                contatto.nome,
                elemento_portfolio.id_elemento

            from elemento_portfolio

                inner join contatto
                    on contatto.id_contatto = elemento_portfolio.id_contatto

            where elemento_portfolio.stato = 'Carico'
              and elemento_portfolio.sport = '#SPORT#'
        ) as t1
  order by t1.nome


