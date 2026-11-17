import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class RunExperiment {

    // Calculate sample mean
    public static double mean(double[] arr) {
        double sum = 0;
        for (double v : arr) sum += v;
        return sum / arr.length;
    }

    // Calculate sample standard deviation
    public static double stddev(double[] arr) {
        double m = mean(arr);
        double sum = 0;
        for (double v : arr) sum += (v - m) * (v - m);
        return Math.sqrt(sum / (arr.length - 1));
    }

    public static void main(String[] args) throws IOException {
        int N = 500_000;       // Array size: 500,000 elements
        int trials = 20;       // Number of trials per CUTOFF
        int maxCutoff = 30;    // Test CUTOFF from 1 to 30
        long seed = 123456L;   // Fixed random seed for reproducibility

        System.out.println("1. Generating test dataset and saving sample to test_data_sample.txt...");
        Random rand = new Random(seed);
        Integer[] originalData = new Integer[N];
        PrintWriter dataOut = new PrintWriter(new FileWriter("test_data_sample.txt"));
        for (int i = 0; i < N; i++) {
            originalData[i] = rand.nextInt(1_000_000);
            if (i < 10_000) {
                dataOut.println(originalData[i]);
            }
        }
        dataOut.close();

        System.out.println("2. Warming up JVM (JIT compilation)...");
        for (int i = 0; i < 5; i++) {
            Integer[] temp = Arrays.copyOf(originalData, N);
            MergeCutoff.sort(temp, 7);
        }

        System.out.println("3. Running benchmark for CUTOFF from 1 to " + maxCutoff + "...");
        PrintWriter csvOut = new PrintWriter(new FileWriter("ket_qua.csv"));
        csvOut.println("Cutoff,MeanTimeMs,StdDevMs");

        System.out.println("----------------------------------------------");
        System.out.printf("%-10s | %-15s | %-15s%n", "CUTOFF", "Mean Time (ms)", "Std Dev (ms)");
        System.out.println("----------------------------------------------");

        for (int cutoff = 1; cutoff <= maxCutoff; cutoff++) {
            double[] times = new double[trials];

            for (int t = 0; t < trials; t++) {
                Integer[] a = Arrays.copyOf(originalData, N);

                long start = System.nanoTime();
                MergeCutoff.sort(a, cutoff);
                long end = System.nanoTime();

                times[t] = (end - start) / 1_000_000.0;
            }

            double avg = mean(times);
            double std = stddev(times);

            System.out.printf(Locale.US, "%-10d | %-15.2f | %-15.2f%n", cutoff, avg, std);
            csvOut.printf(Locale.US, "%d,%.4f,%.4f%n", cutoff, avg, std);
            csvOut.flush();
        }

        csvOut.close();
        System.out.println("----------------------------------------------");
        System.out.println("Done! Results saved to 'ket_qua.csv' and 'test_data_sample.txt'.");
    }
}