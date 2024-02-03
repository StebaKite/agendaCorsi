--------------------------------------------------------
-- query_elenco_fasce_corso
--------------------------------------------------------

select
	t2.descrizione_corso,
	'' as stato,
	'' as sport,
	t2.giorno_settimana as numero_giorno,
	t2.ora_inizio || '-' || t2.ora_fine as descrizione_fascia,
	t2.id_fascia,
	t2.capienza,
	t2.nome_giorno_abbreviato,
	coalesce(t2.totale_fascia,0) as totale_fascia,
	'' as id_corso,
	'' as tipo_corso

  from (

	select
		corso.id_corso,
		corso.descrizione as descrizione_corso,
		fascia.id_fascia,
		fascia.ora_inizio,
		fascia.ora_fine,
		fascia.capienza,
		fascia.giorno_settimana,
		giorno_settimana.nome_giorno_abbreviato,
		t1.totale_fascia

	  from corso

		inner join fascia
		    on fascia.id_corso = corso.id_corso

		inner join giorno_settimana
		    on fascia.giorno_settimana = giorno_settimana.numero_giorno

		left outer join
			(select id_fascia, count(*) as totale_fascia
			   from iscrizione
			   where stato != 'Chiuso'
			  group by id_fascia
			 ) as t1
		  on t1.id_fascia = fascia.id_fascia

      where corso.id_corso = #IDCORSO#
      #FILTRO_FASCIE#

	) as t2
	order by t2.descrizione_corso,t2.giorno_settimana
