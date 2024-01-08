select
	t2.descrizione_corso,
	t2.descrizione_fascia,
	t2.giorno_settimana,
	t2.totale_fascia
  from (
	select
		corso.id_corso,
		corso.descrizione as descrizione_corso,
		fascia.id_fascia,
		fascia.descrizione as descrizione_fascia,
		fascia.giorno_settimana,
		t1.totale_fascia
	  from corso
		inner join fascia on fascia.id_corso = corso.id_corso
		left outer join
			(select id_corso, id_fascia, count(*) as totale_fascia
			   from iscrizione
			  group by id_corso, id_fascia
			 ) as t1
		  on t1.id_corso = corso.id_corso
		  and t1.id_fascia = fascia.id_fascia
	) as t2
	order by 1,2,3
