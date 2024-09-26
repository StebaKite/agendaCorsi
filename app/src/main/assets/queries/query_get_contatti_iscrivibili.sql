----------------------------------------------------------------------------------
-- query_get_contatti_iscrivibili
--
-- Stretegia di accesso
--
-- 1) tutti gli elementi di portfolio "Carichi" e relativi contatti per lo sport del corso  -> nested-table t1
-- 2) elenco dei contatti il cui elemento di portfolio per lo sport del corso, non è consumato da nessuna iscrizione
--
---------------------------------------------------------------------------

select
        t1.nome,
        t1.id_elemento,
        t1.email,
        t1.data_nascita
    from (
        select contatto.nome, contatto.email, elemento_portfolio.id_elemento, contatto.data_nascita
          from elemento_portfolio

                inner join contatto
                    on contatto.id_contatto = elemento_portfolio.id_contatto

            where elemento_portfolio.stato = 'Carico'
              and elemento_portfolio.sport = '#SPORT#'

        ) as t1

  order by t1.nome
