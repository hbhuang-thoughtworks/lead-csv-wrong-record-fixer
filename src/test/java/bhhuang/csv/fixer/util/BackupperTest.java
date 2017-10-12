package bhhuang.csv.fixer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BackupperTest {

    @Test
    public void should_generate_backup_sql() throws Exception {
        String leadId = "xxx";
        String validTimeFrom = "2017-09-05 18:40:00.000";
        String sql = Backupper.buildBackupSQL(leadId, validTimeFrom);

        assertEquals("insert into lead_wrong_comment_rid (rid) select rid from otr_lead_lead where id = 'xxx' and valid_time_from = '2017-09-05 18:40:00.000';", sql);
    }
}