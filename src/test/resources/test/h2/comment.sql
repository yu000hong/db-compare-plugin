CREATE TABLE IF NOT EXISTS "comment" (
  "id"          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "uid"         BIGINT       NULL,
  "article_id"  BIGINT       NOT NULL,
  "comment"     VARCHAR(100) NOT NULL,
  "source_uid"  BIGINT,
  "source_cid"  BIGINT,
  "floor"       INT          NOT NULL DEFAULT 0,
  "status"      TINYINT      NOT NULL DEFAULT 0,
  "create_time" TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_articleId ON "comment" ("article_id");
