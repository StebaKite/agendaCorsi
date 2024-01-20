create table if not exists corso (

    id_corso integer primary key autoincrement,
    descrizione text not null,
    sport text not null,
    stato text not null,
    tipo text not null,
    data_inizio_validita text not null,
    data_fine_validita text not null,
    data_creazione text not null,
    data_ultimo_aggiornamento text
    )