create table if not exists iscrizione (

    id_iscrizione integer primary key autoincrement,
    id_fascia integer not null,
    id_elemento integer not null,
    stato text not null,
    data_creazione text not null,
    data_ultimo_aggiornamento text,

    constraint fk_fascia
        foreign key (id_fascia)
        references fascia (id_fascia)
        on delete cascade,

    constraint fk_elemento
        foreign key (id_elemento)
        references elemento_portfolio (id_elemento)
        on delete cascade
    )