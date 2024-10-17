package clinic;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ListTest {
    private List<Provider> providerList;

    @Before
    public void setUp() {
        providerList = new List<>();
    }

    @Test
    public void testAddDoctor() {
        Doctor doctor = new Doctor(new Profile("John", "Doe", new Date(1, 1, 1980)), Specialty.FAMILY, "12345", Location.BRIDGEWATER);
        providerList.add(doctor);
        assertEquals(1, providerList.size());
        assertTrue(providerList.contains(doctor));
    }

    @Test
    public void testAddTechnician() {
        Technician technician = new Technician(new Profile("Alice", "Smith", new Date(3, 15, 1985)), 50, Location.EDISON);
        providerList.add(technician);
        assertEquals(1, providerList.size());
        assertTrue(providerList.contains(technician));
    }

    @Test
    public void testRemoveDoctor() {
        Doctor doctor = new Doctor(new Profile("John", "Doe", new Date(1, 1, 1980)), Specialty.FAMILY, "12345", Location.BRIDGEWATER);
        providerList.add(doctor);
        providerList.remove(doctor);
        assertEquals(0, providerList.size());
        assertFalse(providerList.contains(doctor));
    }

    @Test
    public void testRemoveTechnician() {
        Technician technician = new Technician(new Profile("Alice", "Smith", new Date(3, 15, 1985)), 50, Location.EDISON);
        providerList.add(technician);
        providerList.remove(technician);
        assertEquals(0, providerList.size());
        assertFalse(providerList.contains(technician));
    }
}
