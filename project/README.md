# URule 4.3.0 恢复工程

本目录包含从 URule Professional 4.3.0 恢复出的前端和后端源码。工程目标是保留原始产物作为比对证据，同时提供一套可以阅读、修改和构建的主源码树。

## 模块

| 模块 | 职责 | 维护入口 |
| --- | --- | --- |
| `urule-console-js` | React/Redux 管理控制台 | `src/` |
| `urule-core-pro` | 规则模型、构建与运行时 | `src/main/java/` |
| `urule-console-pro` | 控制台服务、存储与管理接口 | `src/main/java/` |
| `urule-parent-pro` | Maven 聚合与公共编译配置 | `pom.xml` |

## 源码边界

- 前端 `src/` 是可维护的命名源码；`src/_decompiled/` 与 `assets/` 是恢复和比对材料，不应作为日常修改入口。
- 前端 `src/editor/jquery.*` 与 `src/components/splitter/component/jquery-splitter.js` 是内嵌第三方代码，升级应替换上游版本，不应混入业务格式化。
- 后端各模块的 `src/main/java/` 是 Maven 实际编译的源码。`java_cfr_backup`、`java_hybrid`、`java_restored`、`java_vf` 等目录是不同反编译器的恢复材料，不参与正常构建。
- `RuleParserLexer.java`、`RuleParserParser.java` 以及对应 listener/visitor 是 ANTLR 生成代码；应修改语法源并重新生成，不要手工重构生成文件。
- `dist/`、`target/` 和构建日志属于生成物；`assets/` 下的原始 bundle 是恢复证据，不是日常编辑入口。

## 验证命令

前端：

```powershell
cd urule-console-js
npm test
npm run build
```

后端：

```powershell
mvn -f urule-parent-pro/pom.xml test
```

如果历史 `target/` 中的恢复产物被其他进程占用，应先停止占用进程，或在临时副本中验证，避免清理恢复证据。

## 恢复工具与报告

- [源码可读性恢复报告](docs/2026-08-24_逆向恢复-URule源码可读性-report.md) 记录恢复范围、证据、指标、验证结果和剩余限制。
- [Readability Restorer](tools/readability-restorer/README.md) 使用同版本 Javadoc 和 Java AST 恢复有可靠依据的参数、字段、方法及注释，默认只做预览。

## 维护约定

- 新代码使用表达业务含义的名称，不继续沿用反编译生成的 `var0`、`a()` 等名称。
- 网络错误、线程中断和资源关闭统一通过公共方法或日志处理；不要使用 `printStackTrace()` 或 `System.out.println()`。
- XML/HTML/URL 数据在各自边界处编码，禁止直接拼接未转义的外部值。
- 修复反编译代码时优先保持公开 API 和序列化格式不变，并为恢复出的分支、switch 或缓存逻辑补回归测试。

## 已知技术债

- 部分后端类仍保留缺少唯一证据的反编译变量名，另有 ANTLR 生成代码和拆分匿名内部类；建议按高调用频率逐个补证恢复，不宜机械重命名。
- 前端基于 React 16、Redux 3 和 Webpack 4，组件与全局变量耦合较重；当前测试主要覆盖公共纯函数，仍缺浏览器级集成测试。
- 仓库内保留了较多恢复产物。新增的忽略规则只约束后续文件，已经纳入 Git 索引的历史文件需要由维护者单独决定是否取消跟踪。
