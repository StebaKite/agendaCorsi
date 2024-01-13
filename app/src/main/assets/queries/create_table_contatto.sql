create table if not exists contatto (

    id_contatto integer primary key autoincrement,
    nome text,
    indirizzo text,
    telefono text,
    email text,
    data_creazione text,
    data_ultimo_aggiornamento text
    )