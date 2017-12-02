package mz.iint.bank;

import java.text.SimpleDateFormat;

public class Config {
    static final int[] HEADER_INDEXES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    static final int CLASS_INDEX = 16;
    static final int LAST_HEADER_INDEX = HEADER_INDEXES.length - 1;
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
}
