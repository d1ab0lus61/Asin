public class MailWorker implements Runnable {
    private final PostOffice postOffice;
    private boolean working = true;

    public MailWorker(PostOffice postOffice) {
        this.postOffice = postOffice;
    }

    @Override
    public void run() {
        System.out.println("Worker is processing mail...");
        while (working) {
            synchronized (postOffice) {
                while (!postOffice.hasMail() && working) {
                    try {
                        postOffice.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (postOffice.hasMail()) {
                    postOffice.notifyAll();
                }
            }
        }
    }

    public void stopWorking() {
        working = false;
    }
}
