package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.Page;
import java.util.List;

public interface VersionFileQuery {
   VersionFileQuery id(long id);

   VersionFileQuery fileId(long fileId);

   VersionFileQuery projectId(long projectId);

   VersionFileQuery version(String version);

   VersionFileQuery versionLike(String version);

   VersionFileQuery noteLike(String note);

   List list();

   Page paging(int pageIndex, int pageSize);
}
