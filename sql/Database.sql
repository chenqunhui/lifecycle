
-- 验证码表
drop table  IF EXISTS verification_code;
create table verification_code(
  id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
  business_type VARCHAR(100) NOT NULL DEFAULT '' COMMENT '业务类型',
  mobile VARCHAR(20) NOT NULL COMMENT '手机号',
  verification_code VARCHAR(20) NOT NULL COMMENT '验证码',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expired_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_valid TINYINT NOT NULL  DEFAULT 1 COMMENT '1:有效 0： 无效',
  INDEX idx_verification_code_mobile(mobile,business_type)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- 用户信息表
drop table IF EXISTS user_info;
CREATE table user_info(
  id  BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
  user_name VARCHAR(100) NOT NULL DEFAULT '',
  mobile VARCHAR(20) NOT NULL COMMENT '手机号',
  password VARCHAR(100) NOT NULL,
  email VARCHAR(100)  NULL,
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_valid TINYINT NOT NULL  DEFAULT 1 COMMENT '1:有效 0： 无效',
  token VARCHAR(300) NULL,
  UNIQUE uk_user_info_mobile(mobile),
  UNIQUE uk_user_info_email(email),
  UNIQUE uk_user_info_token(token),
  INDEX idx_user_info_created_time(created_time),
  INDEX idx_user_info_updated_time(updated_time)
)ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- 操作日志表
drop table IF EXISTS operate_log;
CREATE table operate_log(
  id  BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
  trace_id VARCHAR(50) NOT NULL DEFAULT '' COMMENT '日志跟综号',
  path VARCHAR(500) NOT NULL DEFAULT '' COMMENT '资源地址',
  input VARCHAR(5000) null COMMENT '入参',
  output VARCHAR(5000) null COMMENT '出参',
  remote_info JSON  COMMENT '远端信息',
  created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_operate_log_trace_id(trace_id),
  INDEX idx_operate_log_path_created_time(path,created_time)
)ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;