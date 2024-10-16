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
                        if (a1.getPatient().compareTo(a2.getPatient()) > 0 ||
                                (a1.getPatient().compareTo(a2.getPatient()) == 0 &&
                                        a1.getDate().compareTo(a2.getDate()) > 0) ||
                                (a1.getPatient().compareTo(a2.getPatient()) == 0 &&
                                        a1.getDate().compareTo(a2.getDate()) == 0 &&
                                        a1.getTimeslot().compareTo(a2.getTimeslot()) > 0)) {
                            swap = true;
                        }
                        break;
                    case 'd':
                        // Sort by date, then timeslot, then provider name
                        if (a1.getDate().compareTo(a2.getDate()) > 0 ||
                                (a1.getDate().compareTo(a2.getDate()) == 0 &&
                                        a1.getTimeslot().compareTo(a2.getTimeslot()) > 0) ||
                                (a1.getDate().compareTo(a2.getDate()) == 0 &&
                                        a1.getTimeslot().compareTo(a2.getTimeslot()) == 0 &&
                                        a1.getProvider().getName().compareTo(a2.getProvider().getName()) > 0)) {
                            swap = true;
                        }
                        break;
                    case 'l':d
                        // Sort by provider's county, then date, then timeslot
                        if (a1.getProvider().getLocation().getCounty().compareTo(
                                a2.getProvider().getLocation().getCounty()) > 0 ||
                                (a1.getProvider().getLocation().getCounty().compareTo(
                                        a2.getProvider().getLocation().getCounty()) == 0 &&
                                        a1.getDate().compareTo(a2.getDate()) > 0) ||
                                (a1.getProvider().getLocation().getCounty().compareTo(
                                        a2.getProvider().getLocation().getCounty()) == 0 &&
                                        a1.getDate().compareTo(a2.getDate()) == 0 &&
                                        a1.getTimeslot().compareTo(a2.getTimeslot()) > 0)) {
                            swap = true;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid sorting key: " + key);
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
     * Sorts a list of providers by their name.
     *
     * @param list the list of providers to be sorted.
     */
    public static void provider(List<Provider> list) {
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                Provider p1 = list.get(j);
                Provider p2 = list.get(j + 1);

                if (p1.getName().compareTo(p2.getName()) > 0) {
                    // Swap the providers in the list
                    Provider temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
}