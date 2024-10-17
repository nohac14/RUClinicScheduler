package clinic;

import static org.junit.Assert.*;
import org.junit.Test;

public class DateTest {

    @Test
    public void testIsValidValidDates() {
        Date validDate1 = new Date(2, 29, 2020);  // Valid leap year date
        Date validDate2 = new Date(12, 31, 2021);  // Valid end-of-year date
        assertTrue(validDate1.isValid());
        assertTrue(validDate2.isValid());
    }

    @Test
    public void testIsValidInvalidDates() {
        Date invalidDate1 = new Date(2, 30, 2021);  // Invalid date (February can't have 30)
        Date invalidDate2 = new Date(4, 31, 2021);  // Invalid date (April can't have 31)
        Date invalidDate3 = new Date(13, 1, 2021);  // Invalid month (13 is not a valid month)
        Date invalidDate4 = new Date(2, 29, 2021);  // Invalid leap year date
        assertFalse(invalidDate1.isValid());
        assertFalse(invalidDate2.isValid());
        assertFalse(invalidDate3.isValid());
        assertFalse(invalidDate4.isValid());
    }
}
