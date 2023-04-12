import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameCounter {
    private int count;
    private File file;

    public GameCounter() {
        file = new File("GameCounterFile.txt");
        try {
            if (file.exists()) {
                // read the count value from the file
                Scanner scanner = new Scanner(file);
                count = scanner.nextInt();
                scanner.close();
            } else {
                // create a new file with the initial count value of 0
                file.createNewFile();
                count = 0;
                saveToFile();
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize counter: " + e.getMessage());
            // handle the exception here, such as logging or displaying an error message
        }
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
        try {
            saveToFile();
        } catch (IOException e) {
            System.err.println("Failed to save counter value: " + e.getMessage());
            // handle the exception here, such as logging or displaying an error message
        }
    }

    public static void incrementCounter() {
        new GameCounter().increment();
    }

    public static int getCountStat() {
        return new GameCounter().getCount();
    }

    private void saveToFile() throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(Integer.toString(count));
        writer.close();
    }

    public void reset() {
        count = 0;
        try {
            saveToFile();
        } catch (IOException e) {
            System.err.println("Failed to reset counter value: " + e.getMessage());
            // handle the exception here, such as logging or displaying an error message
        }
    }
    public static void resetCounter(){
        new GameCounter().reset();
    }
}
