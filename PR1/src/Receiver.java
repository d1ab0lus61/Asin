import java.util.Random;

class Receiver implements Runnable {
    private final String receiverName;
    private final PostOffice postOffice;
    private final Random random = new Random();

    public Receiver(String receiverName, PostOffice postOffice) {
        this.receiverName = receiverName;
        this.postOffice = postOffice;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(random.nextInt(100) + 500); // Simulate work delay
                Mail mail = postOffice.fetchMail(receiverName);
                if (mail != null) {
                    System.out.println(receiverName + " has processed mail: " + mail.getContent());
                } else {
                    System.out.println(receiverName + " found no mail to process.");
                }
            }
        } catch (InterruptedException e) {

            System.out.println(receiverName+ " was interrupted by end of working hours");
//            e.printStackTrace();
        }
    }
}
