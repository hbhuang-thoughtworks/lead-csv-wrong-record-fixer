package bhhuang.csv.fixer.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FixerTest {

    @Test
    public void should_combine_multiple_array_into_one_array() throws Exception {
        String[] array1 = new String[] {"A", "B"};
        String[] array2 = new String[] {"C", ""};
        String[] array3 = new String[] {"", "D", "E"};

        String[] combinedArray = Fixer.combineArrays(array1, array2, array3);

        assertEquals(String.join(",", new String[]{"A", "B", "C", "", "", "D", "E"}), String.join(",", combinedArray));
    }

    @Test
    public void should_generate_sql_for_update() throws Exception {
        String[] fields = Fixer.buildCorrectFields(
            "58fb1d881c1870000e50bd3d,L39259827-e514-4411-b7e9-5a4fc9721502,佼,,,INBOUND,看车比价,,,,sfdc,1504399823000,INBOUND,,E,,,经销商市场推广(媒体广告),户外,其他原因,1504612740723,D8PANKUN,,purchaser,a166F000005ww2AQAQ,,GS0014717,LOST_SALES,SFDC,1504612740723,C-Class,C级,Mercedes-Benz Passenger Cars,梅赛德斯-奔驰乘用车,C 180 L Style Sedan,C 180 L 时尚型轿车");

        String sql = "update otr_lead_lead set "
            + "comments=N%s, competitor_brand=%s, "
            + "competitor_class=%s, competitor_model=%s, "
            + "created_by=N%s, created_time=N%s, "
            + "current_channel=N%s, "
            + "grade=N%s, last_updated_by=%s, "
            + "lead_source=N%s, lead_type=N%s, "
            + "lost_sales_reason=N%s, lost_sales_time=N%s, "
            + "owner_sales_consultant_id=N%s, referral_lead_sfdc_id=%s, "
            + "role=N%s, sfdc_id=N%s, "
            + "sfdc_owner=%s, showroom_id=N%s, "
            + "status=N%s, system_owner=N%s, "
            + "updated_time=N%s, first_interested_vehicles_brand_description_en=N%s, "
            + "first_interested_vehicles_brand_description_zh=N%s, first_interested_vehicles_vehicle_class_description_en=N%s, "
            + "first_interested_vehicles_vehicle_class_description_zh=N%s, first_interested_vehicles_variant_description_en=N%s, "
            + "first_interested_vehicles_variant_description_zh=N%s "
            + "where id=%s and valid_time_from=%s;";

        assertEquals(String.format(sql,
            "'看车比价'", "null",
            "null", "null",
            "'sfdc'", "'1504399823000'",
            "'INBOUND'",
            "'E'", "null",
            "'经销商市场推广(媒体广告)'", "'户外'",
            "'其他原因'", "'1504612740723'",
            "'D8PANKUN'", "null",
            "'purchaser'", "'a166F000005ww2AQAQ'",
            "null", "'GS0014717'",
            "'LOST_SALES'", "'SFDC'",
            "'1504612740723'", "'Mercedes-Benz Passenger Cars'",
            "'梅赛德斯-奔驰乘用车'", "'C-Class'",
            "'C级'", "'C 180 L Style Sedan'",
            "'C 180 L 时尚型轿车'",
            "'L39259827-e514-4411-b7e9-5a4fc9721502'",
            "'2017-09-05 18:40:00.000'"
            ),
            Fixer.buildSql(fields, "2017-09-05 18:40:00.000"));
    }

    @Test
    public void should_generate_correct_sql_for_update_when_lost_sale_reason_contains_comma() throws Exception {
        String[] fields = Fixer.buildCorrectFields(
            "58fb1d881c1870000e50bd3d,L39259827-e514-4411-b7e9-5a4fc9721502,佼,,,INBOUND,看车比价,,,,sfdc,1504399823000,INBOUND,,E,,,经销商市场推广(媒体广告),户外,Bought not MB Audi or BMW,1504612740723,D8PANKUN,,purchaser,a166F000005ww2AQAQ,,GS0014717,LOST_SALES,SFDC,1504612740723,C-Class,C级,Mercedes-Benz Passenger Cars,梅赛德斯-奔驰乘用车,C 180 L Style Sedan,C 180 L 时尚型轿车");

        String sql = "update otr_lead_lead set "
            + "comments=N%s, competitor_brand=%s, "
            + "competitor_class=%s, competitor_model=%s, "
            + "created_by=N%s, created_time=N%s, "
            + "current_channel=N%s, "
            + "grade=N%s, last_updated_by=%s, "
            + "lead_source=N%s, lead_type=N%s, "
            + "lost_sales_reason=N%s, lost_sales_time=N%s, "
            + "owner_sales_consultant_id=N%s, referral_lead_sfdc_id=%s, "
            + "role=N%s, sfdc_id=N%s, "
            + "sfdc_owner=%s, showroom_id=N%s, "
            + "status=N%s, system_owner=N%s, "
            + "updated_time=N%s, first_interested_vehicles_brand_description_en=N%s, "
            + "first_interested_vehicles_brand_description_zh=N%s, first_interested_vehicles_vehicle_class_description_en=N%s, "
            + "first_interested_vehicles_vehicle_class_description_zh=N%s, first_interested_vehicles_variant_description_en=N%s, "
            + "first_interested_vehicles_variant_description_zh=N%s "
            + "where id=%s and valid_time_from=%s;";

        assertEquals(String.format(sql,
            "'看车比价'", "null",
            "null", "null",
            "'sfdc'", "'1504399823000'",
            "'INBOUND'",
            "'E'", "null",
            "'经销商市场推广(媒体广告)'", "'户外'",
            "'Bought not MB, Audi or BMW'", "'1504612740723'",
            "'D8PANKUN'", "null",
            "'purchaser'", "'a166F000005ww2AQAQ'",
            "null", "'GS0014717'",
            "'LOST_SALES'", "'SFDC'",
            "'1504612740723'", "'Mercedes-Benz Passenger Cars'",
            "'梅赛德斯-奔驰乘用车'", "'C-Class'",
            "'C级'", "'C 180 L Style Sedan'",
            "'C 180 L 时尚型轿车'",
            "'L39259827-e514-4411-b7e9-5a4fc9721502'",
            "'2017-09-05 18:40:00.000'"
            ),
            Fixer.buildSql(fields, "2017-09-05 18:40:00.000"));
    }

    @Test
    public void should_transcode_single_quote() throws Exception {
        String value = "aaa'bbb'ccc'";
        String transcodedValue = Fixer.buildSqlFieldValue(value);
        assertEquals("N'aaa''bbb''ccc'''", transcodedValue);
    }
}