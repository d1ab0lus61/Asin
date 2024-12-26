import java.util.concurrent.CompletableFuture;

public class FirstCompletableTask {
    public static void main(String[] args) {

        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((long) (Math.random() * 2000));
                System.out.println("Завдання 1 завершено успішно");
                return "Завдання 1";
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((long) (Math.random() * 2000));
                System.out.println("Завдання 2 завершено успішно ");
                return "Завдання 2";
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((long) (Math.random() * 2000));
                System.out.println("Завдання 3 завершено успішно");
                return "Завдання 3";
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });


        CompletableFuture<?> firstCompleted = CompletableFuture.anyOf(task1, task2, task3);

        firstCompleted.thenCompose(result ->
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Перше завершене завдання: " + result);
                return null;
            })).join();

    }
}