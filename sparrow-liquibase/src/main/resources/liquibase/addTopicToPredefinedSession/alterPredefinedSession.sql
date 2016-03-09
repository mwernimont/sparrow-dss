--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: alterPredefinedSession.sql

--changeset cschroed:addTopicToPredefinedSession
alter table PREDEFINED_SESSION add (
	topic nvarchar2(100),
)

--rollback alter table PREDEFINED_SESSION drop (topic);