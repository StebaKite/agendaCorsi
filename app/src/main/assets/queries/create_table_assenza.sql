create table if not exists assenza (

    id_assenza integer primary key autoincrement,
    id_iscrizione integer not null,
    data_conferma text not null,

    constraint fk_assenza
        foreign key (id_iscrizione)
        references iscrizione (id_iscrizione)
        on delete cascade
    )