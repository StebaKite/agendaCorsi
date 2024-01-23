create table if not exists presenza (

    id_presenza integer primary key autoincrement,
    id_iscrizione integer not null,
    data_conferma text not null,

    constraint fk_presenza
        foreign key (id_iscrizione)
        references iscrizione (id_iscrizione)
        on delete cascade
    )