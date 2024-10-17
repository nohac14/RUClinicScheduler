package clinic;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProfileTest {

    @Test
    public void testCompareToReturnsMinusOne() {
        Profile profile1 = new Profile("Jane", "Doe", new Date(2, 2, 1991));
        Profile profile2 = new Profile("John", "Doe", new Date(1, 1, 1990));
        assertTrue(profile1.compareTo(profile2) < 0);
    }


    @Test
    public void testCompareToReturnsOne() {
        Profile profile1 = new Profile("John", "Doe", new Date(1, 1, 1992));
        Profile profile2 = new Profile("Jane", "Doe", new Date(2, 2, 1991));
        assertTrue(profile1.compareTo(profile2) > 0);
    }

    @Test
    public void testCompareToReturnsZero() {
        Profile profile1 = new Profile("John", "Doe", new Date(1, 1, 1990));
        Profile profile2 = new Profile("John", "Doe", new Date(1, 1, 1990));
        assertEquals(0, profile1.compareTo(profile2));
    }
}
