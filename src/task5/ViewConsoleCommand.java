package ex17;

/**
 * Консольна команда View
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class ViewConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public ViewConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'v';
    }
    
    @Override
    public String getDescription() {
        return "переглянути результати";
    }
    
    @Override
    public String toString() {
        return "'v' - " + getDescription();
    }
    
    @Override
    public void execute() {
        view.viewShow();
    }
}
