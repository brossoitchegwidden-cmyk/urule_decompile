package com.bstek.urule.runtime.log;

import java.io.IOException;
import java.util.List;

public interface LogWriter {
   void write(List<Log> logs) throws IOException;
}
