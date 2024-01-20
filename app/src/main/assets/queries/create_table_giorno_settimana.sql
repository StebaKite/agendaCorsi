create table if not exists giorno_settimana (

    numero_giorno text primary key not null,
    nome_giorno_abbreviato text not null,
    nome_giorno_esteso text not null
    )