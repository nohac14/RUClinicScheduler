package clinic;

public class Patient extends Person {
    private Visit visits;  // Linked list of completed visits

    public Patient(Profile profile) {
        super(profile);
        this.visits = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Patient other = (Patient) obj;
        return super.getProfile().equals(other.getProfile());
    }

    @Override
    public String toString() {
        return super.getProfile().toString();
    }

    @Override
    public int compareTo(Person other) {
        if (other instanceof Patient) {
            return this.getProfile().compareTo(other.getProfile());
        }
        return 0;
    }

    public int charge() {
        int totalCharge = 0;
        Visit currentVisit = visits;

        while (currentVisit != null) {
            // Ensure the provider is a Doctor or a Technician before calling rate()
            Person provider = currentVisit.getAppointment().getProvider();
            if (provider instanceof Provider) {
                totalCharge += ((Provider) provider).rate();  // Cast to Provider to access rate()
            }
            currentVisit = currentVisit.getNext();
        }

        return totalCharge;
    }


    public void addVisit(Appointment appointment) {
        Visit newVisit = new Visit(appointment);
        newVisit.setNext(visits);
        visits = newVisit;
    }

    public Visit getVisits() {
        return visits;
    }
}