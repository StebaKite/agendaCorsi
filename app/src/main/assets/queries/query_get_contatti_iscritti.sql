--------------------------------------------------------------
-- Contatti iscritti ad una fascia oraria di un corso
--------------------------------------------------------------

select t1.nome, t1.id_iscrizione, t1.stato
  from (

    select contatto.nome, t2.id_elemento, t2.id_iscrizione, t2.stato
      from contatto

        inner join elemento_portfolio
            on elemento_portfolio.id_contatto = contatto.id_contatto

        inner join (
            select id_iscrizione, id_elemento, stato
              from iscrizione
             where iscrizione.id_fascia = #IDFASCIA#
            ) as t2
            on t2.id_elemento = elemento_portfolio.id_elemento

  ) as t1
  order by t1.nome
