package ex17;

/**
 * Представляє методи для поміщення та вилучення завдань
 * обробником потоку; шаблон Worker Thread
 * 
 * @author Student
 * @version 1.0
 */
public interface Queue {
    void put(Command cmd);
    Command take();
    default void shutdown() {}
}
