--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: alterPredefinedSession.sql

--changeset cschroed:addNatureToPredefinedSession
alter table PREDEFINED_SESSION add (
	nature nvarchar2(100),
)

--rollback alter table PREDEFINED_SESSION drop (nature);