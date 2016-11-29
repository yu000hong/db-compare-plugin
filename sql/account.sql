#@formatter:off
CREATE TABLE IF NOT EXISTS `account` (
  `uid`         BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户UID',
  `nickname`    VARCHAR(40) NOT NULL COMMENT '昵称',
  `icode`       VARCHAR(40) NOT NULL COMMENT '账号',
  `image`       VARCHAR(500) NOT NULL COMMENT '用户头像',
  `intro`       VARCHAR(60) DEFAULT '' COMMENT '个人简介',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`uid`),
  UNIQUE KEY idx_code(`icode`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4
  AUTO_INCREMENT = 50000;

#用户隐私设置
#1.加我时需要验证
#2.通过微信号搜索到我
#3.允许陌生人查看10条攻略


