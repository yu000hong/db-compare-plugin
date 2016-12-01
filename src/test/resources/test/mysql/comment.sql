#@formatter:off
CREATE TABLE IF NOT EXISTS `comment` (
  `id`           BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `uid`          BIGINT NULL COMMENT 'UID',
  `article_id`    BIGINT NOT NULL COMMENT '攻略ID',
  `comment`      VARCHAR(100) NOT NULL COMMENT '评论内容',
  `source_uid`    BIGINT COMMENT '原评论UID',
  `source_cid`    BIGINT COMMENT '原评论CID',
  `floor`        INT NOT NULL DEFAULT 0 COMMENT '楼层',
  `status`       TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-正常，1-删除',
  `create_time`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论创建时间',
  PRIMARY KEY (`id`),
  KEY idx_articleId(`article_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
  COMMENT '攻略评论';
