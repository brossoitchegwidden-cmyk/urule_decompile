create table URULE_FILE
(
	ID_  	 		NUMBER 		PRIMARY KEY	    not null,       				-- 主键    
    NAME_  			VARCHAR2(255)               not null,       			-- 名称 
    TYPE_  			VARCHAR2(30)                       not null,            -- 文件类型，ruleset,scorecard,decisioncard...  
    LATEST_VERSION_  VARCHAR2(30)					     null,      	-- 当前文件对应最大版本号  
    PACKAGE_ID_ 	 NUMBER           not null,      			-- 所属目录
    PROJECT_ID_ 	 NUMBER           not null,					-- 所属项目  
    CONTENT_      	CLOB                              null,     			-- 内容  
    DIGEST_			VARCHAR2(32)                      null,               	-- 摘要
    LOCKED_USER_	VARCHAR2(255),											-- 文件锁定人
    CREATE_USER_	VARCHAR2(255),			 							-- 文件创建人  
    UPDATE_USER_	VARCHAR2(255),			 							-- 文件更新人  
    CREATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 新建日期  
    UPDATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 更新日期 
    DELETED_		SMALLINT		  	default 0 not null 					-- 删除标记  
);
COMMENT ON TABLE URULE_FILE IS '文件';

create table URULE_VERSION_FILE
(
	ID_  	 		NUMBER 		PRIMARY KEY	     not null,       				-- 主键    
    NAME_  			VARCHAR2(255)               not null,       			-- 名称  
    FILE_ID_   NUMBER 	 					   		not null,       		-- 当前版本文件所属文件  
    PROJECT_ID_ 	NUMBER							not	null, -- 项目
    VERSION_   VARCHAR2(30)					     not null,      			-- 当前文件版本号 
    NOTE_			VARCHAR2(255),											-- 版本备注
    CONTENT_      	CLOB                              null,     			-- 内容  
    DIGEST_		VARCHAR2(32)                          null,               	-- 摘要
    CREATE_USER_	VARCHAR2(255),			 							-- 文件创建人  
    CREATE_DATE_  TIMESTAMP    default sysdate   not null     -- 新建日期  
);
COMMENT ON TABLE URULE_VERSION_FILE IS '版本文件';

create table URULE_PACKAGE
(
	ID_  	 		NUMBER   PRIMARY KEY	       not null,      			-- 主键  
   	NAME_ 			VARCHAR2(255)             not null,                 -- 目录名称
   	TYPE_ 			VARCHAR2(255)             not null, 				-- 目录类型 
    PARENT_ID_ 		NUMBER         not null,                 -- 所属目录
    PROJECT_ID_ 		NUMBER        not null,					-- 所属项目  
    CREATE_USER_	VARCHAR2(255),			 							-- 文件创建人  
    UPDATE_USER_	VARCHAR2(255),			 							-- 文件更新人  
    CREATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 新建日期  
    UPDATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 更新日期 
    DELETED_		SMALLINT		  	default 0 not null 					-- 删除标记  
);
COMMENT ON TABLE URULE_PACKAGE IS '目录';

create table URULE_PROJECT
(
	ID_  	 	NUMBER    PRIMARY KEY	      not null,                				-- 主键  
    NAME_ 		VARCHAR2(255)              not null,                            -- 项目名称
    DESC_ 		VARCHAR2(255)              null,                            -- 项目备注
    TYPE_ 		VARCHAR2(50)               not null,                             -- 项目类型
    VIEW_MODEL_  VARCHAR2(20)               null,                             -- 视图类型：category:分类视图,fileset:统一视图
    GROUP_ID_ 	VARCHAR2(255)              not null,         			-- 所属团队ID  
    APPROVE_USER_ENABLE_ 	VARCHAR2(50)  		      not null,         				-- 启用审批用户
    APPROVE_USER_DISABLE_ 	VARCHAR2(50)  		      not null,         				-- 禁用审批用户
    APPROVE_USER_DEPLOY_ 	VARCHAR2(50)  		      not null,         				-- 发布审批用户  
    CREATE_USER_	VARCHAR2(255),			 							-- 文件创建人  
    UPDATE_USER_	VARCHAR2(255),			 							-- 文件更新人  
    CREATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 新建日期  
    UPDATE_DATE_  TIMESTAMP    default sysdate   not null     -- 更新日期 
);
COMMENT ON TABLE URULE_PROJECT IS '项目';

create table URULE_URL_CONFIG
(
	ID_  	 	NUMBER    PRIMARY KEY not null,               					-- 主键  
    NAME_ 		VARCHAR2(255)             not null,                             -- 名称
    URL_ 		VARCHAR2(255)             not null,                             -- URL
    TYPE_ 	VARCHAR2(10)  		     not null,         							-- 类型：cluster,client
    GROUP_ID_ 	VARCHAR2(50)  		     not null,         						-- 所属团队ID
    CREATE_USER_	VARCHAR2(255),												-- 文件创建人 
    UPDATE_USER_	VARCHAR2(255),												-- 文件更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate  not null,     			-- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate  not null     			-- 更新日期
);
COMMENT ON TABLE URULE_URL_CONFIG IS  '集群服务器及客户端URL配置表';


create table URULE_DYNAMIC_JAR
(
	ID_  	 	NUMBER   PRIMARY KEY not null,               						-- 主键  
    DESC_ 		VARCHAR2(255)             not null,                             	-- 描述
    NAME_ 		VARCHAR2(255),						                             	-- jar文件名
    GROUP_ID_ 	VARCHAR2(50)  		     not null,         							-- 所属团队ID
    JAR_ 		BLOB,							                             		-- 具体jar文件
    CREATE_USER_	VARCHAR2(255),												-- 文件创建人 
    UPDATE_USER_	VARCHAR2(255),												-- 文件更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate  not null,     			-- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate  not null     			-- 更新日期
) ;
COMMENT ON TABLE URULE_DYNAMIC_JAR IS '动态JAR文件存储表';

create table URULE_PACKET(
	ID_  	 	NUMBER    PRIMARY KEY	     not null,               						-- 主键
	PROJECT_ID_ 		NUMBER            			   not null, 		-- 所属项目 
	NAME_ 		VARCHAR2(255)              not null,                             -- 知识包名称
    CODE_ 		VARCHAR2(255)              not null,                             -- 知识包编码
	TYPE_		VARCHAR2(10)				  not null,                 -- 知识包类型，有两种类型：upload表示上传上来的知识包导出文件,file表示绑定具体的规则文件
	DESC_ 		VARCHAR2(255)              not null,                             -- 知识包描述
	INPUT_DATA_ 		CLOB,												-- 快速测试时留下的JSON格式输入数据
    OUTPUT_DATA_ 		CLOB,												-- 快速测试时留下的JSON格式输出数据
	AUDIT_ENABLE_ 		SMALLINT default 0	not null, 						-- 调用当前知识包是否需要调用信息
    AUDIT_INPUT_ 				CLOB,										-- 调用当前知识包记录下的JSON格式输入数据
	AUDIT_OUTPUT_ 				CLOB,										-- 调用当前知识包记录下的JSON格式输出数据
    REST_ENABLE_ 		SMALLINT default 0	not null, 						-- REST服务是否启用
    REST_SECURITY_ENABLE_ 		SMALLINT default 0	not null, 				-- REST服务是否需要安全验证
	REST_SECURITY_USER_ 		VARCHAR2(255),             						-- REST服务需要安全验证的用户名
	REST_SECURITY_PASSWORD_ 	VARCHAR2(255),             						-- REST服务需要安全验证的密码
	REST_INPUT_ 		CLOB,												-- REST服务需要的JSON格式输入数据
    REST_OUTPUT_ 		CLOB,												-- REST服务需要的JSON格式输出数据
    CREATE_USER_	VARCHAR2(255),												-- 创建人 
    UPDATE_USER_	VARCHAR2(255),												-- 更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate  not null,      -- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate  not null,      -- 更新日期
    ENABLE_ 		SMALLINT default 0 		not null			-- 启用标记
);
COMMENT ON TABLE URULE_PACKET IS '知识包定义主表';
CREATE UNIQUE INDEX URULE_PACKET_CODE_INDEX ON URULE_PACKET(CODE_);

create table URULE_PACKET_PACKAGE(
	ID_  			NUMBER PRIMARY KEY             not null,               				-- 主键
	PACKET_ID_ 		NUMBER            			   not null, 					-- 所属知识包 
	PROJECT_ID_ 	NUMBER							not	null, 					-- 项目
	CONTENT_      	CLOB                       not null,            				-- 上传的编译后的知识包内容 
	DESC_ 		VARCHAR2(255),						                             		-- 描述
	CREATE_USER_	VARCHAR2(255),														-- 创建人 
    UPDATE_USER_	VARCHAR2(255),														-- 更新人 
    CREATE_DATE_    TIMESTAMP     default sysdate  not null,     				-- 新建日期
    UPDATE_DATE_    TIMESTAMP     default sysdate  not null     				-- 更新日期
);
COMMENT ON TABLE URULE_PACKET_PACKAGE IS '知识包定义从表，存储导出后知识包';

create table URULE_PACKET_FILE(
	ID_  	 	NUMBER     PRIMARY KEY	    not null,               								-- 主键
	PACKET_ID_ 		NUMBER           			   not null, 						-- 所属知识包 
	FILE_ID_ 		NUMBER           			   not null, 					-- 关联到的文件ID 
	PROJECT_ID_ 	NUMBER							not	null, 					-- 项目
	PATH_ 		VARCHAR2(255)              not null,                             		-- 关联文件的路径
	VERSION_ 		VARCHAR2(30),  				                           		-- 关联到的文件版本号
	DESC_ 		VARCHAR2(255),						                             		-- 描述
	CREATE_USER_	VARCHAR2(255),														-- 创建人 
    UPDATE_USER_	VARCHAR2(255),														-- 更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate  not null,     		    -- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate  not null     			-- 更新日期
);
COMMENT ON TABLE URULE_PACKET_FILE IS '知识包定义从表,文件表';

create table URULE_DEPLOYED_PACKET(
	ID_  	 	NUMBER   PRIMARY KEY	    not null,               						-- 主键
	PACKET_ID_ 		NUMBER           		 not null, 						-- 所属知识包 
	PROJECT_ID_ 	NUMBER							not	null, -- 项目
	DESC_ 		VARCHAR2(255)              not null,                             -- 描述
	APPLY_ID_ 		NUMBER           		 not null, 				-- 发布此知识包的审批流ID
	CONTENT_      CLOB                       not null,            			-- 编译后的知识包内容  
	DIGEST_		VARCHAR2(32)                            null,               		-- 知识包内容摘要
	VERSION_ 		VARCHAR2(30)              not null,                          -- 发布的知识包版本号
	STATUS_ 		VARCHAR2(30)           not null, 							-- 当前发布的知识包审批状态
	ENABLE_ 		SMALLINT           default 0 		not null, 			-- 启用标记
	CREATE_USER_	VARCHAR2(255),												-- 创建人
    CREATE_DATE_         TIMESTAMP     default sysdate  not null      -- 新建日期
);
COMMENT ON TABLE URULE_DEPLOYED_PACKET IS '已发布知识包主表';

create table URULE_DEPLOYED_PACKET_FILE(
	ID_  	 	NUMBER    PRIMARY KEY	    not null,               						-- 主键
	DEPLOYED_PACKET_ID_ 		NUMBER          not null, 					-- 所属发布知识包ID
	FILE_ID_ 		NUMBER          not null, 							-- 原文件ID
	PROJECT_ID_ 	NUMBER							not	null, -- 项目
	DIGEST_		VARCHAR2(32)                     not null,               		-- 摘要
	PATH_ 		VARCHAR2(255)              not null,                             -- 文件路径 
	VERSION_ 		VARCHAR2(30),                         					 -- 文件版本号
	CONTENT_      CLOB                       not null,            			-- 文件内容  
	CREATE_USER_	VARCHAR2(255),												-- 创建人
    CREATE_DATE_         TIMESTAMP     default sysdate  not null     -- 新建日期
);
COMMENT ON TABLE URULE_DEPLOYED_PACKET_FILE IS '已发布知识包从表，记录包含的文件快照';

create table URULE_PACKET_APPLY(
	ID_  	 	NUMBER    PRIMARY KEY	    not null,               						-- 主键
	PACKET_ID_ 		NUMBER default 0           not null, 								-- 所属知识包ID
	DEPLOYED_PACKET_ID_ 		NUMBER default 0           not null, 					-- 所属发布知识包ID
	PROJECT_ID_ 	NUMBER									not null, -- 项目编号
	TYPE_ 		VARCHAR2(30)              not null,                              -- 申请类型，enable:表示知识包启用申请；deploy:表示知识包发布申请 
	TITLE_ 		VARCHAR2(255)              not null,                             -- 审核标题 
	DESC_ 		VARCHAR2(255)              not null,                             -- 审核描述 
	APPROVER_	VARCHAR2(255),												-- 审核人
	STATUS_		VARCHAR2(30)				not null,								-- 审核结果，pass-通过,reject-驳回,fail-不通过
	CREATE_USER_	VARCHAR2(255),												-- 创建人
    CREATE_DATE_         TIMESTAMP     default sysdate  not null,      -- 新建日期
   	UPDATE_DATE_         TIMESTAMP     default sysdate  not null      -- 新建日期
);
COMMENT ON TABLE URULE_PACKET_APPLY IS '发布知识包审核主表';

create table URULE_PACKET_APPLY_DETAIL(
	ID_  	 	NUMBER    PRIMARY KEY	    not null,               						-- 主键
	APPLY_ID_ 		NUMBER default 0           not null, 							-- 所属申请项ID
	PROJECT_ID_ 	NUMBER							not	null, -- 项目
	DESC_ 		VARCHAR2(255)              not null,                             -- 审核描述 
	CREATE_USER_	VARCHAR2(255),												-- 创建人
    CREATE_DATE_         TIMESTAMP     default sysdate  not null     	-- 新建日期
);
COMMENT ON TABLE URULE_PACKET_APPLY_DETAIL IS '发布知识包审核从表，记录审核过程';

create table URULE_PACKET_SCENARIO
(
	ID_  	 			NUMBER      PRIMARY KEY	    	    not null,				-- 主键
   	PACKET_ID_			NUMBER                            			not null,				-- 所属知识包ID
   	PROJECT_ID_ 	    NUMBER							not	null, -- 项目
    NAME_				VARCHAR2(255)							not null,				-- 名称
    DESC_				VARCHAR2(255)                            not null,				-- 描述
    EXCEL_FILE_			BLOB,															-- 上传的Excel文件
    EXCEL_FILE_NAME_	VARCHAR2(255),													-- 上传的Excel文件名
    INPUT_DATA_ 		CLOB,															-- JSON格式输入数据
    OUTPUT_DATA_ 		CLOB,															-- JSON格式输出数据
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      					-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      					-- 更新日期
);
COMMENT ON TABLE URULE_PACKET_SCENARIO IS '场景批量测试';

create table URULE_DATASOURCE
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,		-- 主键
   	GROUP_ID_ 	        VARCHAR2(50)						not	null,       -- 团队编号
    NAME_				VARCHAR2(255)					not null,	    -- 名称
    TYPE_               VARCHAR2(20)				        not null,		-- 数据源类型: JDBC, JNDI, CUSTOM
    DATASOURCE_BEAN_    VARCHAR2(255)					    null,		-- 自定义DataSource连接类
    DB_JNDI_NAME_       VARCHAR2(255)					    null,		-- JNDI名称
    DB_DRIVER_          VARCHAR2(255)					    null,		-- DRIVER
    DB_URL_             VARCHAR2(255)					    null,		-- URL
    DB_USER_            VARCHAR2(100)					    null,		-- 用户
    DB_PWD_             VARCHAR2(255)					    null,		-- 密码
    DB_VALIDATION_QUERY_ VARCHAR2(255)					    null,		-- 密码
    DB_INITIAL_SIZE_    NUMBER 							        null,		-- 初始化连接数
    DB_MAX_TOTAL_       NUMBER 							        null,		-- 最大活动连接数
    DB_MAX_IDLE_        NUMBER 							        null,		-- 最大空闲连接数
    DB_MIN_IDLE_        NUMBER 							        null,		-- 最小空闲连接数
    DESC_				VARCHAR2(255)                        null,	    -- 描述
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_DATASOURCE IS '数据仓库';

create table URULE_BATCH
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    NAME_				VARCHAR2(255)					not null,				-- 名称
    ENABLE_ 		    SMALLINT default 0	not null, 						-- 是否启用
    SKIP_LIMIT_         NUMBER							        null,               -- 异常数量限定
    ASYNC_              SMALLINT default 1	        not null, 				-- 是否启用异步处理
    CALLBACK_URL_		VARCHAR2(255)					    null,				-- 异步回调URL
    STATUS_             VARCHAR2(20)                 	    null,               -- 状态: 状态: completed:成功, failed:失败, started:执行中,  none:无
    LISTENER_   	    VARCHAR2(255)					    null,				-- 批处理监测类
    THREAD_MULTI_       SMALLINT default 1	        not null, 				-- 是否启用多线程处理
    THREAD_SIZE_        NUMBER 							        null,               -- 开启线程数
    THREAD_DATA_SIZE_   NUMBER 							        null,               -- 单个线程处理数据量
    PROVIDER_ID_ 	    NUMBER 							        null,               -- 数据查询方案编号
    RESOLVER_ID_ 	    NUMBER 							        null,               -- 数据保存方案编号
   	PACKET_ID_			NUMBER                             	    null,				-- 对应的知识包ID
	PACKET_INPUT_DATA_ 	CLOB                            null,				-- 知识包输入参数
    REST_ENABLE_ 		SMALLINT default 0	not null, 						-- REST服务是否启用
    REST_SECURITY_ENABLE_ 		SMALLINT default 0	not null, 				-- REST服务是否需要安全验证
	REST_SECURITY_USER_ 		VARCHAR2(255),             						-- REST服务需要安全验证的用户名
	REST_SECURITY_PASSWORD_ 	VARCHAR2(255),             						-- REST服务需要安全验证的密码
	INPUT_DATA_ 		CLOB                            null,				-- 输入参数
    DESC_				VARCHAR2(255)                        null,				-- 描述
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_BATCH IS '批处理方案';

create table URULE_BATCH_DATA_PROVIDER
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
   	BATCH_ID_			NUMBER                             	not null,				-- 所属方案ID
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    NAME_				VARCHAR2(255)					not null,		        -- 名称
    DATASOURCE_ID_      NUMBER  							    null,		        -- 数据仓库ID
    LISTENER_    	    VARCHAR2(255)					    null,				-- 批处理监测类
	INPUT_DATA_ 		CLOB,												-- 输入参数
    PACKET_VAR_NAME_	VARCHAR2(100)					not null,		    -- 知识包变量名
    SUPPORT_PAGING_     SMALLINT DEFAULT 0            not null,            -- 是否支持分页查询,多线程情况下默认使用分页查询
    PAGE_SIZE_          NUMBER   DEFAULT 100                not null,       -- 多线程情况下该参数无效,系统引用采用单线程数据量的参数作为分页参数
    PAGE_SQL_ 		    CLOB,												-- 分页查询sql
    ORDER_FIELD_ 		VARCHAR2(255)	null,								-- Hive, Presto分页查询sql对应的order by
    ORDER_FIELD_PARAM_NAME_ 		VARCHAR(255)	null,					-- Hive的limit分页orderBy字段对应的参数名
    PAGE_LIMIT_TYPE_ 		VARCHAR(255)	null,							-- Hive分页方式
    COUNT_SQL_ 		    CLOB,												-- 总记录数sql
	VALIDATOR_DATA_ 	CLOB,												-- 数据校验器
    DESC_				VARCHAR2(255)                        null,		        -- 描述
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_BATCH_DATA_PROVIDER IS '批处理查询方案';

create table URULE_BATCH_PROVIDER_FIELD
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    BATCH_ID_			NUMBER                             	not null,				-- 所属方案ID
    PROVIDER_ID_ 	    NUMBER 							    not	null,               -- 查询方案编号
    SRC_PROPERTY_		VARCHAR2(255)							not null,		-- 原属性名称
    DATA_TYPE_			VARCHAR2(255)							not null,		-- 数据类型
    DEST_PROPERTY_		VARCHAR2(255)                            not null,		-- 目标属性名称
    CLAZZ_PATH_		    VARCHAR2(255)                                null,		-- 目标属性classpath
    DATA_PROVIDER_ID_ 	NUMBER							        null,               -- DATA_TYPE_类型为Object或List时对应查询方案编号
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_BATCH_PROVIDER_FIELD IS '批处理查询方案数据映射';

create table URULE_BATCH_DATA_RESOLVER
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
    BATCH_ID_			NUMBER                             	not null,				-- 所属方案ID
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    DATASOURCE_ID_      NUMBER  							    null,		        -- 数据仓库ID
    NAME_				VARCHAR2(255)						not null,		        -- 名称
    LISTENER_    	    VARCHAR2(255)					    null,				-- 批处理监测类
    TRAN_SCOPE_			VARCHAR2(255)						not null,		        -- 事务范围
	VALIDATOR_DATA_ 	CLOB,				                                        -- 数据校验器
    DESC_				VARCHAR2(255)                                null,		-- 描述
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_BATCH_DATA_RESOLVER IS '批处理保存方案';

create table URULE_BATCH_RESOLVER_ITEM
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
    BATCH_ID_			NUMBER                             	not null,				-- 所属方案ID
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    RESOLVER_ID_ 	    NUMBER 							    not	null,               -- 保存方案编号
    NAME_				VARCHAR2(255)					not null,		        -- 名称
    UPDATE_MODE_		VARCHAR2(255)                        null,		        -- 更新类型
    TABLE_NAME_			VARCHAR2(255)                        null,		        -- 目标表
	VALIDATOR_DATA_ 	CLOB,												-- 数据校验器
    PARTITION_NAME_		VARCHAR(255)                        null,		        -- 分区名称
    PARTITION_VALUE_	VARCHAR(255)                        null,		        -- 分区值
    COMMIT_LIMIT_		NUMBER                             null,		        -- 最小提交记录数
    DESC_				VARCHAR2(255)                        null,		        -- 描述
    CREATE_USER_		VARCHAR2(255),											-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),											-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      	-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      	-- 更新日期
);
COMMENT ON TABLE URULE_BATCH_RESOLVER_ITEM IS '批处理保存方案明细项';

create table URULE_BATCH_RESOLVER_FIELD
(
	ID_  	 			NUMBER     PRIMARY KEY	    	    not null,				-- 主键
   	PROJECT_ID_ 	    NUMBER 							    not	null,               -- 项目编号
    BATCH_ID_			NUMBER                             	not null,				-- 所属方案ID
    RESOLVER_ID_ 	    NUMBER 							    not	null,               -- 保存方案编号
    ITEM_ID_ 	        NUMBER 							    not	null,               -- 保存方案明细项编号
    SRC_PROPERTY_	    VARCHAR2(255)							not null,		-- 原属性名称
    DATA_TYPE_			VARCHAR2(255)							not null,		-- 数据类型
    KEY_                SMALLINT		  	        default 0 not null,       -- 是否主键
    DEST_PROPERTY_		VARCHAR2(255)                            not null,		-- 目标属性名称
    CREATE_USER_		VARCHAR2(255),													-- 文件创建人 
    UPDATE_USER_		VARCHAR2(255),													-- 文件更新人 
    CREATE_DATE_        TIMESTAMP     default sysdate not null,      			-- 新建日期
    UPDATE_DATE_        TIMESTAMP     default sysdate not null      			-- 更新日期
);
COMMENT ON TABLE URULE_BATCH_RESOLVER_FIELD IS  '批处理保存方案数据映射';

create table URULE_PROPERTY
(
	ID_  	 	NUMBER              PRIMARY KEY	      not null,    -- 主键             
    KEY_		VARCHAR2(255)                            not null,  -- 配置键            
    VALUE_		VARCHAR2(255)							not null,   -- 键值             
    LABEL_		VARCHAR2(255)                            not null,  -- 说明             
    TYPE_		VARCHAR2(255)                            not null,	-- 类型system,custom
    CREATE_USER_	VARCHAR2(255),			 							-- 文件创建人  
    UPDATE_USER_	VARCHAR2(255),			 							-- 文件更新人  
    CREATE_DATE_  TIMESTAMP    default sysdate   not null,     -- 新建日期  
    UPDATE_DATE_  TIMESTAMP    default sysdate   not null     -- 更新日期 
);
COMMENT ON TABLE URULE_PROPERTY IS '配置';

create table URULE_GROUP
(
	ID_  	 		VARCHAR2(50)		PRIMARY KEY			not null, -- 主键
	NAME_ 			VARCHAR2(255)                        not null, -- 团队名称
    DESC_ 			VARCHAR2(255)      				        null, -- 备注
    CREATE_USER_	VARCHAR2(255),										-- 文件创建人 
    UPDATE_USER_	VARCHAR2(255),										-- 文件更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate   not null,      -- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate   not null     -- 更新日期
);
COMMENT on table URULE_GROUP is '团队';

create table URULE_GROUP_ROLE
(
	ID_  	 		NUMBER			PRIMARY KEY	    	 not null, -- 主键
	NAME_ 			VARCHAR2(255)                        not null, -- 团队用户组
	TYPE_			VARCHAR2(50)                         not null, -- 类型system,custom
	GROUP_ID_		VARCHAR2(50)                         not null, -- 团队编号
    CREATE_USER_	VARCHAR2(255),										-- 文件创建人 
    UPDATE_USER_	VARCHAR2(255),										-- 文件更新人 
    CREATE_DATE_         TIMESTAMP     default sysdate   not null,      -- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate   not null     -- 更新日期
);
COMMENT on table URULE_GROUP_ROLE is '团队角色';

create table URULE_USER
(
	ID_  	 	VARCHAR2(50)  	PRIMARY KEY	    		not null, -- 账号ID,主键
	NAME_ 		VARCHAR2(255)                        	not null, -- 用户名
    PASSWORD_ 		VARCHAR2(255)                        not null, -- 密码
    EMAIL_	 		VARCHAR2(255)                        	null, -- 邮件
    SECRET_KEY_		VARCHAR2(6)								null, -- 密码找回密钥 
    DESC_ 			VARCHAR2(255)      			            null, -- 备注
    CREATE_USER_	VARCHAR2(255)							null, -- 创建人 
    UPDATE_USER_	VARCHAR2(255)						    null, -- 更新人 
    EXPIR_DATE_          TIMESTAMP      default sysdate  	null,  -- 密码找回过期时间
    CREATE_DATE_         TIMESTAMP     default sysdate not null,   -- 新建日期
    UPDATE_DATE_         TIMESTAMP     default sysdate not null   -- 更新日期
);
COMMENT on table URULE_USER is '用户';

create table URULE_GROUP_USER
(
	ID_  	 		NUMBER	PRIMARY KEY	    not null, -- 主键
    USER_ID_ 		VARCHAR2(50)	not null, -- 用户
    USER_NAME_ 		VARCHAR2(255)   not null, -- 用户名
    GROUP_ID_ 		VARCHAR2(50)	not null, -- 团队
    CREATE_DATE_    TIMESTAMP    default sysdate  not null      -- 加入日期
);
COMMENT on table URULE_GROUP_USER is '团队用户';

create table URULE_PROJECT_USER
(
	ID_  	 		NUMBER	PRIMARY KEY	    not null, -- 主键
    USER_ID_ 		VARCHAR2(50)	not null, -- 用户
    USER_NAME_ 		VARCHAR2(255)   not null, -- 用户名
    PROJECT_ID_ 	NUMBER			not null, -- 项目
    CREATE_DATE_    TIMESTAMP    default sysdate  not null      -- 加入日期
);
COMMENT on table URULE_PROJECT_USER is '项目用户';

create table URULE_PROJECT_ROLE
(
	ID_  	 		NUMBER	PRIMARY KEY	    			 not null, -- 主键
	NAME_ 			VARCHAR2(255)                        not null, -- 项目用户组
    DESC_ 			VARCHAR2(255)        					null, -- 备注
    PROJECT_ID_ 	NUMBER              				 not null, -- 所属项目
    TYPE_			VARCHAR2(50)                         not null, -- 类型system,custom
    CREATE_USER_	VARCHAR2(255),										-- 文件创建人 
    UPDATE_USER_	VARCHAR2(255),										-- 文件更新人 
    CREATE_DATE_    TIMESTAMP    default sysdate   not null,      -- 新建日期
    UPDATE_DATE_    TIMESTAMP    default sysdate   not null     -- 更新日期
);
COMMENT on table URULE_PROJECT_ROLE IS '项目角色';

create table URULE_PROJECT_USER_ROLE
(
	ID_  	 		NUMBER	PRIMARY KEY	    	not null, -- 主键
	PROJECT_ID_ 	NUMBER							not	null, -- 项目
    USER_ID_ 		VARCHAR2(50)        not null, -- 用户
    ROLE_ID_ 		NUMBER              not null -- 角色ID
);
COMMENT on table URULE_PROJECT_USER_ROLE IS '用户项目角色关系表';

create table URULE_GROUP_USER_ROLE
(
	ID_  	 		NUMBER	PRIMARY KEY	     not null, -- 主键
	GROUP_ID_ 		VARCHAR2(50)			not	null, -- 项目
    USER_ID_ 		VARCHAR2(50)     not null, -- 用户
    ROLE_ID_ 		NUMBER           not null -- 角色ID
);
COMMENT on table URULE_GROUP_USER_ROLE IS '用户团队角色关系表';

create table URULE_INVITE
(
	ID_  	 		NUMBER		PRIMARY KEY	    not null, -- 主键
    GROUP_ID_ 		VARCHAR2(50)					not null, -- 团队编号
    TYPE_ 			VARCHAR2(50)					not null, -- 类型,可以为TIME, DISPOSABLE
    SECRET_KEY_		VARCHAR2(6)						null, -- 密钥 
    EXPIR_DATE_     TIMESTAMP     default sysdate  null,  -- 过期时间
    CREATE_USER_	VARCHAR2(255),									-- 创建人 
    CREATE_DATE_    TIMESTAMP     default sysdate  null  -- 创建日期
);
COMMENT on table URULE_INVITE IS '邀请表';


create table URULE_AUTHORITY
(
	ID_  	 		NUMBER	PRIMARY KEY	    				not null, -- 主键
    ROLE_ID_ 		NUMBER                     	not null, -- 角色ID
    ROLE_TYPE_ 		VARCHAR2(50)					not null, -- 角色类型,可以为GROUP, PROJECT
    RESOURCE_CODE_ 	VARCHAR2(50) 				not null, -- 资源编码
    RESOURCE_TYPE_ 	VARCHAR2(50)					not null, -- 资源类型,可以为:URL, RULE_FILE, FUNC
    AUTH_ 			NUMBER           default 1  not null -- 授权信息
);
COMMENT on table URULE_AUTHORITY IS  '角色授权表';

create table URULE_LOG_USERLOGIN
(
	ID_  	 		NUMBER		PRIMARY KEY	    not null, -- 主键
    USER_ID_ 		VARCHAR2(50)                 not null, -- 用户账号
    USER_NAME_ 		VARCHAR2(50)                 not null, -- 用户名
    GROUP_ID_ 		VARCHAR2(50)					 	null, -- 团队编号
    IP_				VARCHAR2(50)                 	null, -- 登录地址
    USER_AGENT_		VARCHAR2(255) 	                null, -- 用户代理
    CREATE_DATE_    TIMESTAMP    default sysdate   null  -- 创建时间
);
COMMENT on table URULE_LOG_USERLOGIN IS '登录日志表';

create table URULE_LOG_OPERATION
(
	ID_  	 		NUMBER		PRIMARY KEY	    not null, -- 主键
    USER_ID_ 		VARCHAR2(50)                not null, -- 用户账号
    USER_NAME_ 		VARCHAR2(50)                not null, -- 用户名
    GROUP_ID_ 		VARCHAR2(50)				not null, -- 团队编号
    GROUP_NAME_ 	VARCHAR2(255)					null, -- 团队名称
   	PROJECT_ID_ 	NUMBER							null, -- 项目编号
    PROJECT_NAME_ 	VARCHAR2(255)					null, -- 项目名称
    ITEM_ID_ 		VARCHAR2(50)						null, -- 业务对象编号
    CATEGORY_		VARCHAR2(50)                 	null, -- 模块
    ACTION_			VARCHAR2(100)                 	null, -- 动作
    CONTENT_		VARCHAR2(255)                 	null, -- 操作内容
    CREATE_DATE_    TIMESTAMP    default sysdate    null  -- 创建时间
);
COMMENT on table URULE_LOG_OPERATION IS '操作日志表';

create table URULE_LOG_KNOWLEDGE
(
	ID_  	 		NUMBER		PRIMARY KEY	    not null, -- 主键
    USER_ 			VARCHAR2(50)                 	null, -- 执行用户
    IP_				VARCHAR2(50)                 	null, -- 登录地址
    USER_AGENT_		VARCHAR2(255) 	                null, -- 用户代理
    KNOWLEDGE_ID_	NUMBER			                null, -- 知识包ID
    KNOWLEDGE_NAME_	VARCHAR2(100)                 	null, -- 知识包Name
    VERSION_		VARCHAR2(30)                 	null, -- 版本号
    IN_PARAMS_      CLOB                            not null, -- 输入参数
    OUT_PARAMS_     CLOB                            not null, -- 输出参数
    LOGS_		    CLOB                            not null, -- 执行日志
    START_TIME_     TIMESTAMP    default sysdate   null,  -- 调用开始时间
    END_TIME_     	TIMESTAMP    default sysdate   null,  -- 调用结束时间
    TIME_			NUMBER              	null, -- 耗时
    GROUP_ID_ 		VARCHAR2(50)					not null, -- 团队编号
    GROUP_NAME_ 	VARCHAR2(255)					null, -- 团队名称
   	PROJECT_ID_ 	NUMBER						not null, -- 项目编号
    PROJECT_NAME_ 	VARCHAR2(255)					null, -- 项目名称
    CREATE_DATE_    TIMESTAMP    default sysdate  null  -- 创建时间
);
COMMENT on table URULE_LOG_KNOWLEDGE IS '规则执行日志表';

create table URULE_LOG_BATCH
(
	ID_  	 		NUMBER 		PRIMARY KEY	    not null, -- 主键
    USER_ 			VARCHAR2(50)                 	null, -- 执行用户
    IP_				VARCHAR2(50)                 	null, -- 登录地址
    USER_AGENT_		VARCHAR2(255) 	                null, -- 用户代理
    BATCH_ID_	    NUMBER 			                 	null, -- 方案ID
    BATCH_NAME_	    VARCHAR2(100)                 	null, -- 方案Name
    STATUS_         VARCHAR2(20)                 	null, -- 状态: completed:成功, failed:失败, started:执行中,  none:无
    READ_COUNT_     NUMBER                  	    null, -- 数据总数
    FILTER_COUNT_   NUMBER                  	    null, -- 过滤数据总数
    ITEM_DATA_      CLOB                 	    null, -- 数据保存项统计日志
    IN_PARAMS_      CLOB                    	null, -- 输入参数
    PACKET_ID_      NUMBER                    	        null, -- 知识包ID
    PACKET_PARAMS_  CLOB                    	    null, -- 知识包输入参数
    START_TIME_     TIMESTAMP      default sysdate  null,  -- 调用开始时间
    END_TIME_     	TIMESTAMP      default sysdate  null,  -- 调用结束时间
    TIME_			NUMBER 			                 	null, -- 耗时
    MSG_    		VARCHAR2(255)			        null, -- 异常信息
    DETAIL_    		CLOB			                null, -- 异常详细信息
    GROUP_ID_ 		VARCHAR2(50)					not null, -- 团队编号
    GROUP_NAME_ 	VARCHAR2(255)					null, -- 团队名称
   	PROJECT_ID_ 	NUMBER 							not null, -- 项目编号
    PROJECT_NAME_ 	VARCHAR2(255)					null, -- 项目名称
    CREATE_DATE_    TIMESTAMP      default sysdate  null  -- 创建时间
);
COMMENT on table URULE_LOG_BATCH IS '批处理执行日志表';

create table URULE_LOG_BATCH_SKIP
(
	ID_  	 		NUMBER		PRIMARY KEY	    not null, -- 主键
    BATCH_ID_	    NUMBER			                not null, -- 方案ID
    LOG_ID_  	 	NUMBER		                    not null, -- 批处理日志ID
    TYPE_           VARCHAR2(20)                 	null, -- 类型: base:通用异常, reader:数据加载失败, processor:规则执行,  writer:数据写入, filter:数据过滤
    MSG_    		VARCHAR2(255)			        null, -- 异常信息
    DETAIL_    		CLOB			            null, -- 异常详细信息
    DATA_           CLOB                 	    null, -- 规则数据
    GROUP_ID_ 		VARCHAR2(50)					not null, -- 团队编号
   	PROJECT_ID_ 	NUMBER							not null, -- 项目编号
    CREATE_DATE_    TIMESTAMP      default sysdate  null  -- 创建时间
);
COMMENT on table URULE_LOG_BATCH_SKIP is '批处理执行异常日志表';