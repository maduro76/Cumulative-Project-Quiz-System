package FileHandling;

import Quizes.Quiz;
import Questions.IQuestion;
import Questions.Question;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveAndReadJSON {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(IQuestion.class, (JsonDeserializer<IQuestion>) (json, typeOfT, context) ->
                    context.deserialize(json, Question.class))
            .create();
    public static void saveQuiz(List<Quiz<String>> quizzes, String fileName){
        try (FileWriter writer = new FileWriter(fileName)){
            gson.toJson(quizzes, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<Quiz<String>> readQuizJSON(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            Type listType = new TypeToken<ArrayList<Quiz<String>>>() {}.getType();
            List<Quiz<String>> loadedQuizz = gson.fromJson(reader, listType);
            return loadedQuizz != null ? loadedQuizz : new ArrayList<>();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

