update #TABLENAME# set
    nome = '#NOME#',
    indirizzo = '#INDIR#',
    telefono = '#TEL#',
    email = '#EMAIL#',
    data_ultimo_aggiornamento = datetime('now')
where id_contatto = #IDCONTATTO#