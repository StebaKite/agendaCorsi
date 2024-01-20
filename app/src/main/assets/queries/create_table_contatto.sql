create table if not exists contatto (

    id_contatto integer primary key autoincrement,
    nome text not null,
    data_nascita text not null,
    indirizzo text,
    telefono text not null,
    email text,
    data_creazione text not null,
    data_ultimo_aggiornamento text
    )