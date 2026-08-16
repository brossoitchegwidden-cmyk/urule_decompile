# urule-decompile workspace

Decompiled and restructured urule-pro 4.3.0 commercial jars into real Maven + frontend projects.
Reference: I:/learn_code/urule (open-source multi-module) and I:/learn_code/urule-console-js (frontend).

## Top-level dirs
- inputs/             : original jars (urule-console-pro-4.3.0.jar, urule-core-pro-4.3.0.jar)
- artifacts/          : rebuilt urule-console-pro-4.3.0-recompiled.jar
- src/                : early console decompiled source (com/bstek/urule, 538 files; superseded by project/)
- vineflower_src/     : Vineflower decompiled source (for diff)
- frontend/           : raw frontend assets extracted from jar (asserts/urule)
- libs/               : maven dependency jars (~670)
- gen/                : launch scripts (start8081.bat / start8082-test.bat)
- tools/              : decompilers (cfr.jar, procyon.jar) + maven settings
- scripts/            : automation scripts (build/compile/verify/analyze/beautify);  auto-resolves root
- project/            : === restructured real Maven + frontend projects ===

## project/ multi-module Maven (aligned with open-source urule parent-child)
urule-parent-pro  (aggregator pom; modules: urule-core-pro, urule-console-pro)
  +- urule-core-pro     : com.bstek.urule:urule-core-pro:4.3.0
  |    src/main/java     : 769 decompiled java files (cfr on inputs/urule-core-pro jar)
  |    src/main/resources: 3 non-class resources
  |    pom.xml           : deps antlr4 / spring-web / jackson ...
  +- urule-console-pro  : com.bstek.urule:urule-console-pro:4.3.0
       src/main/java     : 538 decompiled java (com/bstek/urule)
       src/main/resources: 133 items (asserts, xml, sql, META-INF/services, context)
       pom.xml           : depends on urule-core-pro

Build from project/urule-parent-pro with mvn (reactor: core -> console).

## project/urule-console-js frontend (aligned with urule-console-js)
- src/               : named file-tree skeleton mirrored from reference (48 dirs, 231 files)
- src/_decompiled/   : webpack module material from jar (50 bundles, 1585 module_*.js)
- assets/urule/      : raw frontend assets from jar
- scripts/debundle.js: splits webpack self-executing bundles into numeric modules
- package.json / webpack.config.js: npm/webpack config aligned with reference

### Why frontend is not yet readable source
Pro jar *.bundle.js are webpack-compressed: modules identified by numeric id,
no source-path comments, no sourcemap, so numeric modules cannot auto-map to named files.
Reference open-source urule-console-js src/ is the correct target; this project mirrored
that skeleton into src/ for manual backfill from _decompiled modules via webpack entry map.

## Common commands
cd project/urule-parent-pro && mvn -o compile     # build multi-module
powershell -File scripts/build.ps1                # package console jar
powershell -File scripts/verify_final.ps1         # verify coverage / artifact structure
cd project/urule-console-js && npm install && npm run build   # frontend build"# urule_decompile" 
