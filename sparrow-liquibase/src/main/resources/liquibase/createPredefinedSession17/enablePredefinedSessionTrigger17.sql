--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: enablePredefinedSessionTrigger17.sql

--changeset cschroed:enableAutoIncrementPKonPredefinedSession

ALTER TRIGGER "PREDEFINED_SESSION_AUTO_ID_TRG" ENABLE;
--rollback SELECT * FROM DUAL;