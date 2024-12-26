import javax.swing.JFileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;

public class FileSearch {

    public static void main(String[] args) {
        // Діалогове вікно для вибору директорії
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            String searchString = JOptionPane.showInputDialog("Введіть літеру або слово для пошуку:");

            if (searchString != null && !searchString.isEmpty()) {
                ExecutorService es = Executors.newFixedThreadPool(2);
                try {
                    int totalCount = searchFiles(es, directory, searchString);
                    JOptionPane.showMessageDialog(null, "Знайдено файлів: " + totalCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Помилка при пошуку.");
                } finally {
                    es.shutdown();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Пошукове слово не було введено.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Директорію не було обрано.");
        }
    }

    private static int searchFiles(ExecutorService executorService, File directory, String searchString) throws Exception {
        List<Future<Integer>> futures = new ArrayList<>();
        int count = 0;

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Якщо директорія, створюємо завдання пошуку і в цій директорії
                        futures.add(executorService.submit(() -> searchFiles(executorService, file, searchString)));
                    } else if (file.getName().contains(searchString)) {
                        count++;
                    }
                }

                // Чекаємо на завершення всіх завдань
                for (Future<Integer> future : futures) {
                    count += future.get();
                }
            }
        }

        return count;
    }
}
