import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class FlightBooking {

    private static Random random = new Random();

    public static void main(String[] args) {
        CompletableFuture<Boolean> checkSeats = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(random.nextInt(2)* 1000L);
                System.out.println("Перевірка наявності місць...");
                return random.nextBoolean();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });


        CompletableFuture<Double> findBestPrice = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(random.nextInt(2)*1500L);
                System.out.println("Пошук найкращої ціни...");
                return 50 + (200 - 50) * random.nextDouble(); // Ціна в діапазоні [50, 200]
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        CompletableFuture<Void> booking = checkSeats.thenCombine(findBestPrice, (seatsAvailable, price) -> {
            if (seatsAvailable) {
                System.out.println("Місця є! Найкраща ціна: " + String.format("%.2f", price) + " USD.");
                return true;
            } else {
                System.out.println("Немає вільних місць.");
                return false;
            }
        }).thenCompose(available -> {
            if (available) {
                // Якщо місця є, здійснюємо оплату
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Оплата...");
                        return random.nextBoolean();
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                });
            } else {
                // Якщо місць немає, припиняємо процес
                return CompletableFuture.supplyAsync(() -> false);
            }
        }).thenAccept(paymentSuccess -> {
            if (paymentSuccess) {
                System.out.println("Бронювання квитка завершено успішно!");
            } else {
                System.out.println("Не вдалося здійснити оплату або немає місць.");
            }
        });

        booking.join();
    }
}
