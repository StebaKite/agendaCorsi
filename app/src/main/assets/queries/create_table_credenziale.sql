create table if not exists credenziale (

    utente text primary key not null,
    password text not null,
    data_creazione text not null,
    data_ultimo_aggiornamento text
    )