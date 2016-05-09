--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: createRefCursor20.sql

CREATE OR REPLACE EDITIONABLE PACKAGE "TYPES" 
as
	type ref_cursor is ref cursor;
end;
