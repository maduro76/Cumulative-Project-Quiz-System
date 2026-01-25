import Controllers.*;
import Utilities.*;
import Models.*;
import Services.*;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ThreadManager.shutdown();
        }));

        QuizManager manager = new QuizManager();
        manager.run();

        System.out.println("\nThank you for using Quiz System!");
    }
}