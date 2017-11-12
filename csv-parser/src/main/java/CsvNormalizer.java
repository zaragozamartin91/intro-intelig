import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class CsvNormalizer {
    static final int[] HEADER_INDEXES = {0, 1, 3, 4, 5, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33};
    static final int ADDR_HEADER_INDEX = 3;
    static final int DESC_HEADER_INDEX = 4;
    static final int TIME_HEADER_INDEX = 1;
    static final int LAST_HEADER_INDEX = HEADER_INDEXES.length - 1;
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    int recordsToKeep;
    String outFilePath;
    String inFileName;

    public CsvNormalizer(int recordsToKeep, String outFilePath, String inFileName) {
        this.recordsToKeep = recordsToKeep;
        this.outFilePath = outFilePath;
        this.inFileName = inFileName;
    }

    public void run() throws IOException, ParseException {
        File outFile = new File(outFilePath);
        Files.delete(outFile.toPath());

        InputStream filestream = MainApp.class.getResourceAsStream(inFileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));
        PrintWriter writer = new PrintWriter(new FileOutputStream(outFile));

        CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);
        for (CSVRecord record : parser) {
            if (record.getRecordNumber() > recordsToKeep) break;
            else if (record.getRecordNumber() == 1) writeHeader(record, writer);
            else writeRecord(record, writer);
            System.out.println(record);
        }

        writer.flush();
        writer.close();
    }

    private static void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        for (int i = 0; i < HEADER_INDEXES.length; i++) {
            int headerIndex = HEADER_INDEXES[i];

            String value = removeCommas(record.get(headerIndex));
            if (headerIndex == DESC_HEADER_INDEX) value = standarizeDescription(value);
            else if (headerIndex == TIME_HEADER_INDEX) value = standarizeTime(value);

            if (i == LAST_HEADER_INDEX) recordLine.append(value);
            else recordLine.append(value + ",");
        }

        writer.println(recordLine.toString());
    }

    private static String standarizeTime(String value) throws ParseException {
        if (value.startsWith("0")) value = "0" + value;
        Date date = DATE_FORMAT.parse(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour + ":00:00";
    }

    private static String removeCommas(String value) {
        return value.replaceAll(Pattern.quote(","), "");
    }

    private static void writeHeader(CSVRecord record, PrintWriter writer) {
        StringBuilder headerLine = new StringBuilder(removeSpaces(record.get(HEADER_INDEXES[0])));
        for (int i = 1; i < HEADER_INDEXES.length; i++) {
            headerLine.append("," + removeSpaces(record.get(HEADER_INDEXES[i])));
        }
        writer.println(headerLine.toString());
    }

    private static String removeSpaces(String s) {
        return s.replaceAll(" +", "");
    }

    private static String standarizeDescription(String description) {
        if (description.contains("ELUDE")) return "ELUDE POLICE";
        if (description.contains("SIGNAL BY POLICE")) return "ELUDE POLICE";

        if (description.contains("SLOW") && description.contains("SPEED")) return "DRIVING SLOW";
        if (description.contains("CONTEST") && description.contains("SPEED")) return "SPEEDING";
        if (description.contains("SPEED")) return "SPEEDING";

        if (description.contains("PLATE")) return "PLATE VIOLATION";


        final String LIC_MSG = "LICENSE OR REGISTRATION RELATED";
        if ((description.contains("WITHOUT") || description.contains("W/O")) && description.contains("LICENSE")) {
            return LIC_MSG;
        }
        if (description.contains("SUSPENDED") && (description.contains("LICENSE") || description.contains("LIC."))) {
            return LIC_MSG;
        }
        if (description.contains("SUSPENDED") && (description.contains("REGISTRATION") || description.contains("REG"))) {
            return LIC_MSG;
        }
        if (description.contains("REVOKED") && (description.contains("LICENSE") || description.contains("LIC."))) {
            return LIC_MSG;
        }
        if (description.contains("EXPIRED") && (description.contains("LICENSE") || description.contains("LIC."))) {
            return LIC_MSG;
        }
        if (description.contains("LIC.") && description.contains("RESTRICTION")) {
            return LIC_MSG;
        }
        if (description.contains("FICTITIOUS")) return LIC_MSG;
        if (description.contains("LICENSE")) return LIC_MSG;
        if (description.contains("DISPLAY REGISTRATION")) return LIC_MSG;
        if (description.contains("REGISTRATION CARD")) return LIC_MSG;
        if (description.contains("CURRENT REG. CARD")) return LIC_MSG;
        if (description.contains("UNREGISTERED")) return LIC_MSG;
        if (description.contains("TO HAVE LIC.WITH HIM")) return LIC_MSG;


        if (description.contains("ALCOHOL")) return "IMPAIRED BY DRUGS OR ALCOHOL";
        if (description.contains("IMPAIRED")) return "IMPAIRED BY DRUGS OR ALCOHOL";

        if (description.contains("SEATBELT") || description.contains("SEAT BELT")) return "SEATBELT VIOLATION";
        //UNINSURED

        if (description.contains("UNINSURED")) return "DRIVING UNINSURED VEHICLE";


        if (description.contains("EQUIP")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("WINDOW TINT")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("FRONT LAMP")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("REAR LAMP")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("DAZZLING")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("DISPLAY LIGHTED LAMPS")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("OBSTRUCTED WINDSHIELD")) return "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("W/O TIRES")) return "VEHICLE OR EQUIPMENT VIOLATION";
        //


        if (description.contains("RECKLESS")) return "RECKLESS DRIVING";
        if (description.contains("NEGLIGENT")) return "RECKLESS DRIVING";
        if (description.contains("LANE")) return "RECKLESS DRIVING";
        if (description.contains("CLOSER THAN REASONABLE")) return "RECKLESS DRIVING";
        if (description.contains("DRIVE ACROSS PRIVATE")) return "RECKLESS DRIVING";


        if (description.startsWith("PEDESTRIAN")) return "PEDESTRIAN INFRACTION";


        if (description.contains("ACCIDENT")) return "ACCIDENT RELATED";
        if (description.contains("ACC.")) return "ACCIDENT RELATED";
        if (description.contains("UNATTENDED DAMAGED")) return "ACCIDENT RELATED";
        if (description.contains("PROP. DAMAGE")) return "ACCIDENT RELATED";
        //PROP. DAMAGE


        if (description.contains("TEXTELECTRONIC") ||
                description.contains("TEXT MSG") ||
                description.contains("TELEPHONE") ||
                description.contains("ELECTRONIC MSG") ||
                description.contains("HANDHELD PHONE")) return "PHONE RELATED";


        if (description.contains("OBEY")) return "DISOBEY TRAFFIC CONTROL";
        if (description.contains("DISOBEY")) return "DISOBEY TRAFFIC CONTROL";


        final String MINOR_INFRACTION = "MINOR INFRACTION";
        if (description.contains("STOP")) return MINOR_INFRACTION;
        if (description.contains("YIELD")) return MINOR_INFRACTION;
        if (description.contains("TURN")) return MINOR_INFRACTION;
        if (description.contains("PARKING")) return MINOR_INFRACTION;
        if (description.contains("WRONG WAY")) return MINOR_INFRACTION;
        if (description.contains("UNSAFE BACKING")) return MINOR_INFRACTION;
        if (description.contains("TO CARRY CARGO")) return MINOR_INFRACTION;
        if (description.contains("CHILD")) {
            if (description.contains("SECURE") || description.contains("TRANSPORT")) {
                return MINOR_INFRACTION;
            }
        }


        if (description.contains("PASSING")) return "IMPROPER PASSING";


        if (description.contains("HOLDER OF LEARNER")) return "LEARNING DRIVER WITHOUT SUPERVISION";


        if (description.contains("EARPLUGS")) return "IMPAIRED HEARING";
        if (description.contains("EARPHONES")) return "IMPAIRED HEARING";
        if (description.contains("HEADSET")) return "IMPAIRED HEARING";

        return description;
    }
}
