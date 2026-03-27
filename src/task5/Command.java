package ex17;

/**
 * Інтерфейс команди або задачі
 * Шаблони: Command, Worker Thread
 * 
 * @author Student
 * @version 1.0
 */
public interface Command {
    /**
     * Виконання команди
     */
    void execute();
    
    /**
     * Скасування команди (для undo)
     */
    default void undo() {
        // за замовчуванням - пустий метод
    }
}
