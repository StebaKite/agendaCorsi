create view if not exists all_contatti_view as
select t1.*
  from (
        select
            contatto.id_contatto,
            contatto.nome,
            contatto.data_nascita,
            contatto.indirizzo,
            contatto.telefono,
            contatto.email,
            coalesce(elemento_portfolio.stato, "") as stato_elemento

        from contatto

            left outer join elemento_portfolio
                on elemento_portfolio.id_contatto = contatto.id_contatto
  ) as t1