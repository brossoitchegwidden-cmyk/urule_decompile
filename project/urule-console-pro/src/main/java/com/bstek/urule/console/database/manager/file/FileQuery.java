package com.bstek.urule.console.database.manager.file;

import java.util.List;

public interface FileQuery {
   FileQuery id(long var1);

   FileQuery ids(List var1);

   FileQuery name(String var1);

   FileQuery nameLike(String var1);

   FileQuery type(String var1);

   FileQuery types(String[] var1);

   FileQuery lockedUser(String var1);

   FileQuery updateUser(String var1);

   FileQuery deleted(boolean var1);

   FileQuery removeEmpty(boolean var1);

   FileQuery containCommonProject(boolean var1);

   FileQuery desc(String var1);

   FileQuery asc(String var1);

   List tree(Long var1);

   List list(Long var1);
}
