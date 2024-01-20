create table if not exists fascia (

    id_fascia integer primary key autoincrement,
    id_corso integer not null,
    descrizione text not null,
    giorno_settimana text not null,
    ora_inizio text not null,
    ora_fine text not null,
    capienza text not null,
    data_creazione text not null,
    data_ultimo_aggiornamento text,

    constraint fk_corso
        foreign key (id_corso)
        references corso (id_corso)
        on delete cascade
    )