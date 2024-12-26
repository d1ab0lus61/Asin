import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class FindPrimes {

    public static void main(String[] args) {
        int numberOfThreads = 2;
        ExecutorService es = Executors.newFixedThreadPool(numberOfThreads);
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введіть число N (макс число, до якого шукаємо прості числа):");
            int N = scanner.nextInt();
            scanner.close();


            int partSize = N / numberOfThreads;


            List<Callable<List<Integer>>> tasks = new ArrayList<>();
            for (int i = 0; i < numberOfThreads; i++) {
                int startRange = i * partSize;
                int endRange = (i == numberOfThreads - 1) ? N : (i + 1) * partSize - 1;
                tasks.add(new PrimeNumberTask(startRange, endRange));
            }

            long startTime = System.currentTimeMillis();
            List<Future<List<Integer>>> futures = es.invokeAll(tasks);
            CopyOnWriteArrayList<Integer> primeNumbers = new CopyOnWriteArrayList<>();

            for (Future<List<Integer>> future : futures) {
                primeNumbers.addAll(future.get());
            }
            long endTime = System.currentTimeMillis();


            System.out.print("Прості числа до " + N + ":");
            System.out.println(primeNumbers);
            System.out.println("Час виконання: " + (endTime - startTime) + " мс");

        } catch (Exception e) {
            e.printStackTrace();
        }
        es.shutdown();


    }
}




class PrimeNumberTask implements Callable<List<Integer>> {
    private final int start;
    private final int end;

    public PrimeNumberTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public List<Integer> call() {
        List<Integer> primes = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }


    private boolean isPrime(int number) {
        if (number < 2) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}