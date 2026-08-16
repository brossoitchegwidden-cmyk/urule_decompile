# URule Console JS (frontend, decompiled)

Restores frontend from urule-console-pro-4.3.0.jar.

## Layout
- src/ : named file-tree skeleton mirrored from open-source reference
  I:/learn_code/urule/urule-console-js/src (48 dirs, 231 files).
  This is the expected readable result: named dirs + meaningful jsx components.
- src/_decompiled/ : webpack module material from the jar (50 bundles, 1585 modules).
- assets/urule/ : raw frontend assets pulled from the jar (js/html/echarts).
- scripts/debundle.js : splits webpack self-executing bundles into modules.
- package.json / webpack.config.js : npm/webpack config aligned with reference.

## Why module_*.js is not human-readable
Pro jar bundles are webpack-compressed: (()=>{var e,n={5577:function(e,n,t){...}}})
- modules identified by numeric id, NO source-path comments (.jsx / ./src/).
- no sourcemap (.map files).
So numeric modules cannot be auto-mapped back to named files.

## Correct target (reference)
Open-source reference I:/learn_code/urule/urule-console-js is同源 with Pro build;
its src/ is the expected result: named dirs + jsx components.
This project mirrored that skeleton into src/ as the restore target.

## Next-step restore
1) Use webpack.config.js entry to map each bundle to its entry file.
2) Diff src/_decompiled/<bundle>/module_*.js vs reference named files, backfill.
3) If sourcemap build is available, use source-map to restore named source.