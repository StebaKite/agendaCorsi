--------------------------------------------------------------
-- Contatti iscritti ad una fascia oraria di un corso
--------------------------------------------------------------

select t1.nome, t1,id_iscrizione
  from (

    select contatto.nome, t2.id_elemento
      from contatto

        inner join elemento_portfolio
            on elemento_portfolio.id_contatto = contatto.id_contatto

        inner join (
            select id_iscrizione
              from iscrizione
             where iscrizione.id_fascia = #IDFASCIA#
            ) as t2
            on t2.id_elemento = elemento_portfolio.id_elemento

  ) as t1
  order by t1.nome
