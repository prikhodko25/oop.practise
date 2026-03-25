package ex17;

/**
 * ConcreteCreator (шаблон проектування Factory Method)<br>
 * Реалізує метод, що "фабрикує" об'єкти
 * 
 * @author Student
 * @version 2.0
 * @see Viewable
 * @see ViewableResult#getView()
 */
public class ViewableResult implements Viewable {
    
    @Override
    public View getView() {
        return new ViewResult();
    }
}
