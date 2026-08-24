package com.bstek.urule.console.database.manager.file;

import java.util.List;

public interface FileQuery {
   FileQuery id(long id);

   FileQuery ids(List ids);

   FileQuery name(String name);

   FileQuery nameLike(String name);

   FileQuery type(String type);

   FileQuery types(String[] types);

   FileQuery lockedUser(String lockedUser);

   FileQuery updateUser(String updateUser);

   FileQuery deleted(boolean deleted);

   FileQuery removeEmpty(boolean removeEmpty);

   FileQuery containCommonProject(boolean containCommonProject);

   FileQuery desc(String property);

   FileQuery asc(String property);

   List tree(Long projectId);

   List list(Long projectId);
}
