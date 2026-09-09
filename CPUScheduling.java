import java.util.*;

class Process {
    String id;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;

    Process(String id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    Process copy() {
        return new Process(id, arrivalTime, burstTime);
    }
}

public class CPUScheduling {

    static final int TIME_QUANTUM = 3;

    static Process[] createProcesses() {
        return new Process[] {
            new Process("P1", 0, 8),
            new Process("P2", 0, 4),
            new Process("P3", 0, 9),
            new Process("P4", 0, 5)
        };
    }

    // Algorithm A: FCFS
    static void fcfs(Process[] processes) {
        int currentTime = 0;
        int contextSwitch = 0;

        System.out.println("\n===== FCFS =====");

        for (int i = 0; i < processes.length; i++) {
            Process p = processes[i];

            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }

            System.out.println(
                "Time " + currentTime + " -> " +
                (currentTime + p.burstTime) + ": " + p.id
            );

            currentTime += p.burstTime;

            p.completionTime = currentTime;
            p.turnaroundTime =
                    p.completionTime - p.arrivalTime;
            p.waitingTime =
                    p.turnaroundTime - p.burstTime;

            if (i > 0) {
                contextSwitch++;
            }
        }

        printResults(processes, contextSwitch);
    }

    // Algorithm B: Round Robin
    static void roundRobin(Process[] processes) {
        Queue<Process> queue = new ArrayDeque<>();

        for (Process p : processes) {
            queue.offer(p);
        }

        int currentTime = 0;
        int contextSwitch = 0;
        Process previous = null;

        System.out.println("\n===== ROUND ROBIN =====");

        while (!queue.isEmpty()) {

            Process p = queue.poll();

            if (previous != null && previous != p) {
                contextSwitch++;
            }

            int executionTime =
                    Math.min(TIME_QUANTUM, p.remainingTime);

            System.out.println(
                "Time " + currentTime + " -> " +
                (currentTime + executionTime) +
                ": " + p.id
            );

            currentTime += executionTime;
            p.remainingTime -= executionTime;

            if (p.remainingTime == 0) {
                p.completionTime = currentTime;
                p.turnaroundTime =
                        p.completionTime - p.arrivalTime;
                p.waitingTime =
                        p.turnaroundTime - p.burstTime;
            } else {
                queue.offer(p);
            }

            previous = p;
        }

        printResults(processes, contextSwitch);
    }

    static void printResults(
            Process[] processes,
            int contextSwitch) {

        double totalWaiting = 0;
        double totalTurnaround = 0;

        System.out.println(
            "\nProcess\tWaiting\tTurnaround"
        );

        for (Process p : processes) {
            System.out.println(
                p.id + "\t" +
                p.waitingTime + "\t" +
                p.turnaroundTime
            );

            totalWaiting += p.waitingTime;
            totalTurnaround += p.turnaroundTime;
        }

        System.out.println(
            "Average Waiting Time = " +
            totalWaiting / processes.length
        );

        System.out.println(
            "Average Turnaround Time = " +
            totalTurnaround / processes.length
        );

        System.out.println(
            "Context Switches = " + contextSwitch
        );
    }

    public static void main(String[] args) {

        Process[] fcfsProcesses = createProcesses();
        Process[] rrProcesses = createProcesses();

        fcfs(fcfsProcesses);
        roundRobin(rrProcesses);
    }
}
