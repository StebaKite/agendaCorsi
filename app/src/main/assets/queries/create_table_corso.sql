create table if not exists corso (

    id_corso integer primary key autoincrement,
    descrizione text,
    sport text,
    stato text,
    tipo text,
    data_inizio_validita text,
    data_fine_validita text,
    data_creazione text,
    data_ultimo_aggiornamento text
    )