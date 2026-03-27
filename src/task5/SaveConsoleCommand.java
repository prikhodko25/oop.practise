package ex17;

import java.io.IOException;

/**
 * Консольна команда Save
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class SaveConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public SaveConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 's';
    }
    
    @Override
    public String getDescription() {
        return "зберегти у файл";
    }
    
    @Override
    public String toString() {
        return "'s' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Збереження даних...");
        try {
            view.viewSave();
            System.out.println("Збережено.");
        } catch (IOException e) {
            System.out.println("Помилка: " + e);
        }
    }
}
