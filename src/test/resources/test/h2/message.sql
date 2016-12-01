CREATE TABLE IF NOT EXISTS "message" (
  "id"          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  "type"        TINYINT      NOT NULL,
  "owner_uid"   BIGINT       NOT NULL,
  "sender_uid"  BIGINT       NOT NULL,
  "comment_id"  BIGINT       NOT NULL DEFAULT 0,
  "status"      TINYINT      NOT NULL DEFAULT 0,
  "data"        VARCHAR(500) NOT NULL,
  "create_time" TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

