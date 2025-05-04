import java.util.Scanner;

// Process class
class Process {
    public int id;
    public String status;

    public Process(int id) {
        this.id = id;
        this.status = "active";
    }
}

public class BullyRing1 {

    Scanner sc ;
    Process[] processes;
    int n;

    public BullyRing1() {
        sc = new Scanner(System.in);
    }

    // Initialize processes
    public void ring() {
        System.out.print("Enter total number of processes: ");
        n = sc.nextInt();
        processes = new Process[n];
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(i);
        }
    }

    // Bully Algorithm
    public void performBullyElection() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Process " + processes[getMaxValue()].id + " fails");
        processes[getMaxValue()].status = "Inactive";

        int idOfInitiator = 0;
        boolean overStatus = true;

        while (overStatus) {
            boolean higherProcesses = false;
            System.out.println();

            for (int i = idOfInitiator + 1; i < n; i++) {
                if (processes[i].status.equals("active")) {
                    System.out.println("Process " + idOfInitiator + " Passes Election(" + idOfInitiator + ") message to Process " + i);
                    higherProcesses = true;
                }
            }

            if (higherProcesses) {
                System.out.println();
                for (int i = idOfInitiator + 1; i < n; i++) {
                    if (processes[i].status.equals("active")) {
                        System.out.println("Process " + i + " passes Ok(" + i + ") message to Process " + idOfInitiator);
                    }
                }
                idOfInitiator++;
            } 
            else {
                int coord = processes[getMaxValue()].id;
                System.out.println("Finally Process " + coord + " Becomes Coordinator");

                for (int i = coord - 1; i >= 0; i--) {
                    if (processes[i].status.equals("active")) {
                        System.out.println("Process " + coord + " passes Coordinator(" + coord + ") message to Process " + i);
                    }
                }
                System.out.println("\nEnd of Election");
                overStatus = false;
                break;
            }
        }
    }

    // Ring Algorithm
    public void performRingElection() {
        System.out.print("Enter the ID of the process that initiates the election: ");
        int initiator = sc.nextInt();

        if (processes[initiator].status.equals("Inactive")) {
            System.out.println("Initiator process is inactive. Cannot start election.");
            return;
        }

        System.out.println("Process " + initiator + " starts Ring Election");

        int coordinator = -1;
        int current = (initiator + 1) % n;
        int[] electionMessage = new int[n];
        int msgCount = 0;

        // Start passing the election message in the ring
        electionMessage[msgCount++] = processes[initiator].id;

        while (current != initiator) {
            if (processes[current].status.equals("active")) {
                System.out.println("Process " + current + " receives Election message and forwards it");
                electionMessage[msgCount++] = processes[current].id;
            }
            current = (current + 1) % n;
        }

        // Find max ID from the collected message
        coordinator = electionMessage[0];
        for (int i = 1; i < msgCount; i++) {
            if (electionMessage[i] > coordinator) {
                coordinator = electionMessage[i];
            }
        }

        System.out.println("Process " + coordinator + " is elected as Coordinator");

        // Send coordinator message around the ring
        current = (coordinator + 1) % n;
        while (current != coordinator) {
            if (processes[current].status.equals("active")) {
                System.out.println("Coordinator message sent to Process " + current);
            }
            current = (current + 1) % n;
        }

        System.out.println("\nEnd of Ring Election");
    }

    // Get max active process
    public int getMaxValue() {
        int mxId = -1;
        int mxIdIndex = -1;
        for (int i = 0; i < processes.length; i++) {
            if (processes[i].status.equals("active") && processes[i].id > mxId) {
                mxId = processes[i].id;
                mxIdIndex = i;
            }
        }
        return mxIdIndex;
    }

    // Main method
    public static void main(String[] args) {
        BullyRing1 obj = new BullyRing1();
        obj.ring();

        while (true) {
            System.out.println("\nChoose Election Algorithm:");
            System.out.println("1. Bully Algorithm");
            System.out.println("2. Ring Algorithm");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = obj.sc.nextInt();

            switch (choice) {
                case 1:
                    obj.performBullyElection();
                    break;
                case 2:
                    obj.performRingElection();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
