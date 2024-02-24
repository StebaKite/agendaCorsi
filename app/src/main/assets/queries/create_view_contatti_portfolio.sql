----------------------------------------------------------------------------------
-- Contatti e loro portfolio
---------------------------------------------------------------------------
create view if not exists contatti_portfolio_view as
    select
            t1.nome,
            t1.id_elemento,
            t1.email,
            t1.data_nascita,
            t1.sport,
            t1.stato

        from (
            select
                contatto.nome,
                contatto.email,
                contatto.data_nascita,
                elemento_portfolio.id_elemento,
                elemento_portfolio.stato,
                elemento_portfolio.sport

              from elemento_portfolio

                    inner join contatto
                        on contatto.id_contatto = elemento_portfolio.id_contatto

            ) as t1
