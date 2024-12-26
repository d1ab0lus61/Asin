import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class AsyncCalculation {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();


        double[] numbers = new Random().doubles(20, 1, 10).toArray();


        System.out.println("Початкова послідовність чисел:");
        System.out.println(Arrays.toString(numbers));

        CompletableFuture<Double> minOddFuture = CompletableFuture.supplyAsync(() -> findMinOdd(numbers));
        CompletableFuture<Double> maxEvenFuture = CompletableFuture.supplyAsync(() -> findMaxEven(numbers));

        minOddFuture.thenAcceptAsync(minOdd -> {
            System.out.println("Мінімальне значення серед непарних індексів: " + minOdd);
        });

        maxEvenFuture.thenAcceptAsync(maxEven -> {
            System.out.println("Максимальне значення серед парних індексів: " + maxEven);
            double result = minOddFuture.join() + maxEven;
            System.out.println("Результат: min(непарні) + max(парні) = " + result);
        });

        CompletableFuture.runAsync(() -> {
            try {
                minOddFuture.get();
                maxEvenFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Час виконання усіх асинхронних операцій: " + (endTime - startTime) + " мс");
        }).join();
    }


    private static double findMinOdd(double[] numbers) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numbers.length; i += 2) {  // (1, 3, 5, ...)
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    private static double findMaxEven(double[] numbers) {
        double max = Double.MIN_VALUE;  // Початкове значення для максимуму
        for (int i = 1; i < numbers.length; i += 2) {  //  (2, 4, 6, ...)
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

}
