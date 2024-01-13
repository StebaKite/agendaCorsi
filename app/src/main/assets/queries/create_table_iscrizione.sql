create table if not exists iscrizione (

    id_iscrizione integer primary key autoincrement,
    id_fascia integer,
    id_elemento integer,
    stato text,
    data_creazione text,
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