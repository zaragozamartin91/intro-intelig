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
    static final int[] HEADER_INDEXES = Config.HEADER_INDEXES;
    static final int ADDR_HEADER_INDEX = Config.ADDR_HEADER_INDEX;
    static final int DESC_HEADER_INDEX = Config.DESC_HEADER_INDEX;
    static final int TIME_HEADER_INDEX = Config.TIME_HEADER_INDEX;
    static final int LAST_HEADER_INDEX = Config.LAST_HEADER_INDEX;
    static final SimpleDateFormat DATE_FORMAT = Config.DATE_FORMAT;


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
        try {
            Files.delete(outFile.toPath());
        } catch (IOException e) { }


        InputStream filestream = new FileInputStream(inFileName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(filestream));
        PrintWriter writer = new PrintWriter(new FileOutputStream(outFile));

        try {
            CSVParser parser = CSVParser.parse(csvReader, CSVFormat.EXCEL);
            for (CSVRecord record : parser) {
                if (record.getRecordNumber() > recordsToKeep) break;
                else if (record.getRecordNumber() == 1) writeHeader(record, writer);
                else writeRecord(record, writer);
                System.out.println(record);
            }

            writer.flush();
        } finally {
            if (writer != null) writer.close();
            if (csvReader != null) csvReader.close();
        }
    }

    private void writeRecord(CSVRecord record, PrintWriter writer) throws ParseException {
        StringBuilder recordLine = new StringBuilder();

        for (int i = 0; i < HEADER_INDEXES.length; i++) {
            int headerIndex = HEADER_INDEXES[i];

            String value = removeCommas(record.get(headerIndex)).trim();
            value = value.isEmpty() ? "__EMPTY__" : value;
            if (headerIndex == DESC_HEADER_INDEX) value = standarizeDescription(value);
            else if (headerIndex == TIME_HEADER_INDEX) value = standarizeTime(value);

            if (i == LAST_HEADER_INDEX) recordLine.append(value);
            else recordLine.append(value + ",");
        }

        writer.println(recordLine.toString());
    }

    private String standarizeTime(String value) throws ParseException {
        if (value.startsWith("0")) value = "0" + value;
        Date date = DATE_FORMAT.parse(value);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

//        return hour + ":00:00";
        return "" + hour;
    }

    private String removeCommas(String value) {
        return value.replaceAll(Pattern.quote(","), "");
    }

    private void writeHeader(CSVRecord record, PrintWriter writer) {
        StringBuilder headerLine = new StringBuilder(removeSpaces(record.get(HEADER_INDEXES[0])));
        for (int i = 1; i < HEADER_INDEXES.length; i++) {
            headerLine.append("," + removeSpaces(record.get(HEADER_INDEXES[i])));
        }
        writer.println(headerLine.toString());
    }

    private String removeSpaces(String s) {
        return s.replaceAll(" +", "");
    }

    private String standarizeDescription(String description) {
        if (description.contains("ELUDE")) return "ELUDE POLICE";
        if (description.contains("SIGNAL BY POLICE")) return "ELUDE POLICE";


        if (description.contains("CONTEST") && description.contains("SPEED")) return "SPEEDING";
        if (description.contains("SPEED")) return "SPEEDING";


        if (description.contains("PLATE")) return "PLATE VIOLATION";


        final String LIC_MSG = "LICENSE OR REGISTRATION RELATED";
        if ((description.contains("WITHOUT") || description.contains("W/O")) && description.contains("LICENSE")) return LIC_MSG;
        if (description.contains("SUSPENDED") && (description.contains("LICENSE") || description.contains("LIC."))) return LIC_MSG;
        if (description.contains("SUSPENDED") && (description.contains("REGISTRATION") || description.contains("REG"))) return LIC_MSG;
        if (description.contains("REVOKED") && (description.contains("LICENSE") || description.contains("LIC."))) return LIC_MSG;
        if (description.contains("EXPIRED") && (description.contains("LICENSE") || description.contains("LIC."))) return LIC_MSG;
        if (description.contains("LIC.") && description.contains("RESTRICTION")) return LIC_MSG;
        if (description.contains("FICTITIOUS")) return LIC_MSG;
        if (description.contains("LICENSE")) return LIC_MSG;
        if (description.contains("DISPLAY REGISTRATION")) return LIC_MSG;
        if (description.contains("REGISTRATION CARD")) return LIC_MSG;
        if (description.contains("CURRENT REG. CARD")) return LIC_MSG;
        if (description.contains("UNREGISTERED")) return LIC_MSG;
        if (description.contains("TO HAVE LIC.WITH HIM")) return LIC_MSG;
        if (description.contains("UNPAID REGISTRATION")) return LIC_MSG;
        if (description.contains("W/O CURRENT REGISTRATION")) return LIC_MSG;
        if (description.contains("CURRENT REGISTRATION & VALIDATION")) return LIC_MSG;
        if (description.contains("suspended")) return LIC_MSG;
        if (description.contains("display")) return LIC_MSG;
        if (description.contains("SUSPENDED LIC AND PRIVILEGE")) return LIC_MSG;
        if (description.contains("FALSIFIED VEH. DOCUMENT")) return LIC_MSG;
        if (description.contains("FAIL TO DISPLAY REG. CARD")) return LIC_MSG;
        if (description.contains("FRAUD IN USE OF MVA ID CARD")) return LIC_MSG;
        if (description.contains("DRIVING W/O CURRENT TAGS")) return LIC_MSG;
        if (description.contains("REQUIRED ACT PERTAINING TO DRIVER'S LIC")) return LIC_MSG;


        if (description.contains("ALCOHOL")) return "IMPAIRED BY DRUGS OR ALCOHOL";
        if (description.contains("IMPAIRED")) return "IMPAIRED BY DRUGS OR ALCOHOL";


        if (description.contains("UNINSURED")) return "DRIVING UNINSURED VEHICLE";
        if (description.contains("DRIVING NINSURED VEHICLE")) return "DRIVING UNINSURED VEHICLE";


        final String VEHICLE_EQUIPMENT_VIOLATION = "VEHICLE OR EQUIPMENT VIOLATION";
        if (description.contains("EQUIP")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("WINDOW TINT")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("FRONT LAMP")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("REAR LAMP")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("DAZZLING")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("DISPLAY LIGHTED LAMPS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("OBSTRUCTED WINDSHIELD")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("W/O TIRES")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("TAG LIGHTS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("TAILLIGHTS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("HEADLIGHTS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("VISIBILITY")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("VISIBLE RED")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("VEHICLE WITHOUT LIGHTED HEAD")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("W/O ADEQUATE TAIL LAMPS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("FAILURE TO AVOID PROJECTING GLARING LIGHT")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("LAMP IMPROPERLY DISPLAYING WHITE LIGHT")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("INADEQUATE STEERING AXLES TIRE")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("WINDSHIELD VIEW OBSTRUCTED")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("USE REFLECTORS ON MOTOR VEH. CHANGING ORIGINAL PERFORMANCE")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("FAILURE TO DISPLAY & REFLECT AMBER COLOR")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("LAMPS OBSCURED BY OTHERWISE")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("REMOVABLE WINDSHIELD PLACARD IS HANGING FROM")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("ALTERED & DANGEROUS BUMPER")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("WITHOUT REQUIRED SIGNAL LAMPS")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("DRIVING CAUSING TO BE DRIVEN UNSAFE VEH")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("DRIVING UNSAFE VEH")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("WITH VISIBLE BLUE DEVICE")) return VEHICLE_EQUIPMENT_VIOLATION;
        if (description.contains("WITH VISIBLE BLUE LAMP")) return VEHICLE_EQUIPMENT_VIOLATION;


        final String RECKLESS_DRIVING = "RECKLESS DRIVING";
        if (description.contains("RECKLESS")) return RECKLESS_DRIVING;
        if (description.contains("NEGLIGENT")) return RECKLESS_DRIVING;
        if (description.contains("LANE")) return RECKLESS_DRIVING;
        if (description.contains("CLOSER THAN REASONABLE")) return RECKLESS_DRIVING;
        if (description.contains("DRIVE ACROSS PRIVATE")) return RECKLESS_DRIVING;
        if (description.contains("REDUCE LIGHT DISTRIBUTION WHEN WITHIN")) return RECKLESS_DRIVING;
        if (description.contains("AGGRESSIVE DRIVING")) return RECKLESS_DRIVING;
        if (description.contains("FAILURE") && description.contains("DRIVE") && description.contains("RIGHT")) return RECKLESS_DRIVING;


        if (description.startsWith("PEDESTRIAN")) return "PEDESTRIAN INFRACTION";


        if (description.contains("ACCIDENT")) return "ACCIDENT RELATED";
        if (description.contains("ACC.")) return "ACCIDENT RELATED";
        if (description.contains("UNATTENDED DAMAGED")) return "ACCIDENT RELATED";
        if (description.contains("PROP. DAMAGE")) return "ACCIDENT RELATED";
        if (description.contains("FAILURE TO EXERCISE DUE CARE TO AVOID PEDESTRIAN COLLISION")) return "ACCIDENT RELATED";


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
        if (description.contains("PROVISIONAL DRIVER UNDER 18 DRIVING")) return MINOR_INFRACTION;
        if (description.contains("HORN ON HWY. WHEN NOT REASONABLY NECESSARY")) return MINOR_INFRACTION;
        if (description.contains("STANDING VEH. TO OBSTRUCT FREE VEH.")) return MINOR_INFRACTION;
        if (description.contains("VEH. FOR GENERAL DAILY TRANSPORTATION")) return MINOR_INFRACTION;
        if (description.contains("IMPROPER USE OF VEH. FOG LAMP")) return MINOR_INFRACTION;
        if (description.contains("STANDING VEH. IN FRONT OF PUBLIC DRIVEWAY")) return MINOR_INFRACTION;
        if (description.contains("NOT RESTRAINED BY CHILD SAFETY SEAT")) return MINOR_INFRACTION;
        if (description.contains("STANDING VEH. ON CROSSWALK")) return MINOR_INFRACTION;
        if (description.contains("SEATBELT") || description.contains("SEAT BELT")) return MINOR_INFRACTION;
        if (description.contains("CHILD")) {
            if (description.contains("SECURE") || description.contains("TRANSPORT")) {
                return MINOR_INFRACTION;
            }
        }


        if (description.contains("PASSING")) return RECKLESS_DRIVING;


        if (description.contains("HOLDER OF LEARNER")) return "LEARNING DRIVER WITHOUT SUPERVISION";


        if (description.contains("EARPLUGS")) return "IMPAIRED HEARING";
        if (description.contains("EAR PLUGS")) return "IMPAIRED HEARING";
        if (description.contains("EARPHONES")) return "IMPAIRED HEARING";
        if (description.contains("HEADSET")) return "IMPAIRED HEARING";
        if (description.contains("HEADPHONES")) return "IMPAIRED HEARING";


        if (description.contains("SPINNING WHEELS")) return "NOISE RELATED";
        if (description.contains("SOUND")) return "NOISE RELATED";
        if (description.contains("NOISE")) return "NOISE RELATED";


        if (description.contains("ADDRESS CHANGE")) return "PAPERWORK RELATED";
        if (description.contains("VALID MEDICAL")) return "PAPERWORK RELATED";
        if (description.contains("DISPLAYDECAL ON VEHICLE")) return "PAPERWORK RELATED";
        if (description.contains("PRESENT EVIDENCE OF REQUIRED SECURITY")) return "PAPERWORK RELATED";
        if (description.contains("VIOLATION OF RENTAL AGREEMENT")) return "PAPERWORK RELATED";
        if (description.contains("FAILURE TO MAINTAIN REQUIRED SECURITY")) return "PAPERWORK RELATED";
        if (description.contains("W/O MOTOR CARRIER AUTHORITY")) return "PAPERWORK RELATED";


        if (description.contains("UNAUTHORIZED PERSON")) return "UNAUTHORIZED PERSON DRIVING";


        if (description.contains("WHEN NOT ABLE TO COMPLY WITH ENGLISH SPEAKING REQUIREMENTS")) return "OTHER";
        if (description.contains("SOLICIT DONATIONS W/O PERMIT")) return "OTHER";
        if (description.contains("THROWING ANY REFUSEON")) return "OTHER";
        if (description.contains("FRONT LEFT CLEARANCE; 1 OF 3 FRONT ID")) return "OTHER";
        if (description.contains("SLOW") && description.contains("SPEED")) return "OTHER";


        return description;
    }
}
