--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: findGenericCacheProcedure20.sql

create or replace procedure GENERIC_CACHE_BY_CLS_AND_CODE (
  key_in in number,
  value_class_in in varchar,
  return_cursor out types.ref_cursor
  )
as begin
  update generic_cache
     set last_touched = sysdate
   where key = key_in;

  commit;

  open return_cursor for
    select KEY, VALUE_CLASS, VALUE, LAST_TOUCHED from GENERIC_CACHE
    where key = key_in
      and VALUE_CLASS = value_class_in;
end GENERIC_CACHE_BY_CLS_AND_CODE;

--------- 
 