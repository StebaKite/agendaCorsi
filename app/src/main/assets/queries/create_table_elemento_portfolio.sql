create table if not exists elemento_portfolio (

    id_elemento integer primary key autoincrement,
    id_contatto integer,
    descrizione text,
    sport text,
    numero_lezioni integer,
    data_ultima_ricarica text,
    stato text,
    data_creazione text,
    data_ultimo_aggiornamento text,

    constraint fk_contatto
        foreign key (id_contatto)
        references contatto (id_contatto)
        on delete cascade
    )