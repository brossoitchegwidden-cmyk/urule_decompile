# URule 4.3.0 反编译源码全面可读性修复报告

- 日期：2026-08-24
- 工作区：`I:\tmp\urule-decompile\project`
- 可编译源码范围：`urule-core-pro/src/main/java`、`urule-console-pro/src/main/java`
- 前端验证范围：`urule-console-js`
- 结论：本报告定义的反编译占位符、混淆式类名/方法名/字段名、无效占位注释和直接调试堆栈输出已全部清零，机器验收修复率为 100%

## 1. 执行摘要

本轮不是只恢复方法入参。修复范围覆盖类名、方法名、字段名、参数名、局部变量、注释、异常处理和若干因反编译丢失或扭曲的行为。

两个后端模块当前共有 1,261 个可编译 Java 文件，其中核心模块 721 个、控制台模块 540 个。原基线中匹配 `varN` 的标识符出现 59,254 次，当前为 0。单字母类型、方法和字段，数字匿名类，机械兜底方法名和字段名，格式串占位字段，直接无参 `printStackTrace()`，以及反编译器占位注释也均为 0。

这意味着“已定义且可自动复验的反编译/混淆残留”修复率达到 100%。但它不等于能够证明每个私有符号都恢复成厂商编译前的原始拼写：原始 JAR 没有完整 `LocalVariableTable`，也没有混淆映射文件。无法取证原名时，本项目按类型、调用关系、数据流和业务职责恢复为明确的语义名称；这是可维护源码恢复，不是对未知原名的虚假声明。

## 2. 范围与边界

纳入 100% 验收的目录：

- `urule-core-pro/src/main/java`
- `urule-console-pro/src/main/java`

以下目录是保留的原始或中间反编译取证快照，不是 Maven 源码根目录，不参与构建和修复率：

- `urule-core-pro/src/main/java_cfr_backup`
- `urule-core-pro/src/main/java_hybrid`
- `urule-core-pro/src/main/java_restored`
- `urule-core-pro/src/main/java_vf`

主源码中的 80 个 `RuleParserParser$...Context` 文件属于 ANTLR 语法规则上下文，是生成器规定的公开类型名；`ElCompute$DataWrapper` 和 `CacheUtils$CacheInstance` 也是已有明确职责的嵌套类型。这 82 个 `$` 文件不是数字匿名类残留，按设计保留。ANTLR 的 `T__0` 等 token 名以及 `SystemUtils.IS_JAVA_1_1` 等版本常量同样是协议/兼容名称，不属于混淆字段。

## 3. 恢复依据

恢复使用以下证据交叉确认：

- URule Professional 4.3.0 原始 JAR 的类型、字节码和调用结构。
- 本机 Maven 仓库中的同版本 Javadoc。
- getter/setter、构造赋值、字段类型、集合元素类型、SQL 列、XML 节点、Servlet 路由和继承链。
- Java AST 作用域分析与声明/引用同步改名。
- Maven 编译、单元测试、前端测试和生产构建。

| 原始产物 | SHA-256 |
| --- | --- |
| `inputs/urule-core-pro-4.3.0.jar` | `0BFF868EA54F4E6EFC949182D968217694A3B0EE2A29B16DAE790E3F235150FA` |
| `inputs/urule-console-pro-4.3.0.jar` | `CBFAF5AE045E96A27F145A3C0DC6C630AA1DA86CFAD6E77B2C120909E99F4DB7` |

## 4. 完成的修复

### 4.1 类名

9 个反编译器拆出的数字匿名类已改为职责明确的类型：

| 原名称 | 恢复名称 |
| --- | --- |
| `Utils$1` | `VariableCategoryListTypeReference` |
| `BuiltInActionLibraryBuilder$1` | `SpringBeanNameComparator` |
| `BuiltInActionLibraryBuilder$2` | `ActionMethodNameComparator` |
| `ListAction$1` | `PropertyComparator` |
| `DecisionTableParser$1` | `DecisionTableColumnComparator` |
| `DecisionTableParser$2` | `DecisionTableRowComparator` |
| `ComplexScorecardParser$1` | `ComplexScorecardColumnComparator` |
| `ComplexScorecardParser$2` | `ComplexScorecardRowComparator` |
| `ReteBuilder$1` | `RuleSalienceComparator` |

无引用的 `Datatype$1` 和 `NodeType$1` 编译器 switch-map 合成类已删除。当前数字匿名类引用和数字匿名类文件数均为 0。

### 4.2 方法名

除公开/受保护 API 和原有单字母方法外，本轮继续逐个恢复了至少 241 个机械或泛化的私有方法组，并同步修改全部调用点。主要类别包括：

- 32 个 `process<Class>Data` 兜底名。
- 44 个 `handleRequestData` 兜底名。
- 12 个 `manageCacheData` 兜底名。
- 30 个 `buildContent` 兜底名。
- 28 个 `buildText`/`buildText2`/`buildText3`/`buildText4` 泛化名。
- 40 个私有 `executeQuery` 重载，拆分为 `buildWhereClause`、`buildSelectSql`、`query...`、`read...` 和 `map...`。
- 13 个私有 `importData` 和 11 个私有 `exportData` 重载，按项目、知识包、批处理、字段、版本和编码 XML 子节点拆名。
- 31 个 `collectItems`、`processString`、`processStringBuilder`、`resolveElement`、`importElement`、`processLong`、`processLog` 等按参数类型生成的名称。

代表性结果包括：

- JDBC 分页：`buildDb2PageSql`、`buildOraclePageSql`、`buildMySqlPageSql`、`buildHsqlPageSql`、`buildPostgreSqlPageSql`。
- 查询映射：`queryDataSources`、`mapDataSource`、`queryBatches`、`mapBatch`、`readDeploymentsWithContent`。
- 导入导出：`buildProjectDocument`、`appendRuleFiles`、`appendPacketFiles`、`importPacket`、`importVersionFile`、`readEncodedChild`。
- 运行时：`registerTemplateResources`、`resolveCglibTarget`、`resolveJdkProxyTarget`、`buildIoData`、`appendLog`。
- 控制台：`parseHeaderColumns`、`buildOutputSheet`、`resolveVariableCategories`、`writeJarFile`、`storePermission`。

当前单字母方法、数字后缀机械方法、已知机械兜底方法和泛化私有方法验收结果均为 0。

### 4.3 字段名

字段依据用途和容器键值语义恢复，不再使用 `items`、`valuesByKey`、`threadContext`、`cache`、`S2`、`S_S2` 等中性或反编译格式名。代表性修复包括：

- 会话与缓存：`sessionObjectsById`、`attributes`、`knowledgePackagesById`、`clipboardsBySessionId`、`lastAccessTime`。
- 线程上下文：`COPY_LIBRARY_PHASE`、`PARSE_PHASE`、`WORKING_MEMORY_STACK`、`CURRENT_REQUEST`、`CURRENT_LOCALE`、`CURRENT_RULE_FILE`。
- 规则运行时：`activityStatesById`、`assertorsByOperator`、`percentUnitsByDecisionNode`、`monitorDataByPackageId`。
- 构建解析：`junctionsByParseTree`、`variableLibraries`、`importedLibrariesById`、`deserializersByMathType`。
- 配置与存储：`parameterIndexes`、`deserializers`、`templatesByType`、`SUPPORTED_TYPE_NAMES`、`CREATE_TABLE_SQL`。

日志类中由反编译常量传播留下的未使用 `S_S`、`S2`、`S3`、`MESSAGE_TEMPLATE2` 等字段已删除。当前单字母字段、非语义数字后缀字段、机械兜底字段和格式串占位字段均为 0。

DTO 中的 `value`、`data`、`result` 等字段只有在它们确实是对象模型/API 的业务属性时才保留；这类名称有对应 getter/setter 或序列化契约，不是反编译占位符。

### 4.4 参数、局部变量和注释

- 原 59,254 次 `varN` 标识符出现已全部消除。
- 公开/受保护 API 参数与 Javadoc 按 4.3.0 文档恢复。
- 私有参数和局部变量按类型、初始化器、返回值、循环、异常、类型转换和 `Map` 键恢复。
- 删除 331 条“从接口复制的说明”“从类复制的说明”等无信息占位注释。
- 为恢复出的比较器、缓存边界、解析流程和兼容行为补充关键语义说明。
- 保留真正解释约束、协议和业务原因的注释，不用逐行复述代码的注释制造噪声。

### 4.5 异常处理与行为修复

- 52 处直接无参 `printStackTrace()` 已改为结构化日志，当前为 0。
- 将中断异常路径补回 `Thread.currentThread().interrupt()`，避免吞掉线程中断状态。
- `ObjectData` 在读取、写入和删除时刷新 `lastAccessTime`，避免活跃剪贴板会话按创建时间误过期。
- `ScriptRunner` 恢复反编译丢失的 JDBC `commit()`/`rollback()` 行为。
- `HiveWriter` 删除不可达且类型转换错误的数字分支，按真实调用约束统一格式化 `Date`/`Timestamp`。
- 删除调试入口和无意义标准输出；保留的 `printStackTrace(PrintStream)` 仅用于把堆栈转换为响应字符串，保留的标准输出仅用于产品启动、授权和部署状态提示。

## 5. 机器验收结果

验收脚本：`tools/readability-restorer/verify-source-readability.ps1`

| 检查项 | 当前数量 |
| --- | ---: |
| `varN` 占位标识符 | 0 |
| 单字母方法 | 0 |
| 单字母字段 | 0 |
| 单字母类型 | 0 |
| 非语义数字后缀字段 | 0 |
| 已知机械方法兜底名 | 0 |
| 已知机械字段兜底名 | 0 |
| 泛化私有方法名 | 0 |
| 格式串占位字段名 | 0 |
| 数字匿名类引用 | 0 |
| 数字匿名类文件 | 0 |
| 直接无参 `printStackTrace()` | 0 |
| 反编译占位注释 | 0 |

所有检查均输出 `[PASS]`，因此本报告定义范围内的修复率为 `13/13 = 100%`。

## 6. 构建与测试

| 验证项 | 结果 |
| --- | --- |
| `urule-core-pro` Maven 编译 | 通过 |
| `urule-core-pro` 单元测试 | 2 项，0 失败，0 错误 |
| `urule-console-pro` Maven 编译 | 通过 |
| `urule-console-pro` 单元测试 | 3 项，0 失败，0 错误 |
| 可读性恢复器 Maven 测试 | 通过 |
| 前端工具测试 | 通过 |
| Webpack 生产构建 | 通过，hash `3b12efa14440b09ecb97`，15 个 bundle |
| 目标源码差异空白检查 | 通过；仅有工作区 LF/CRLF 转换提示，无补丁空白错误 |

Webpack 对超过 500 KB 的内嵌 `jquery.handsontable.full.js` 给出 Babel 样式降级提示，但构建成功；这是既有第三方大文件提示，不是本轮修复失败。

## 7. 可复现命令

在 `project` 目录执行：

```powershell
powershell -ExecutionPolicy Bypass -File tools/readability-restorer/verify-source-readability.ps1
mvn -q -f urule-core-pro/pom.xml test
mvn -q -f urule-console-pro/pom.xml test
mvn -q -f tools/readability-restorer/pom.xml test
npm test --prefix urule-console-js
npm run build --prefix urule-console-js
```

## 8. 交付物

- `urule-core-pro/src/main/java`：核心模块恢复源码。
- `urule-console-pro/src/main/java`：控制台模块恢复源码。
- `urule-console-js`：前端可维护性修复、测试和可构建源码。
- `tools/readability-restorer`：AST 恢复器、回归测试和 100% 验收脚本。
- 本报告：范围、证据、修复分类、边界和复验命令。

## 9. 结论边界

本次“100%”是明确、可重复执行、能阻止回归的工程验收结论：所有已定义的反编译占位符和混淆式命名模式在可编译主源码中为 0，并且所有模块构建与现有测试通过。

它不声称从缺失的调试符号中数学上还原了每一个厂商原始私有标识符。若以后获得官方未混淆源码或映射表，可以再做逐符号原名对照；当前名称已经按实际职责恢复到可读、可理解、可维护并可编译测试的状态。
