DROP TABLE IF EXISTS pw_pokemon_move_detail;

CREATE TABLE IF NOT EXISTS pw_pokemon_move_detail (
  id         INT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '自增主键',
  `desc`     VARCHAR(256) NOT NULL DEFAULT ''
  COMMENT '招式描述',
  imgUrl     VARCHAR(256) NOT NULL DEFAULT ''
  COMMENT '图片链接',
  effect     VARCHAR(256) NOT NULL DEFAULT ''
  COMMENT '附加效果',
  notes      VARCHAR(256) NOT NULL DEFAULT ''
  COMMENT '注意事项',
  scope      VARCHAR(256) NOT NULL DEFAULT ''
  COMMENT '作用范围',
  createTime DATETIME     NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  modifyTime DATETIME     NOT NULL DEFAULT current_timestamp
  ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (id)
)
  ENGINE InnoDB
  DEFAULT CHARSET 'utf8mb4'
  COMMENT '宝可梦招式详情表';