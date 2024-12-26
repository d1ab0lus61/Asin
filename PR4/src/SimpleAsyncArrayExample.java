import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SimpleAsyncArrayExample  {
    public static void main(String[] args) {
        CompletableFuture<int[][]> arrayFuture = CompletableFuture.supplyAsync(() -> {

            long startTime = System.nanoTime();

            int[][] array = new int[3][3];
            // Генерація випадкових чисел для масиву
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    array[i][j] = (int) (Math.random() * 100);
                }
            }
            System.out.printf(
                    "Час котрий був затрачений для створення масиву: %.2f мс\n",
                    (System.nanoTime() - startTime) / 1_000_000.0);

            return array;
        });


        arrayFuture.thenAcceptAsync(array -> {
            long startTime = System.nanoTime();

            System.out.println("Початковий масив:");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(array[i][j] + " ");
                }
                System.out.println();
            }

            System.out.printf(
                    "Час котрий був затрачений на вивід початкового масиву: %.2f мс\n",
                    (System.nanoTime() - startTime) / 1_000_000.0);
        });

        arrayFuture.thenAcceptAsync(array -> {
            long startTime = System.nanoTime();
            for (int j = 0; j < 3; j++) {

                StringBuilder column = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                    column.append(array[i][j]).append(", ");
                }

                // Виведення стовпця
                System.out.println("Стовпець " + (j + 1) + ": " + column);

            }
            System.out.printf(
                    "Час котрий був затрачений на вивід стовпців масиву по черзі: %.2f мс\n",
                    (System.nanoTime() - startTime) / 1_000_000.0);
        }).thenRunAsync(() -> System.out.println("\tАсинхронна задача на вивід стовпців була закінчена.")).join();
    }
}