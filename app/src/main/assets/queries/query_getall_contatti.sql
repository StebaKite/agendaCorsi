select
    contatto.id_contatto,
    contatto.nome,
    contatto.data_nascita,
    contatto.indirizzo,
    contatto.telefono,
    contatto.email,
    coalesce(elemento_portfolio.stato, "")

from contatto

    left outer join elemento_portfolio
        on elemento_portfolio.id_contatto = contatto.id_contatto

order by contatto.nome