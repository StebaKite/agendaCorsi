create view if not exists contatti_iscritti_view as
    select
        contatto.nome,
        iscrizione.id_iscrizione,
        iscrizione.stato,
        contatto.data_nascita,
        iscrizione.id_fascia

      from iscrizione

        inner join elemento_portfolio
            on elemento_portfolio.id_elemento = iscrizione.id_elemento

        inner join contatto
            on contatto.id_contatto = elemento_portfolio.id_contatto
