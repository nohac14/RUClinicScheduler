package clinic;

public class Sort {

    /**
     * Sorts a list of appointments based on the given key.
     * 'p' for sorting by patient profile (last name, first name, DOB),
     * 'd' for sorting by date and timeslot,
     * 'l' for sorting by provider's location (county).
     *
     * @param list the list of appointments to be sorted.
     * @param key  the key to sort by ('p' for patient, 'd' for date, 'l' for location).
     */
    public static void appointment(List<Appointment> list, char key) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                Appointment a1 = list.get(j);
                Appointment a2 = list.get(j + 1);

                boolean swap = false;
                switch (key) {
                    case 'p':
                        // Sort by patient profile, then date, then timeslot
                        if (comparePatientProfile(a1, a2) > 0) {
                            swap = true;
                        }
                        break;
                    case 'd':
                        // Sort by date, then timeslot, then provider name
                        if (compareByDateAndTimeslot(a1, a2) > 0) {
                            swap = true;
                        }
                        break;
                    case 'l':
                        // Sort by provider's county, then date, then timeslot
                        if (compareByLocation(a1, a2) > 0) {
                            swap = true;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid sorting key: " + key + ". Valid keys are 'p', 'd', 'l'.");
                }

                if (swap) {
                    // Swap the appointments in the list
                    Appointment temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Compares two appointments by patient profile, then by date and timeslot.
     */
    private static int comparePatientProfile(Appointment a1, Appointment a2) {
        int patientComparison = a1.getPatient().compareTo(a2.getPatient());
        if (patientComparison != 0) return patientComparison;

        // If profiles are the same, compare by date and timeslot
        int dateComparison = a1.getDate().compareTo(a2.getDate());
        if (dateComparison != 0) return dateComparison;

        return a1.getTimeslot().compareTo(a2.getTimeslot());
    }

    /**
     * Compares two appointments by date, timeslot, and provider name.
     */
    private static int compareByDateAndTimeslot(Appointment a1, Appointment a2) {
        int dateComparison = a1.getDate().compareTo(a2.getDate());
        if (dateComparison != 0) return dateComparison;

        int timeslotComparison = a1.getTimeslot().compareTo(a2.getTimeslot());
        if (timeslotComparison != 0) return timeslotComparison;

        // If date and timeslot are the same, compare provider names
        return a1.getProvider().getName().compareTo(a2.getProvider().getName());
    }

    /**
     * Compares two appointments by provider's county, then by date and timeslot.
     */
    private static int compareByLocation(Appointment a1, Appointment a2) {
        // Ensure both providers are actually of type Provider
        if (a1.getProvider() instanceof Provider provider1 && a2.getProvider() instanceof Provider provider2) {

            String county1 = provider1.getLocation().getCounty();
            String county2 = provider2.getLocation().getCounty();

            int countyComparison = county1.compareTo(county2);
            if (countyComparison != 0) return countyComparison;

            int dateComparison = a1.getDate().compareTo(a2.getDate());
            if (dateComparison != 0) return dateComparison;

            return a1.getTimeslot().compareTo(a2.getTimeslot());
        } else {
            throw new IllegalArgumentException("Both participants in comparison must be Providers.");
        }
    }


    /**
     * Sorts a list of providers by their name.
     *
     * @param list the list of providers to be sorted.
     */
    // Sorts providers using their profile comparison (last name, first name, DOB)
    public static void provider(List<Provider> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                Provider p1 = list.get(j);
                Provider p2 = list.get(j + 1);

                if (p1.getProfile().compareTo(p2.getProfile()) > 0) {
                    // Swap providers in the list if not in correct order
                    Provider temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}
