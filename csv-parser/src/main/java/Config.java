import java.text.SimpleDateFormat;

public class Config {
    static final int[] HEADER_INDEXES = {1, 3, 4, 19, 24, 28, 29, 33};
    static final int ADDR_HEADER_INDEX = 3;
    static final int DESC_HEADER_INDEX = 4;
    static final int TIME_HEADER_INDEX = 1;
    static final int LAST_HEADER_INDEX = HEADER_INDEXES.length - 1;
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

}
