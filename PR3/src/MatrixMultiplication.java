import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class MatrixMultiplication {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Введіть кількість рядків у 1-й матриці: ");
        int rowsA = scanner.nextInt();
        System.out.print("Введіть кількість стовпців у 1-й матриці: ");
        int colsA = scanner.nextInt();
        System.out.print("Введіть кількість стовпців у 2-й матриці: ");
        int colsB = scanner.nextInt();


        int[][] matrixA = generateRandomMatrix(rowsA, colsA);
        int[][] matrixB = generateRandomMatrix(colsA, colsB);

        System.out.println("\nМатриця A:");
        printMatrix(matrixA);
        System.out.println("\nМатриця B:");
        printMatrix(matrixB);

        // ForkJoinPool
        System.out.println("\nРозрахунок з ForkJoinPool:");
        long startTime = System.nanoTime();
        int[][] resultForkJoin = multiplyWithForkJoin(matrixA, matrixB);
        printMatrix(resultForkJoin);
        System.out.println("Час: " + (System.nanoTime() - startTime) / 1_000_000 + " мс");

        // ExecutorService
        System.out.println("\nРозрахунок з ExecutorService:");
        ExecutorService executor = Executors.newFixedThreadPool(4);
        startTime = System.nanoTime();
        int[][] resultExecutor = multiplyWithExecutorService(matrixA, matrixB, executor);
        printMatrix(resultExecutor);
        System.out.println("Час: " + (System.nanoTime() - startTime) / 1_000_000 + " мс");

        executor.shutdown();
    }

    public static int[][] generateRandomMatrix(int rows, int cols) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(10);
            }
        }
        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    // Множення за допомогою ForkJoinPool
    public static int[][] multiplyWithForkJoin(int[][] matrixA, int[][] matrixB) throws InterruptedException, ExecutionException {
        int rows = matrixA.length;
        int colsB = matrixB[0].length;
        int[][] result = new int[rows][colsB];
        ForkJoinPool pool = new ForkJoinPool();
        List<RecursiveTask<Void>> tasks = new ArrayList<>();

        // Створюємо завдання для кожного рядка матриці
        for (int i = 0; i < matrixA.length; i++) {
            final int row = i;
            tasks.add(new RecursiveTask<Void>() {
                @Override
                protected Void compute() {
                    for (int j = 0; j < matrixB[0].length; j++) {
                        for (int k = 0; k < matrixB.length; k++) {
                            result[row][j] += matrixA[row][k] * matrixB[k][j];
                        }
                    }
                    return null;
                }
            });
        }
        for (RecursiveTask<Void> task : tasks) {
            pool.invoke(task);
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);

        return result;
    }

    // Множення за допомогою ExecutorService
    public static int[][] multiplyWithExecutorService(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
        int rowsA = matrixA.length;
        int colsB = matrixB[0].length;
        int[][] result = new int[rowsA][colsB];
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < rowsA; i++) {
            final int row = i;
            tasks.add(() -> {
                for (int j = 0; j < colsB; j++) {
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
                return null;
            });
        }

        executor.invokeAll(tasks);
        return result;
    }
}
