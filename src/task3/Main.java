package ex17;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Обчислення та відображення результатів<br>
 * Містить реалізацію статичного методу main()
 * 
 * @author Student
 * @version 2.0
 * @see Main#main
 */
public class Main {
    
    private View view;

    public Main(View view) {
        this.view = view;
    }

    protected void menu() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        do {
            System.out.println("\nВведіть команду:");
            System.out.println("'q' - вихід");
            System.out.println("'v' - переглянути поточні результати");
            System.out.println("'g' - згенерувати випадкові дані");
            System.out.println("'s' - зберегти стан у файл");
            System.out.println("'r' - відновити стан з файлу");
            System.out.print("Ваш вибір: ");
            
            try {
                s = in.readLine();
            } catch (IOException e) {
                System.out.println("Помилка: " + e);
                System.exit(0);
            }
            
            if (s == null || s.length() == 0) continue;
            
            switch (s.charAt(0)) {
                case 'q':
                    System.out.println("Вихід.");
                    break;
                case 'v':
                    view.viewShow();
                    break;
                case 'g':
                    view.viewInit();
                    view.viewShow();
                    break;
                case 's':
                    try {
                        view.viewSave();
                    } catch (IOException e) {
                        System.out.println("Помилка серіалізації: " + e);
                    }
                    view.viewShow();
                    break;
                case 'r':
                    try {
                        view.viewRestore();
                    } catch (Exception e) {
                        System.out.println("Помилка десеріалізації: " + e);
                    }
                    view.viewShow();
                    break;
                default:
                    System.out.println("Невідома команда.");
            }
        } while (s.charAt(0) != 'q');
    }

    public static void main(String[] args) {
        Main main = new Main(new ViewableResult().getView());
        main.menu();
    }
}
