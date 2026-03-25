# Завдання 3
## Коди
### Item2d.java
```java
package ex17;

import java.io.Serializable;

/**
 * Зберігає вихідні дані та результати обчислень координат тіла.
 * Реалізує інтерфейс {@link Serializable} для можливості серіалізації.
 * 
 * @author Student
 * @version 2.0
 */
public class Item2d implements Serializable {

    private static final long serialVersionUID = 1L;
    private double velocity;
    private double angle;
    private double time;
    private transient double x;
    private double y;
    private static final double G = 9.81;

    public Item2d() {
        velocity = 0.0;
        angle = 0.0;
        time = 0.0;
        x = 0.0;
        y = 0.0;
    }

    public Item2d(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        this.x = 0.0;
        this.y = 0.0;
    }

    public double getVelocity() { return velocity; }
    public double setVelocity(double velocity) { return this.velocity = velocity; }
    public double getAngle() { return angle; }
    public double setAngle(double angle) { return this.angle = angle; }
    public double getTime() { return time; }
    public double setTime(double time) { return this.time = time; }
    public double getX() { return x; }
    public double setX(double x) { return this.x = x; }
    public double getY() { return y; }
    public double setY(double y) { return this.y = y; }
    public static double getG() { return G; }

    public void calculateCoordinates() {
        double angleRad = Math.toRadians(angle);
        this.x = velocity * Math.cos(angleRad) * time;
        this.y = velocity * Math.sin(angleRad) * time - (G * time * time) / 2;
    }

    public Item2d setVAT(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        calculateCoordinates();
        return this;
    }

    @Override
    public String toString() {
        return String.format("v0=%.1f м/с, α=%.1f°, t=%.1f с → x=%.2f м, y=%.2f м", 
                velocity, angle, time, x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Item2d other = (Item2d) obj;
        if (Double.doubleToLongBits(velocity) != Double.doubleToLongBits(other.velocity)) return false;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) return false;
        if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time)) return false;
        if (Math.abs(x - other.x) > .1e-10) return false;
        if (Math.abs(y - other.y) > .1e-10) return false;
        return true;
    }
}
```
### View.java
```java
package ex17;

import java.io.IOException;

/**
 * Product (шаблон проектування Factory Method)<br>
 * Інтерфейс "фабрикованих" об'єктів<br>
 * Об'являє методи відображення об'єктів
 * 
 * @author Student
 * @version 2.0
 */
public interface View {
    
    void viewHeader();
    void viewBody();
    void viewFooter();
    void viewShow();
    void viewInit();
    void viewSave() throws IOException;
    void viewRestore() throws Exception;
}
```
### Viewable.java
```java
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
```
### ViewableResult.java
```java
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
```
### ViewResult.java
```java
package ex17;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * ConcreteProduct (шаблон проектування Factory Method)<br>
 * Обчислення координат тіла, збереження та відображення результатів
 * 
 * @author Student
 * @version 2.0
 * @see View
 */
public class ViewResult implements View {
    
    private static final String FNAME = "items.bin";
    private static final int DEFAULT_NUM = 10;
    private ArrayList<Item2d> items = new ArrayList<Item2d>();

    public ViewResult() {
        this(DEFAULT_NUM);
    }

    public ViewResult(int n) {
        for (int ctr = 0; ctr < n; ctr++) {
            items.add(new Item2d());
        }
    }

    public ArrayList<Item2d> getItems() {
        return items;
    }

    private Item2d calc(double velocity, double angle, double time) {
        Item2d item = new Item2d();
        item.setVAT(velocity, angle, time);
        return item;
    }

    public void init(double[] velocities, double[] angles, double[] times) {
        int size = items.size();
        for (int i = 0; i < size && i < velocities.length; i++) {
            items.set(i, calc(velocities[i], angles[i], times[i]));
        }
    }

    @Override
    public void viewInit() {
        int size = items.size();
        double[] velocities = new double[size];
        double[] angles = new double[size];
        double[] times = new double[size];
        
        for (int i = 0; i < size; i++) {
            velocities[i] = 10 + Math.random() * 90;
            angles[i] = Math.random() * 90;
            times[i] = Math.random() * 10;
        }
        init(velocities, angles, times);
    }

    @Override
    public void viewSave() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME));
        os.writeObject(items);
        os.flush();
        os.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void viewRestore() throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME));
        items = (ArrayList<Item2d>) is.readObject();
        is.close();
    }

    @Override
    public void viewHeader() {
        System.out.println("Результати обчислень координат тіла:");
    }

    @Override
    public void viewBody() {
        int i = 0;
        for (Item2d item : items) {
            System.out.printf("%2d. %s%n", ++i, item);
        }
    }

    @Override
    public void viewFooter() {
        // пустий метод
    }

    @Override
    public void viewShow() {
        viewHeader();
        viewBody();
        viewFooter();
    }
}
```
### Main.java
```java
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
```
### MainTest.java
```java
package ex17;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.Assert;
import java.io.IOException;

/**
 * Виконує тестування розроблених класів.
 * 
 * @author Student
 * @version 2.0
 */
public class MainTest {
    
    @Test
    public void testCalc() {
        ViewResult view = new ViewResult(5);
        
        double[] velocities = {10.0, 10.0, 20.0, 20.0, 30.0};
        double[] angles = {0.0, 90.0, 45.0, 30.0, 60.0};
        double[] times = {2.0, 1.0, 1.0, 1.5, 0.5};
        
        view.init(velocities, angles, times);
        
        assertEquals(20.0, view.getItems().get(0).getX(), 1e-10);
        assertEquals(-19.62, view.getItems().get(0).getY(), 1e-10);
        
        assertEquals(0.0, view.getItems().get(1).getX(), 1e-10);
        assertEquals(5.095, view.getItems().get(1).getY(), 1e-10);
        
        double expectedX = 20.0 * Math.cos(Math.toRadians(45));
        double expectedY = 20.0 * Math.sin(Math.toRadians(45)) - 4.905;
        assertEquals(expectedX, view.getItems().get(2).getX(), 1e-10);
        assertEquals(expectedY, view.getItems().get(2).getY(), 1e-10);
    }
    
    @Test
    public void testRestore() {
        ViewResult view1 = new ViewResult(100);
        ViewResult view2 = new ViewResult();
        
        view1.viewInit();
        
        try {
            view1.viewSave();
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        
        try {
            view2.viewRestore();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        
        assertEquals(view1.getItems().size(), view2.getItems().size());
        assertTrue("containsAll()", view1.getItems().containsAll(view2.getItems()));
        
        assertEquals(view1.getItems().get(0).getVelocity(), 
                view2.getItems().get(0).getVelocity(), 1e-10);
        assertEquals(view1.getItems().get(0).getAngle(), 
                view2.getItems().get(0).getAngle(), 1e-10);
        assertEquals(view1.getItems().get(0).getTime(), 
                view2.getItems().get(0).getTime(), 1e-10);
        assertEquals(view1.getItems().get(0).getX(), 
                view2.getItems().get(0).getX(), 1e-10);
        assertEquals(view1.getItems().get(0).getY(), 
                view2.getItems().get(0).getY(), 1e-10);
    }
}
```
## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/tаsk3.1.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task3.2.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task3.3.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task3.4.png)
