create table if not exists elemento_portfolio (

    id_elemento integer primary key autoincrement,
    id_contatto integer not null,
    descrizione text not null,
    sport text not null,
    numero_lezioni integer not null,
    numero_assenze_recuperabili integer not null,
    data_ultima_ricarica text,
    stato text not null,
    data_creazione text not null,
    data_ultimo_aggiornamento text,

    constraint fk_contatto
        foreign key (id_contatto)
        references contatto (id_contatto)
        on delete cascade
    )