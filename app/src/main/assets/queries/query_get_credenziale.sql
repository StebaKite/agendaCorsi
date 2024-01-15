select
    utente,
    password,
    data_creazione,
    data_ultimo_aggiornamento

from credenziale

order by data_ultimo_aggiornamento desc