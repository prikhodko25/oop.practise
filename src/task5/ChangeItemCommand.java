package ex17;

/**
 * Команда для зміни елемента (масштабування)
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class ChangeItemCommand implements Command {
    
    /** Оброблюваний об'єкт */
    private Item2d item;
    
    /** Параметр команди (коефіцієнт масштабування) */
    private double scale;
    
    /** Збережене попереднє значення для undo */
    private double previousY;
    
    /**
     * Встановлює об'єкт для обробки
     * @param item об'єкт Item2d
     * @return нове значення
     */
    public Item2d setItem(Item2d item) {
        return this.item = item;
    }
    
    /**
     * Повертає об'єкт, що обробляється
     * @return об'єкт Item2d
     */
    public Item2d getItem() {
        return item;
    }
    
    /**
     * Встановлює коефіцієнт масштабування
     * @param scale коефіцієнт масштабування
     * @return нове значення
     */
    public double setScale(double scale) {
        return this.scale = scale;
    }
    
    /**
     * Повертає коефіцієнт масштабування
     * @return коефіцієнт масштабування
     */
    public double getScale() {
        return scale;
    }
    
    @Override
    public void execute() {
        if (item != null) {
            previousY = item.getY();
            item.setY(item.getY() * scale);
        }
    }
    
    @Override
    public void undo() {
        if (item != null) {
            item.setY(previousY);
        }
    }
}
