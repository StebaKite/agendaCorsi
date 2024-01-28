create table if not exists totale_corso (

    id_totale integer primary key autoincrement,
    descrizione_corso text not null,
    anno_svolgimento text not null,
    nome_totale text not null,
    valore_totale integer not null
    )