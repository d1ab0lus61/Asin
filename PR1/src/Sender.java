import java.util.Random;

public class Sender implements Runnable {
    private final String senderName;
    private final PostOffice postOffice;
    private final Random random = new Random();

    public Sender(String senderName, PostOffice postOffice) {
        this.senderName = senderName;
        this.postOffice = postOffice;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(random.nextInt(100) + 500);
                String recipientName = "Receiver-" + (random.nextInt(3) + 1);
                String content = "Hello from " + senderName;
                postOffice.addMail(new Mail(senderName, recipientName, content));
            }
        } catch (InterruptedException e) {
            System.out.println(senderName + " stopped sending due to interruption.");
        }
    }
}
