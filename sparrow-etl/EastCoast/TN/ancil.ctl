OPTIONS (SKIP=1)
LOAD DATA
INTO TABLE TEMP_ANCIL
TRUNCATE
FIELDS TERMINATED BY X'9'
TRAILING NULLCOLS
(local_id
,std_id


,hydseq "nvl(:hydseq,0)"



,termflag






,reachcode_comb filler

,edaname
,ReachTOT filler


,del_frac filler



,ci_hi_del_frac filler
)