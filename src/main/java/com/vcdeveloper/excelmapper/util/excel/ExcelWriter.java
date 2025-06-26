package com.vcdeveloper.excelmapper.util.excel;

import java.io.OutputStream;
import java.util.List;

public interface ExcelWriter {

    <T> void write(List<T> dataList, OutputStream out) throws Exception;
}
