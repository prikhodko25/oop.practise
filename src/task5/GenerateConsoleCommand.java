package ex17; 

/**
 * Консольна команда Generate
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class GenerateConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public GenerateConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'g';
    }
    
    @Override
    public String getDescription() {
        return "генерувати випадкові дані";
    }
    
    @Override
    public String toString() {
        return "'g' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Генерація випадкових даних...");
        view.viewInit();
        view.viewShow();
    }
    
    @Override
    public void undo() {
        System.out.println("Неможливо скасувати генерацію.");
    }
}
