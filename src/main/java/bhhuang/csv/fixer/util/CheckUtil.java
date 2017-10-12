package bhhuang.csv.fixer.util;

import java.util.Arrays;

public class CheckUtil {
    private static final String COMMA = ",";

    public static boolean isFieldCountMatched(String line, int fieldCount) {
        String[] fields = line.split(COMMA, -1);
        return fields.length == fieldCount;
    }

    public static String getCorrectFieldValue(String line, int correctFieldIndex, int correctFieldCount) {
        String[] fields = line.split(COMMA, -1);
        int fieldCount = fields.length;
        int fieldCountAfterCorrectFieldIndex = correctFieldCount - correctFieldIndex;
        int endIndex = fieldCount - fieldCountAfterCorrectFieldIndex + 1;
        String[] fieldRange = Arrays.copyOfRange(fields, correctFieldIndex, endIndex);
        String correctFieldValue = String.join(COMMA, fieldRange);
        return correctFieldValue;
    }


    public static String[] getLeftFields(String line, int correctFieldIndex) {
        String[] fields = line.split(COMMA, -1);
        String[] leftFields = Arrays.copyOfRange(fields, 0, correctFieldIndex);
        return leftFields;
    }

    public static String[] getRightFields(String line, int correctFieldIndex, int correctFieldCount) {
        String[] fields = line.split(COMMA, -1);
        int fieldCount = fields.length;
        int from = correctFieldIndex + (fieldCount - correctFieldCount) + 1;
        int to = fieldCount;

        return Arrays.copyOfRange(fields, from, to);
    }
}
