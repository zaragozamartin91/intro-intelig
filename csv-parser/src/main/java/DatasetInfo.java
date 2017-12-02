import java.util.Map;

public class DatasetInfo {
    Map<String, Counter> occurrences;
    int colIdx;
    Counter recordCounter;

    public DatasetInfo(Map<String, Counter> occurrences, int colIdx, Counter recordCounter) {
        this.occurrences = occurrences;
        this.colIdx = colIdx;
        this.recordCounter = recordCounter;
    }
}
