package ex17;

/**
 * Інтерфейс консольної команди
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public interface ConsoleCommand extends Command {
    /**
     * Гаряча клавіша команди
     * @return символ гарячої клавіші
     */
    char getKey();
    
    /**
     * Опис команди
     * @return рядок з описом
     */
    String getDescription();
}
