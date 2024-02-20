delete from presenza
where id_iscrizione = #IDISCR#
  and data_conferma not like '#OGGI#%'