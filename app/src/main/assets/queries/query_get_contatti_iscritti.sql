--------------------------------------------------------------
-- Contatti iscritti ad una fascia oraria di un corso
--------------------------------------------------------------

select
    contatto.nome,
    iscrizione.id_iscrizione,
    iscrizione.stato,
    contatto.data_nascita

  from iscrizione

    inner join elemento_portfolio
        on elemento_portfolio.id_elemento = iscrizione.id_elemento

    inner join contatto
        on contatto.id_contatto = elemento_portfolio.id_contatto

  where iscrizione.id_fascia = #IDFASCIA#

  order by contatto.nome

