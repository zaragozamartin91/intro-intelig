import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.*;

/**
 * Filtro de registros con bajas ocurrencias dado una columna
 */
public class RecordOccurrenceFilter {
    private String fileName;
    private String colName;
    private int minRecordCount;
    private String outFileName;

    public RecordOccurrenceFilter(String fileName, String colName, int minRecordCount, String outFileName) {
        this.fileName = fileName;
        this.colName = colName;
        this.minRecordCount = minRecordCount;
        this.outFileName = outFileName;
    }


    public void filter() throws IOException, ParseException {
        try {
            Files.delete(new File(outFileName).toPath());
            System.out.println(outFileName + " eliminado");
        } catch (IOException e) {}

        final DatasetInfo datasetInfo = Utils.parseDataset(fileName, colName);
        final Map<String, Counter> occurrences = datasetInfo.occurrences;
        final int colIdx = datasetInfo.colIdx;
        final int recordCount = datasetInfo.recordCounter.count;

        PrintWriter writer = new PrintWriter(new FileOutputStream(outFileName));
        InputStream filestream = new FileInputStream(fileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));
        CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);

        Set<String> remainingCategories = new HashSet<>();

        try {
            final Iterator<CSVRecord> recordIterator = parser.iterator();
            final CSVRecord header = recordIterator.next();

            Utils.writeRecord(header, writer);

            recordIterator.forEachRemaining(record -> {
                final String value = record.get(colIdx);
                int count = occurrences.get(value).count;

                if (count >= minRecordCount) try {
                    Utils.writeRecord(record, writer);
                    remainingCategories.add(value);
                } catch (ParseException e) {
                    throw new RuntimeException("Error al escribir los registros");
                }
            });

            System.out.println();
            int delCatCount = occurrences.size() - remainingCategories.size();
            System.out.println("# Categorias eliminadas: " + delCatCount);
            System.out.println("Categorias pendientes:");
            remainingCategories.forEach(cat -> System.out.println(cat));

        } finally {
            writer.close();
            csvReader.close();
        }
    }
}
