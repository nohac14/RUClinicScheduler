package clinic;

/**
 * The RunProject1 class is the main entry point for the clinic scheduling application.
 * It creates an instance of the Scheduler class and initiates the scheduling system.
 * The Scheduler class handles commands such as scheduling, canceling, and rescheduling appointments.
 * This class is responsible for starting the program.
 *
 * @author Jonas Lazebnik, Arjun Anand
 */
public class RunProject1 {

    /**
     * The main method that starts the clinic scheduling application by creating
     * a new instance of the Scheduler and invoking its run method.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        new Scheduler().run();
    }
}
