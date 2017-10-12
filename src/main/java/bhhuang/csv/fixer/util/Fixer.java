package bhhuang.csv.fixer.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

public class Fixer {
    private int counter = 0;

    private static final int correctFieldIndex = 6;

    public static final String[] fields = new String[]{
        "personId",
        "_id",
        "bdcName",
        "budget",
        "campainId",
        "channel",
        "comments",
        "competitorBrand",
        "competitorClass",
        "competitorModel",
        "createdBy",
        "createdTime",
        "currentChannel",
        "currentSalesConsultantId",
        "grade",
        "lastUpdatedBy",
        "leadId",
        "leadSource",
        "leadType",
        "lostSalesReason",
        "lostSalesTime",
        "ownerSalesConsultantId",
        "referralLeadSfdcId",
        "role",
        "sfdcId",
        "sfdcOwner",
        "showroomId",
        "status",
        "systemOwner",
        "updatedTime",
        "vehicleClass_descriptionEN",
        "vehicleClass_descriptionZH",
        "brand_descriptionEN",
        "brand_descriptionZH",
        "variant_descriptionEN",
        "variant_descriptionZH"
    };

    public static final String[] sqlFields = new String[]{
        "comments", "competitor_brand",
        "competitor_class", "competitor_model",
        "created_by", "created_time",
        "current_channel",
        "grade", "last_updated_by",
        "lead_source", "lead_type",
        "lost_sales_reason", "lost_sales_time",
        "owner_sales_consultant_id", "referral_lead_sfdc_id",
        "role", "sfdc_id",
        "sfdc_owner", "showroom_id",
        "status", "system_owner",
        "updated_time", "first_interested_vehicles_brand_description_en",
        "first_interested_vehicles_brand_description_zh", "first_interested_vehicles_vehicle_class_description_en",
        "first_interested_vehicles_vehicle_class_description_zh", "first_interested_vehicles_variant_description_en",
        "first_interested_vehicles_variant_description_zh=%s "
    };

    static Logger logger = Logger.getLogger(Fixer.class);

    public static void main(String[] args) {
        Fixer fixer = new Fixer();
        long start = System.currentTimeMillis();
        fixer.start();

        logger.debug(String.format("use time %d", System.currentTimeMillis()-start));
    }

    private void start() {
        List<Pair<String, String>> fileNamesAndValidTimeFrom = Lists.newArrayList(
            Pair.of("1-otr_lead_lead.csv", "2017-09-05 18:40:00.000")
            ,Pair.of("2-export-all-non-fake-leads-by-dealer-ids.csv", "2017-09-05 18:40:00.000")
            ,Pair.of("3-otr_lead_lead.csv", "2017-09-09 10:00:00.000")
//            Pair.of("4-UAT-otr_lead_lead.csv", "2017-07-11 19:00:00.000")
//            Pair.of("5-single-line.csv", "2017-07-11 19:00:00.000")
        );

        fileNamesAndValidTimeFrom.forEach(pair -> printSqlForCsvFile(pair.getLeft(), pair.getRight()));

        logger.debug("SQL count: " + counter);
    }

    private void printSqlForCsvFile(String fileName, String validTimeFrom) {
        List<String> lines = readLines(fileName);
        List<String> sqls = lines.stream()
            .skip(1)
            .filter(line -> !CheckUtil.isFieldCountMatched(line, fields.length))
            .map(line -> replaceCommaInLostSaleReason(line))
            .map(line -> buildCorrectFields(line))
//            .peek(fields -> printBackupSQL(fields, validTimeFrom))
            .map(fields -> buildSql(fields, validTimeFrom))
            .collect(Collectors.toList());
        counter += sqls.size();
        printSQL(sqls);
    }

    private String replaceCommaInLostSaleReason(String line) {
        return StringUtils.replace(line, "Bought not MB, Audi or BMW", "Bought not MB Audi or BMW");
    }

    private void printBackupSQL(String[] fields, String validTimeFrom) {
        String sql = Backupper.buildBackupSQL(fields[1], validTimeFrom);
        Backupper.printBackupSQL(sql);
    }

    private void printSQL(List<String> sqls) {
        sqls.stream().forEach(sql -> logger.warn(sql));
    }

    public static String[] buildCorrectFields(String line) {
        logger.info(String.format("found not matched records: %s", line));
        String[] leftFields = CheckUtil.getLeftFields(line, correctFieldIndex);
        String[] rightFields = CheckUtil.getRightFields(line, correctFieldIndex, fields.length);
        String commentFieldValue = CheckUtil.getCorrectFieldValue(line, correctFieldIndex, fields.length);
        String[] middleField = new String[]{commentFieldValue};

        String[] newFields = combineArrays(leftFields, middleField, rightFields);
        processLostSaleReasonWithComma(newFields);
        return newFields;
    }

    private static void processLostSaleReasonWithComma(String[] fields) {
        fields[19] = StringUtils.replace(fields[19], "Bought not MB Audi or BMW", "Bought not MB, Audi or BMW");
    }

    private List<String> readLines(String fileName) {
        try {
            String file = this.getClass().getClassLoader().getResource(fileName).getFile();
            List<String> lines = FileUtils.readLines(new File(file), "utf-8");
            return lines;
        } catch (IOException e) {
            logger.error("error", e);
        }
        return new ArrayList<String>();
    }

    static <T> T[] combineArrays(T[]... arrays) {
        int length = 0;
        for (T[] array : arrays) {
            length += array.length;
        }

        //T[] result = new T[length];
        final T[] result = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);

        int offset = 0;
        for (T[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public static String buildSql(String[] fields, String validTimeFrom) {

        String sqlTpl = "update otr_lead_lead set "
            + "comments=%s, competitor_brand=%s, "
            + "competitor_class=%s, competitor_model=%s, "
            + "created_by=%s, created_time=%s, "
            + "current_channel=%s, "
            + "grade=%s, last_updated_by=%s, "
            + "lead_source=%s, lead_type=%s, "
            + "lost_sales_reason=%s, lost_sales_time=%s, "
            + "owner_sales_consultant_id=%s, referral_lead_sfdc_id=%s, "
            + "role=%s, sfdc_id=%s, "
            + "sfdc_owner=%s, showroom_id=%s, "
            + "status=%s, system_owner=%s, "
            + "updated_time=%s, first_interested_vehicles_brand_description_en=%s, "
            + "first_interested_vehicles_brand_description_zh=%s, first_interested_vehicles_vehicle_class_description_en=%s, "
            + "first_interested_vehicles_vehicle_class_description_zh=%s, first_interested_vehicles_variant_description_en=%s, "
            + "first_interested_vehicles_variant_description_zh=%s "
            + "where id=%s and valid_time_from=%s;";

        String sql = String.format(sqlTpl,
            buildSqlFieldValue(fields[6]), buildSqlFieldValue(fields[7]),
            buildSqlFieldValue(fields[8]), buildSqlFieldValue(fields[9]),
            buildSqlFieldValue(fields[10]), buildSqlFieldValue(fields[11]),
            buildSqlFieldValue(fields[12]),
            buildSqlFieldValue(fields[14]), buildSqlFieldValue(fields[15]),
            buildSqlFieldValue(fields[17]), buildSqlFieldValue(fields[18]),
            buildSqlFieldValue(fields[19]), buildSqlFieldValue(fields[20]),
            buildSqlFieldValue(fields[21]), buildSqlFieldValue(fields[22]),
            buildSqlFieldValue(fields[23]), buildSqlFieldValue(fields[24]),
            buildSqlFieldValue(fields[25]), buildSqlFieldValue(fields[26]),
            buildSqlFieldValue(fields[27]), buildSqlFieldValue(fields[28]),
            buildSqlFieldValue(fields[29]), buildSqlFieldValue(fields[32]),
            buildSqlFieldValue(fields[33]), buildSqlFieldValue(fields[30]),
            buildSqlFieldValue(fields[31]), buildSqlFieldValue(fields[34]),
            buildSqlFieldValue(fields[35]),
            "'" + fields[1] + "'",
            "'" + validTimeFrom + "'"
        );
        return sql;
    }

    public static String buildSqlFieldValue(String rawValue) {
        if (StringUtils.isBlank(rawValue)) {
            return "null";
        } else {
            if (rawValue.contains("'")) {
                logger.debug("contains single quota: " + rawValue);
            }
            String transcodedValue = rawValue.replaceAll("'", "''");
            return String.format("N'%s'", transcodedValue);
        }
    }
}
