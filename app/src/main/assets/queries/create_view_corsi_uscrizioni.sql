----------------------------------------------------------------------------------
-- Corsi con le iscrizioni
---------------------------------------------------------------------------
create view if not exists corsi_iscrizioni_view as
    select
        corso.id_corso,
        iscrizione.id_elemento

       from corso

            inner join fascia
                on fascia.id_corso = corso.id_corso

            inner join iscrizione
                on iscrizione.id_fascia = fascia.id_fascia

       where corso.stato != 'Chiuso'
