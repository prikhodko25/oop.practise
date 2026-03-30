package ex17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Головний клас програми
 * 
 * @author Student
 * @version 1.0
 */
public class Main {
    
    private ViewResult view = new ViewResult(10);
    
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            System.out.println("\nВведіть команду:");
            System.out.println("'g' - генерувати дані");
            System.out.println("'v' - переглянути дані");
            System.out.println("'e' - обчислити (max, avg, minmax)");
            System.out.println("'q' - вихід");
            System.out.print("> ");
            
            try {
                String s = in.readLine();
                if (s == null || s.length() == 0) continue;
                
                char key = s.charAt(0);
                
                switch (key) {
                    case 'g':
                        view.viewInit();
                        view.viewShow();
                        break;
                    case 'v':
                        view.viewShow();
                        break;
                    case 'e':
                        ExecuteConsoleCommand cmd = new ExecuteConsoleCommand(view);
                        cmd.execute();
                        break;
                    case 'q':
                        System.out.println("Вихід.");
                        return;
                    default:
                        System.out.println("Невідома команда.");
                }
            } catch (IOException e) {
                System.out.println("Помилка: " + e);
            }
        }
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}
