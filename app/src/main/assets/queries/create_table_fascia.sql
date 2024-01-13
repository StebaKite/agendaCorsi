create table if not exists fascia (

    id_fascia integer primary key autoincrement,
    id_corso integer,
    descrizione text,
    giorno_settimana text,
    ora_inizio text,
    ora_fine text,
    capienza text,
    data_creazione text,
    data_ultimo_aggiornamento text,

    constraint fk_corso
        foreign key (id_corso)
        references corso (id_corso)
        on delete cascade
    )