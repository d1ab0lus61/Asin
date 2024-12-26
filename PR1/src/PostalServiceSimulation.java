import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PostalServiceSimulation  {
    public static void main(String[] args) {
        PostOffice postOffice = new PostOffice();
        Random random = new Random();

        // Створення відправників
        List<Thread> senderThreads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Sender sender = new Sender("Sender-" + (i + 1), postOffice);
            Thread senderThread = new Thread(sender);
            senderThreads.add(senderThread);
            senderThread.start();
        }

        // Створення отримувачів
        List<Thread> receiverThreads = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Receiver receiver = new Receiver("Receiver-" + (i + 1), postOffice);
            Thread receiverThread = new Thread(receiver);
            receiverThreads.add(receiverThread);
            receiverThread.start();
        }


        // Створення працівника пошти
        MailWorker mailWorker = new MailWorker(postOffice);
        Thread workerThread = new Thread(mailWorker);
        workerThread.start();


        try {
            Thread.sleep(3000);
            mailWorker.stopWorking();
            System.out.println("End of working hours");
            workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Останні Threads
        for (Thread t : senderThreads) {
            t.interrupt();
        }
        for (Thread t : receiverThreads) {
            t.interrupt();
        }
    }
}