package ex17;

/**
 * ConcreteCreator (шаблон проектування Factory Method)<br>
 * Розширює ViewableResult для створення об'єктів ViewTable
 * 
 * @author Student
 * @version 3.0
 */
public class ViewableTable extends ViewableResult {
    
    @Override
    public View getView() {
        return new ViewTable();
    }
}
