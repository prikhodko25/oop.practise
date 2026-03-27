package ex17;

/**
 * Консольна команда Restore
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class RestoreConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public RestoreConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'r';
    }
    
    @Override
    public String getDescription() {
        return "відновити з файлу";
    }
    
    @Override
    public String toString() {
        return "'r' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Відновлення даних...");
        try {
            view.viewRestore();
            System.out.println("Відновлено.");
            view.viewShow();
        } catch (Exception e) {
            System.out.println("Помилка: " + e);
        }
    }
}
