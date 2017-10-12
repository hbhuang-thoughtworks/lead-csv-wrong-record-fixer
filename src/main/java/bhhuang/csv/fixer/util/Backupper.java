package bhhuang.csv.fixer.util;

import org.apache.log4j.Logger;

public class Backupper {
    static Logger logger = Logger.getLogger(Backupper.class);

    public static String buildBackupSQL(String leadId, String validTimeFrom) {
        return String.format("insert into lead_wrong_comment_rid (rid) select rid from otr_lead_lead where id = '%s' and valid_time_from = '%s';", leadId, validTimeFrom);
    }

    public static void printBackupSQL(String sql) {
        logger.warn(sql);
    }
}
