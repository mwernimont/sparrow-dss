--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: deleteFromGenericCacheProcedure20.sql

create or replace procedure GENERIC_CACHE_DELETE_OLD_ROWS
as begin
  delete from generic_cache where last_touched < sysdate-2;
  commit;
end GENERIC_CACHE_DELETE_OLD_ROWS; 
 