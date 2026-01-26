package Services;

import Data.DataManager;
import Models.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizService {
    private final DataManager<Quiz<String>> dataManager;
    private final ExecutorService executor;

    public QuizService(DataManager<Quiz<String>> dataManager) {
        this.dataManager = dataManager;
        this.executor = Executors.newFixedThreadPool(4);
    }

    public CompletableFuture<List<Quiz<String>>> loadQuizzesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try{
                Thread.sleep(300);
                return dataManager.loadAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> saveQuizAsync(Quiz<String> quiz) {
        return CompletableFuture.runAsync(() -> dataManager.addItems(quiz), executor);
    }

    public CompletableFuture<Void> updateQuizAsync(String id, Quiz<String> quiz) {
        return CompletableFuture.runAsync(() -> dataManager.updateItems(id, quiz), executor);
    }

    public CompletableFuture<Void> deleteQuizAsync(String id) {
        return CompletableFuture.runAsync(() -> dataManager.deleteItems(id), executor);
    }

    public List<Quiz<String>> getAllQuizzes() {
        return dataManager.loadAll();
    }


    public Optional<Quiz<String>> getQuizById(String id) {
        return dataManager.findById(id);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
