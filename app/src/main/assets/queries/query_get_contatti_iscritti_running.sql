--------------------------------------------------------------
-- Contatti iscritti ad una fascia oraria di un corso running
--------------------------------------------------------------

select
    contatto.nome,
    iscrizione.id_iscrizione,
    iscrizione.stato,
    contatto.data_nascita,
    iscrizione.id_elemento,
    coalesce(t1.id_presenza, '') as id_presenza,
    coalesce(t1.data_conferma, '') as data_conferma_presenza,
    coalesce(t2.id_assenza, '') as id_assenza,
    coalesce(t2.data_conferma, '') as data_conferma_assenza,
    elemento_portfolio.stato as stato_elemento

  from iscrizione

    inner join elemento_portfolio
        on elemento_portfolio.id_elemento = iscrizione.id_elemento

    inner join contatto
        on contatto.id_contatto = elemento_portfolio.id_contatto

    left outer join (
        select id_presenza, id_iscrizione, data_conferma
          from presenza
          where data_conferma like '#OGGI#%'
        ) as t1
        on t1.id_iscrizione = iscrizione.id_iscrizione

    left outer join (
        select id_assenza, id_iscrizione, data_conferma
          from assenza
          where data_conferma like '#OGGI#%'
        ) as t2
        on t2.id_iscrizione = iscrizione.id_iscrizione

  where iscrizione.id_fascia = #IDFASCIA#

  order by contatto.nome

