package ex16;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Обчислення та відображення результатів.
 * Містить реалізацію статичного методу main().
 * 
 * @author Student
 * @version 1.0
 * @see Main#main
 */
public class Main {

    /** Об'єкт класу {@linkplain Calc}.<br>Розв'язує задачу індивідуального завдання. */
    private Calc calc = new Calc();

    /**
     * Відображає меню.
     */
    private void menu() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.println("\n'q' - вихід");
            System.out.println("'v' - переглянути результат");
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
                    calc.show();
                    break;
                case 'g':
                    double v0 = 10 + Math.random() * 90;
                    double alpha = Math.random() * 90;
                    double t = Math.random() * 10;
                    calc.init(v0, alpha, t);
                    calc.show();
                    break;
                case 's':
                    try {
                        calc.save();
                    } catch (IOException e) {
                        System.out.println("Помилка: " + e);
                    }
                    calc.show();
                    break;
                case 'r':
                    try {
                        calc.restore();
                    } catch (Exception e) {
                        System.out.println("Помилка: " + e);
                    }
                    calc.show();
                    break;
                default:
                    System.out.println("Невідома команда.");
            }
        } while (s.charAt(0) != 'q');
    }

    /**
     * Виконується при запуску програми.
     * Обчислюються координати тіла для різних параметрів.
     * Результати обчислень виводяться на екран.
     * 
     * @param args - параметри запуску програми.
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.menu();
    }
}
