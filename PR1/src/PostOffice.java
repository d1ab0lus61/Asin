import java.util.ArrayList;
import java.util.List;

public class PostOffice {

    private final List<Mail> mailQueue = new ArrayList<>();

    public synchronized void addMail(Mail mail) {
        mailQueue.add(mail);
        System.out.println("SEND-> " + mail.getSenderName() + " відправив лист: " + mail.getContent());
        notifyAll();
    }

    // Метод отримання листа
    public synchronized Mail fetchMail(String receiverName) {
        for (Mail mail : mailQueue) {
            if (mail.getRecipientName().equals(receiverName)) {
                mailQueue.remove(mail);
                System.out.println("RECEIVE<- " +receiverName + " отримав лист: " + mail.getContent());
                return mail;
            }
        }
        return null;
    }

    public synchronized boolean hasMail() {
        return !mailQueue.isEmpty();
    }
}
