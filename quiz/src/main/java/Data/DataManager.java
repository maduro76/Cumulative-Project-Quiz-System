package Data;

import Models.Quiz;

import java.util.List;
import java.util.Optional;

public interface DataManager<T> {
    List<T> loadAll();
    void saveAll(List<T> items);
    Optional<T> findById(String id);
    void addItems(T item);
    void updateItems(String id, T item);
    void deleteItems(String id);
}
