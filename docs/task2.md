# Завдання 2
## Коди
### Item2d.java
```java
package ex16;

import java.io.Serializable;

/**
 * Зберігає вихідні дані та результати обчислень координат тіла.
 * Реалізує інтерфейс {@link Serializable} для можливості серіалізації.
 * 
 * @author Student
 * @version 1.0
 */
public class Item2d implements Serializable {

    /** Автоматично згенерована константа. */
    private static final long serialVersionUID = 1L;

    /** Початкова швидкість (м/с). */
    private double velocity;

    /** Кут до горизонту (градуси). */
    private double angle;

    /** Час польоту (с). */
    private double time;

    /** 
     * Координата X (горизонтальна).
     * Поле позначено як transient, щоб воно не серіалізувалося стандартним способом.
     * При десеріалізації отримає значення за замовчуванням (0.0).
     */
    private transient double x;

    /** Координата Y (вертикальна). */
    private double y;

    /** Прискорення вільного падіння (м/с²). */
    private static final double G = 9.81;

    /**
     * Конструктор за замовчуванням. Ініціалізує поля нульовими значеннями.
     */
    public Item2d() {
        velocity = 0.0;
        angle = 0.0;
        time = 0.0;
        x = 0.0;
        y = 0.0;
    }

    /**
     * Конструктор з параметрами.
     * 
     * @param velocity початкова швидкість (м/с)
     * @param angle    кут до горизонту (градуси)
     * @param time     час польоту (с)
     */
    public Item2d(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Повертає початкову швидкість.
     * 
     * @return початкова швидкість (м/с)
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Встановлює початкову швидкість.
     * 
     * @param velocity нове значення початкової швидкості
     * @return встановлене значення швидкості
     */
    public double setVelocity(double velocity) {
        return this.velocity = velocity;
    }

    /**
     * Повертає кут до горизонту.
     * 
     * @return кут (градуси)
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Встановлює кут до горизонту.
     * 
     * @param angle нове значення кута (градуси)
     * @return встановлене значення кута
     */
    public double setAngle(double angle) {
        return this.angle = angle;
    }

    /**
     * Повертає час польоту.
     * 
     * @return час (с)
     */
    public double getTime() {
        return time;
    }

    /**
     * Встановлює час польоту.
     * 
     * @param time нове значення часу (с)
     * @return встановлене значення часу
     */
    public double setTime(double time) {
        return this.time = time;
    }

    /**
     * Повертає координату X.
     * 
     * @return координата X (м)
     */
    public double getX() {
        return x;
    }

    /**
     * Встановлює координату X (використовується при обчисленнях).
     * 
     * @param x нове значення координати X (м)
     * @return встановлене значення X
     */
    public double setX(double x) {
        return this.x = x;
    }

    /**
     * Повертає координату Y.
     * 
     * @return координата Y (м)
     */
    public double getY() {
        return y;
    }

    /**
     * Встановлює координату Y.
     * 
     * @param y нове значення координати Y (м)
     * @return встановлене значення Y
     */
    public double setY(double y) {
        return this.y = y;
    }

    /**
     * Повертає прискорення вільного падіння (константа).
     * 
     * @return значення g = 9.81 м/с²
     */
    public static double getG() {
        return G;
    }

    /**
     * Обчислює координати X та Y за формулами руху тіла під дією сили тяжіння.
     * X = v0 * cos(α) * t
     * Y = v0 * sin(α) * t - (g * t²) / 2
     */
    public void calculateCoordinates() {
        double angleRad = Math.toRadians(angle);
        this.x = velocity * Math.cos(angleRad) * time;
        this.y = velocity * Math.sin(angleRad) * time - (G * time * time) / 2;
    }

    /**
     * Встановлює значення полів: швидкості, кута, часу
     * та обчислює координати.
     * 
     * @param velocity значення для ініціалізації поля {@link Item2d#velocity}
     * @param angle    значення для ініціалізації поля {@link Item2d#angle}
     * @param time     значення для ініціалізації поля {@link Item2d#time}
     * @return this
     */
    public Item2d setVAT(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        calculateCoordinates();
        return this;
    }

    /**
     * Представляє результат обчислень у вигляді рядка
     * з шістнадцятковим представленням цілої частини координат.
     * <br>{@inheritDoc}
     */
    @Override
    public String toString() {
        int intX = (int) x;
        int intY = (int) y;
        String hexX = Integer.toHexString(intX).toUpperCase();
        String hexY = Integer.toHexString(intY).toUpperCase();
        return String.format("x = %.2f (0x%s), y = %.2f (0x%s)", x, hexX, y, hexY);
    }

    /**
     * Автоматично згенерований метод.<br>{@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item2d other = (Item2d) obj;
        if (Double.doubleToLongBits(velocity) != Double.doubleToLongBits(other.velocity))
            return false;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle))
            return false;
        if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
            return false;
        if (Math.abs(x - other.x) > .1e-10)
            return false;
        if (Math.abs(y - other.y) > .1e-10)
            return false;
        return true;
    }
}
```
### Calc.java
```java
package ex16;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Містить реалізацію методів для обчислення координат фізичного тіла.
 * 
 * @author Student
 * @version 1.0
 */
public class Calc {

    /** Ім'я файлу, що використовується при серіалізації. */
    private static final String FNAME = "Item2d.bin";

    /** Зберігає результат обчислень. Об'єкт класу {@linkplain Item2d} */
    private Item2d result;

    /**
     * Ініціалізує {@link Calc#result}
     */
    public Calc() {
        result = new Item2d();
    }

    /**
     * Встановити значення {@link Calc#result}
     * 
     * @param result - нове значення посилання на об'єкт {@linkplain Item2d}
     */
    public void setResult(Item2d result) {
        this.result = result;
    }

    /**
     * Отримати значення {@link Calc#result}
     * 
     * @return поточне значення посилання на об'єкт {@linkplain Item2d}
     */
    public Item2d getResult() {
        return result;
    }

    /**
     * Обчислює координати X та Y за формулами:
     * X = v0 * cos(α) * t
     * Y = v0 * sin(α) * t - (g * t²) / 2
     * 
     * @param velocity початкова швидкість (м/с)
     * @param angle    кут до горизонту (градуси)
     * @param time     час польоту (с)
     * @return результат обчислень {@link Item2d}
     */
    public Item2d init(double velocity, double angle, double time) {
        result.setVAT(velocity, angle, time);
        return result;
    }

    /**
     * Виводить результат обчислень.
     */
    public void show() {
        System.out.println(result);
    }

    /**
     * Зберігає {@link Calc#result} у файлі {@link Calc#FNAME}
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME));
        os.writeObject(result);
        os.flush();
        os.close();
        System.out.println("Стан збережено у файл " + FNAME);
    }

    /**
     * Відновлює {@link Calc#result} з файлу {@link Calc#FNAME}
     * 
     * @throws Exception
     */
    public void restore() throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME));
        result = (Item2d) is.readObject();
        is.close();
        System.out.println("Стан відновлено з файлу " + FNAME);
    }
}
```
### Main.java
```java
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
```
### MainTest.java
```java
package ex16;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;
import java.io.IOException;
import ex16.Calc;

/**
 * Виконує тестування розроблених класів.
 * 
 * @author Student
 * @version 1.0
 */
public class MainTest {

    /**
     * Перевірка основної функціональності класу {@linkplain Calc}
     * для обчислення координат тіла.
     */
    @Test
    public void testCalc() {
        Calc calc = new Calc();
        
        calc.init(10.0, 0.0, 2.0);
        assertEquals(20.0, calc.getResult().getX(), 1e-10);
        assertEquals(-19.62, calc.getResult().getY(), 1e-10);
        
        calc.init(10.0, 90.0, 1.0);
        assertEquals(0.0, calc.getResult().getX(), 1e-10);
        assertEquals(5.095, calc.getResult().getY(), 1e-10);
        
        calc.init(20.0, 45.0, 1.0);
        double expectedX = 20.0 * Math.cos(Math.toRadians(45));
        double expectedY = 20.0 * Math.sin(Math.toRadians(45)) - 4.905;
        assertEquals(expectedX, calc.getResult().getX(), 1e-10);
        assertEquals(expectedY, calc.getResult().getY(), 1e-10);
    }
    
    /**
     * Перевірка серіалізації. Коректність відновлення даних.
     */
    @Test
    public void testRestore() {
        Calc calc = new Calc();
        
        for (int ctr = 0; ctr < 100; ctr++) {
            double v0 = 10 + Math.random() * 90;
            double angle = Math.random() * 90;
            double time = Math.random() * 10;
            
            calc.init(v0, angle, time);
            double y = calc.getResult().getY();
            
            try {
                calc.save();
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }
            
            calc.init(Math.random() * 100, Math.random() * 90, Math.random() * 10);
            
            try {
                calc.restore();
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }
            
            assertEquals(v0, calc.getResult().getVelocity(), 1e-10);
            assertEquals(angle, calc.getResult().getAngle(), 1e-10);
            assertEquals(time, calc.getResult().getTime(), 1e-10);
            assertEquals(y, calc.getResult().getY(), 1e-10);
        }
    }
}
```
## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task2.1.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task2.2.png)
