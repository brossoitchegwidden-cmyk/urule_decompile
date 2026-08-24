# Readability Restorer

该工具使用 URule 同版本官方 Javadoc 和 Java AST 恢复反编译丢失的信息。

它只执行有明确依据的改动：

- 按类、方法名、参数数量和参数类型匹配 Javadoc，恢复公开及受保护方法的参数名；
- 根据标准 getter/setter 的直接字段访问，恢复私有字段名；
- 仅在源码没有 Javadoc 且官方文档确有说明时，恢复类或方法说明；
- 跳过 ANTLR 等生成源码、匿名类拆分文件和无法唯一匹配的成员。
- 可通过字段别名文件同步修复子类仍在使用的旧受保护字段名。
- 可选 `--restore-protected-methods` 按同版本 Javadoc 恢复受保护方法名及可静态解析的调用点。
- 若目标签名已经存在（通常是可读入口加短名兼容桥），或继承链存在同名同参数数量的未解析重载，方法及调用恢复会保守跳过。
- 可选 `--restore-locals` 按声明类型、初始化表达式、类型转换、直接返回值、增强/普通 `for`、异常类型及 `Map` 键恢复高置信局部变量和私有方法参数名。
- 可选 `--restore-all-placeholders` 将局部变量恢复扩展到所有仍符合反编译占位符规则的名称。
- 可选 `--restore-private-fields` 恢复剩余私有短字段，并细化旧版本工具留下的 `items`、`valuesByKey`、`threadContext`、`cache` 中性兜底名。
- 可选 `--restore-private-methods` 按重载组恢复私有短方法，并消除 `processInternal*`、`buildTextInternal*` 中性兜底名。
- 可选 `--repair-servlet-json-calls` 修复可静态确认的 Servlet JSON 输出桥调用。
- `--include-generated` 与 `--include-synthetic` 仅用于明确需要处理生成/合成源码的场景；ANTLR visitor 默认仍会跳过。
- 只跳过明确的 ANTLR 生成 visitor；名称以 `Visitor` 结尾的手写业务类仍会参与恢复。

默认仅输出预览统计；传入 `--apply` 才会写入源码。

```powershell
mvn -q -f tools/readability-restorer/pom.xml compile exec:java `
  '-Dexec.args=--source-root urule-core-pro/src/main/java --javadoc-jar C:/path/urule-core-pro-4.3.0-javadoc.jar --field-alias-file tools/readability-restorer/core-field-aliases.properties --restore-protected-methods --restore-locals'
```

传入 `--apply` 应用修改；省略时可用于幂等预览。工具自身的端到端测试覆盖手写 visitor、类型转换、直接返回、循环索引、`Map` 键、生成 visitor 跳过、兼容桥冲突和继承重载规则：

```powershell
mvn -q -f tools/readability-restorer/pom.xml test
```

每次应用后都应运行对应模块测试和字节码结构校验。

主源码的反编译占位符验收可直接运行：

```powershell
powershell -ExecutionPolicy Bypass -File tools/readability-restorer/verify-source-readability.ps1
```

验收范围是两个可编译模块的 `src/main/java`。`java_cfr_backup` 与 `java_hybrid` 是保留的原始/中间反编译取证快照，不参与构建，也不计入恢复率。

脚本会阻止 `varN`、单字母类型/方法/字段、非语义数字字段、数字匿名类、机械方法/字段兜底名、泛化私有方法、格式串占位字段、无参 `printStackTrace()` 和反编译占位注释回归。ANTLR 规则上下文、token 常量及确有业务含义的数字版本名称不属于这些模式。
