package ex17;

/**
 * Creator (шаблон проектування Factory Method)<br>
 * Об'являє метод, що "фабрикує" об'єкти
 * 
 * @author Student
 * @version 2.0
 * @see Viewable#getView()
 */
public interface Viewable {
    
    View getView();
}
