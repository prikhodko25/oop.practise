package ex17;

import java.util.Scanner;

/**
 * Консольна команда зміни ширини таблиці
 * 
 * @author Student
 * @version 1.0
 */
public class WidthConsoleCommand implements ConsoleCommand {
    
    private View view;
    
    public WidthConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'w';
    }
    
    @Override
    public String getDescription() {
        return "змінити ширину таблиці";
    }
    
    @Override
    public String toString() {
        return "'w' - " + getDescription();
    }
    
    @Override
    public void execute() {
        if (!(view instanceof ViewTable)) {
            System.out.println("Функція доступна тільки в табличному режимі");
            return;
        }
        
        ViewTable viewTable = (ViewTable) view;
        System.out.print("Ширина (10-80): ");
        
        Scanner scanner = new Scanner(System.in);
        try {
            int newWidth = Integer.parseInt(scanner.nextLine());
            if (newWidth >= 10 && newWidth <= 80) {
                viewTable.setWidth(newWidth);
                System.out.println("Ширину змінено на " + newWidth);
                viewTable.viewShow();
            } else {
                System.out.println("Помилка: ширина має бути від 10 до 80");
            }
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть ціле число");
        }
    }
}
