package ex17;

import java.util.Stack;

/**
 * Формує та відображає меню
 * Реалізує шаблон Singleton
 * 
 * @author Student
 * @version 1.0
 */
public class Application {
    
    /** Посилання на єдиний екземпляр класу Application */
    private static Application instance = new Application();
    
    /** Закритий конструктор (шаблон Singleton) */
    private Application() {}
    
    /**
     * Повертає посилання на екземпляр класу Application
     * @return єдиний екземпляр
     */
    public static Application getInstance() {
        return instance;
    }
    
    /** Об'єкт для відображення результатів (табличне представлення) */
    private View view = new ViewableTable().getView();
    
    /** Макрокоманда Menu */
    private Menu menu = new Menu();
    
    /** Історія команд для undo */
    private Stack<Command> history = new Stack<>();
    
    /**
     * Обробка команд користувача
     */
    public void run() {
        // Додавання команд до меню
        menu.add(new ViewConsoleCommand(view));
        menu.add(new GenerateConsoleCommand(view));
        menu.add(new ChangeConsoleCommand(view));
        menu.add(new SaveConsoleCommand(view));
        menu.add(new RestoreConsoleCommand(view));
        menu.add(new WidthConsoleCommand(view));
        
        // Додавання команди undo
        UndoCommand undoCommand = new UndoCommand(history);
        menu.setUndoCommand(undoCommand);
        
        // Запуск меню
        menu.execute();
    }
}
