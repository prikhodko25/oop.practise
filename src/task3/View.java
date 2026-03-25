package ex17;

import java.io.IOException;

/**
 * Product (шаблон проектування Factory Method)<br>
 * Інтерфейс "фабрикованих" об'єктів<br>
 * Об'являє методи відображення об'єктів
 * 
 * @author Student
 * @version 2.0
 */
public interface View {
    
    void viewHeader();
    void viewBody();
    void viewFooter();
    void viewShow();
    void viewInit();
    void viewSave() throws IOException;
    void viewRestore() throws Exception;
}
