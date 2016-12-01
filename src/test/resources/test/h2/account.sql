CREATE TABLE IF NOT EXISTS "account" (
  "uid"         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "nickname"    VARCHAR(40)  NOT NULL,
  "code"        VARCHAR(40)  NOT NULL,
  "image"       VARCHAR(500) NOT NULL,
  "intro"       VARCHAR(60)           DEFAULT '',
  "create_time" TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_nickname ON "account" ("nickname");
CREATE UNIQUE INDEX IF NOT EXISTS idx_code ON "account" ("code");



