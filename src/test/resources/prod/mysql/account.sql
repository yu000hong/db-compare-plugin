#@formatter:off
CREATE TABLE IF NOT EXISTS `account` (
  `uid`         BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户UID',
  `nickname`    VARCHAR(20) NOT NULL COMMENT '昵称',
  `code`       VARCHAR(40) NULL COMMENT '账号',
  `image`       VARCHAR(500) NOT NULL COMMENT '用户头像',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uid`),
  UNIQUE KEY idx_nickname(`nickname`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 50000;



