package ex17;

import java.util.Scanner;

/**
 * Консольна команда Change item
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class ChangeConsoleCommand extends ChangeItemCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Повертає об'єкт View
     * @return об'єкт View
     */
    public View getView() {
        return view;
    }
    
    /**
     * Встановлює об'єкт View
     * @param view об'єкт View
     * @return нове значення
     */
    public View setView(View view) {
        return this.view = view;
    }
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public ChangeConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'c';
    }
    
    @Override
    public String getDescription() {
        return "змінити (масштабувати Y)";
    }
    
    @Override
    public String toString() {
        return "'c' - " + getDescription();
    }
    
    @Override
    public void execute() {
        if (!(view instanceof ViewResult)) {
            System.out.println("Помилка: невірний тип View");
            return;
        }
        
        ViewResult viewResult = (ViewResult) view;
        
        System.out.print("Введіть коефіцієнт масштабування (0.1-10.0): ");
        Scanner scanner = new Scanner(System.in);
        
        try {
            double scale = Double.parseDouble(scanner.nextLine());
            if (scale < 0.1 || scale > 10.0) {
                System.out.println("Коефіцієнт має бути в межах 0.1-10.0");
                return;
            }
            
            setScale(scale);
            System.out.println("Масштабування Y з коефіцієнтом " + scale);
            
            for (Item2d item : viewResult.getItems()) {
                setItem(item);
                super.execute();
            }
            
            System.out.println("Готово.");
            view.viewShow();
            
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть число");
        }
    }
    
    @Override
    public void undo() {
        if (!(view instanceof ViewResult)) return;
        
        ViewResult viewResult = (ViewResult) view;
        
        for (Item2d item : viewResult.getItems()) {
            setItem(item);
            super.undo();
        }
        
        System.out.println("Скасовано останню зміну.");
        view.viewShow();
    }
}
