# Завдання 4
## Коди
### ViewableTable.java
```java
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
```
### ViewTable.java
```java
package ex17;

import java.util.Formatter;

/**
 * ConcreteProduct (шаблон проектування Factory Method)<br>
 * Виведення результатів у вигляді текстової таблиці
 * 
 * @author Student
 * @version 3.0
 */
public class ViewTable extends ViewResult {
    
    private static final int DEFAULT_WIDTH = 30;
    private int width;

    public ViewTable() {
        width = DEFAULT_WIDTH;
    }

    public ViewTable(int width) {
        this.width = width;
    }

    public ViewTable(int width, int n) {
        super(n);
        this.width = width;
    }

    public int setWidth(int width) {
        return this.width = width;
    }

    public int getWidth() {
        return width;
    }

    private void outLine() {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
    }

    private void outLineLn() {
        outLine();
        System.out.println();
    }

    private void outHeader() {
        int colWidth = (width - 3) / 2;
        String format = "%" + colWidth + "s | %" + colWidth + "s%n";
        System.out.printf(format, "x (м)", "y (м)");
        outLineLn();
    }

    private void outBody() {
        int colWidth = (width - 3) / 2;
        String format = "%" + colWidth + ".2f | %" + colWidth + ".2f%n";
        
        for (Item2d item : getItems()) {
            System.out.printf(format, item.getX(), item.getY());
        }
    }

    public final void init(int width) {
        this.width = width;
        viewInit();
    }

    public final void init(int width, double stepX) {
        this.width = width;
        init(stepX);
    }

    @Override
    public void init(double stepX) {
        super.init(stepX);
    }

    @Override
    public void viewInit() {
        super.viewInit();
    }

    @Override
    public void viewHeader() {
        System.out.println();
        outHeader();
    }

    @Override
    public void viewBody() {
        if (getItems().isEmpty() || getItems().get(0).getVelocity() == 0) {
            System.out.println("Дані відсутні.");
            return;
        }
        outBody();
    }

    @Override
    public void viewFooter() {
        outLineLn();
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
 * Головний клас програми
 * 
 * @author Student
 * @version 3.0
 */
public class Main extends ex17.Main {
    
    public Main(View view) {
        super(view);
    }
    
    @Override
    protected void menu() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ViewTable viewTable = null;
        
        if (view instanceof ViewTable) {
            viewTable = (ViewTable) view;
        }
        
        do {
            System.out.println("\nВведіть команду:");
            System.out.println("q - вихід");
            System.out.println("v - переглянути результати");
            System.out.println("g - згенерувати дані");
            System.out.println("s - зберегти у файл");
            System.out.println("r - відновити з файлу");
            if (viewTable != null) {
                System.out.println("w - змінити ширину таблиці (поточна: " + viewTable.getWidth() + ")");
            }
            System.out.print("> ");
            
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
                        System.out.println("Збережено.");
                    } catch (IOException e) {
                        System.out.println("Помилка: " + e);
                    }
                    break;
                case 'r':
                    try {
                        view.viewRestore();
                        System.out.println("Відновлено.");
                    } catch (Exception e) {
                        System.out.println("Помилка: " + e);
                    }
                    view.viewShow();
                    break;
                case 'w':
                    if (viewTable != null) {
                        System.out.print("Ширина (10-80): ");
                        try {
                            String widthStr = in.readLine();
                            if (widthStr != null && !widthStr.isEmpty()) {
                                int newWidth = Integer.parseInt(widthStr);
                                if (newWidth >= 10 && newWidth <= 80) {
                                    viewTable.setWidth(newWidth);
                                    System.out.println("Ширину змінено на " + newWidth);
                                    viewTable.viewShow();
                                } else {
                                    System.out.println("Помилка: ширина має бути від 10 до 80");
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Помилка: введіть ціле число");
                        } catch (IOException e) {
                            System.out.println("Помилка: " + e);
                        }
                    }
                    break;
                default:
                    System.out.println("Невідома команда.");
            }
        } while (s != null && s.charAt(0) != 'q');
    }
    
    public static void main(String[] args) {
        Main main = new Main(new ViewableTable().getView());
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
import static org.junit.Assert.assertNotEquals;
import junit.framework.Assert;
import java.io.IOException;

/**
 * Тестування розроблених класів
 * 
 * @author Student
 * @version 3.0
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
    
    @Test
    public void testViewTable() {
        ViewTable viewTable = new ViewTable(25, 3);
        
        assertEquals(25, viewTable.getWidth());
        assertEquals(3, viewTable.getItems().size());
        
        viewTable.setWidth(40);
        assertEquals(40, viewTable.getWidth());
        
        viewTable.init(30);
        assertEquals(30, viewTable.getWidth());
        assertNotEquals(0, viewTable.getItems().get(0).getVelocity());
    }
    
    @Test
    public void testPolymorphism() {
        View view = new ViewTable();
        
        assertTrue(view instanceof ViewTable);
        assertTrue(view instanceof ViewResult);
        
        view.viewInit();
        view.viewShow();
        
        ViewTable viewTable = (ViewTable) view;
        viewTable.setWidth(50);
        assertEquals(50, viewTable.getWidth());
    }
}
```
## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/tаsk4.1.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task4.2.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task4.3.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task4.4.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task4.5.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task4.6.png)
