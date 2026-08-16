package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface VersionFileQuery {
   VersionFileQuery id(long var1);

   VersionFileQuery fileId(long var1);

   VersionFileQuery projectId(long var1);

   VersionFileQuery version(String var1);

   VersionFileQuery versionLike(String var1);

   VersionFileQuery noteLike(String var1);

   List list();

   Page paging(int var1, int var2);
}
