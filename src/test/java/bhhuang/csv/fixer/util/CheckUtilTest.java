package bhhuang.csv.fixer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckUtilTest {
    private static final String COMMA = ",";
    private static final String field_count_not_matched_line = "58ccdcd94e223d000f4c8ef5,L47b5206e-146d-41bd-b5e3-b3fd1c12b1e6,,,,WALK_IN,有奔驰" + COMMA + "置换" + COMMA + "等她儿子回来再订，,,,,otr,1503642608674,WALK_IN,,D,,,,,,,D8HAOYTA,,purchaser,a166F000005fkWCQAY,,,LEAD,OTR,1504613400978,A-Class,A级,Mercedes-Benz Passenger Cars,梅赛德斯-奔驰乘用车,A 200 Dynamic,A 200 动感型";

    @Test
    public void should_return_true_when_number_of_field_count_matched() throws Exception {
        String line = "58ccdcd94e223d000f4c8ef5,L47b5206e-146d-41bd-b5e3-b3fd1c12b1e6,,,,WALK_IN,有奔驰置换，等她儿子回来再订，,,,,otr,1503642608674,WALK_IN,,D,,,,,,,D8HAOYTA,,purchaser,a166F000005fkWCQAY,,,LEAD,OTR,1504613400978,A-Class,A级,Mercedes-Benz Passenger Cars,梅赛德斯-奔驰乘用车,A 200 Dynamic,A 200 动感型";

        boolean isMatched = CheckUtil.isFieldCountMatched(line, 36);

        assertTrue(isMatched);
    }

    @Test
    public void should_return_false_when_number_of_field_count_not_matched() throws Exception {
        boolean isMatched = CheckUtil.isFieldCountMatched(field_count_not_matched_line, 36);

        assertFalse(isMatched);
    }

    @Test
    public void should_get_correct_comment_field_from_multiple_comma_separated_fields() throws Exception {
        String correctCommentFieldValue = "有奔驰,置换,等她儿子回来再订，";

        assertEquals(correctCommentFieldValue, CheckUtil.getCorrectFieldValue(field_count_not_matched_line, 6, 36));
    }

    @Test
    public void should_get_left_fields_before_correct_field_index() throws Exception {
        String leftFieldsValue = "58ccdcd94e223d000f4c8ef5,L47b5206e-146d-41bd-b5e3-b3fd1c12b1e6,,,,WALK_IN";
        String[] leftFields = CheckUtil.getLeftFields(field_count_not_matched_line, 6);
        assertEquals(leftFieldsValue, String.join(",", leftFields));
    }

    @Test
    public void should_get_right_fields_after_correct_field_index() throws Exception {
        String rightFieldsValue = ",,,otr,1503642608674,WALK_IN,,D,,,,,,,D8HAOYTA,,purchaser,a166F000005fkWCQAY,,,LEAD,OTR,1504613400978,A-Class,A级,Mercedes-Benz Passenger Cars,梅赛德斯-奔驰乘用车,A 200 Dynamic,A 200 动感型";
        String[] rightFields = CheckUtil.getRightFields(field_count_not_matched_line, 6, 36);
        assertEquals(rightFieldsValue, String.join(",", rightFields));
    }

    @Test
    public void merged_raw_value_should_be_equal_with_original_value() throws Exception {
        String[] leftFields = CheckUtil.getLeftFields(field_count_not_matched_line, 6);
        String[] rightFields = CheckUtil.getRightFields(field_count_not_matched_line, 6, 36);
        String correctFieldValue = CheckUtil.getCorrectFieldValue(field_count_not_matched_line, 6, 36);

        assertEquals(field_count_not_matched_line, String.join(COMMA, leftFields) + COMMA + correctFieldValue + COMMA + String.join(COMMA, rightFields));
    }
}