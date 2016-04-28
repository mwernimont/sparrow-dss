--liquibase formatted sql

--This is for the sparrow_dss schema

--logicalFilePath: createGenericCache19.sql

--changeset cschroed:createGenericCache
CREATE TABLE "GENERIC_CACHE" (
	"KEY" NUMBER(10,0), 
	"VALUE_CLASS" VARCHAR2(100), 
	"VALUE" BLOB, 
	"LAST_TOUCHED" TIMESTAMP (6)
);
--rollback drop table 'generic_cache';

--changeset cschroed:createGenericCacheLastTouchedIndex
CREATE INDEX "GENERIC_CACHE_LAST_TOUCHED" ON "GENERIC_CACHE" ("LAST_TOUCHED");
--rollback drop index 'GENERIC_CACHE_LAST_TOUCHED';

--changeset cschroed:createGenericCacheUniqueIndex
CREATE UNIQUE INDEX "GENERIC_CACHE_UK" ON "GENERIC_CACHE" ("VALUE_CLASS", "KEY");
--rollback drop index GENERIC_CACHE_UK;

--changeset cschroed:genericCacheLastTouchedNotNull
ALTER TABLE "GENERIC_CACHE" MODIFY ("LAST_TOUCHED" NOT NULL ENABLE);
--rollback ALTER TABLE "GENERIC_CACHE" MODIFY ("LAST_TOUCHED" NULL);

--changeset cschroed:genericCacheValueClassNotNull
ALTER TABLE "GENERIC_CACHE" MODIFY ("VALUE_CLASS" NOT NULL ENABLE);
--rollback ALTER TABLE "GENERIC_CACHE" MODIFY ("VALUE_CLASS" NULL);

--changeset cschroed:genericCacheKeyNotNull
ALTER TABLE "GENERIC_CACHE" MODIFY ("KEY" NOT NULL ENABLE);
--rollback ALTER TABLE "GENERIC_CACHE" MODIFY ("KEY" NULL);
