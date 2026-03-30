# Завдання 6
## Коди
### Queue.java
```java
package ex17;

/**
 * Представляє методи для поміщення та вилучення завдань
 * обробником потоку; шаблон Worker Thread
 * 
 * @author Student
 * @version 1.0
 */
public interface Queue {
    void put(Command cmd);
    Command take();
    default void shutdown() {}
}
```
### CommandQueue.java
```java
package ex17;

import java.util.Vector;

/**
 * Створює обробник потоку, що виконує об'єкти Command
 * Шаблон Worker Thread
 * 
 * @author Student
 * @version 1.0
 */
public class CommandQueue implements Queue {
    
    private Vector<Command> tasks;
    private boolean waiting;
    private boolean shutdown;
    
    public void shutdown() {
        shutdown = true;
    }
    
    public CommandQueue() {
        tasks = new Vector<>();
        waiting = false;
        new Thread(new Worker()).start();
    }
    
    @Override
    public void put(Command r) {
        tasks.add(r);
        if (waiting) {
            synchronized (this) {
                notifyAll();
            }
        }
    }
    
    @Override
    public Command take() {
        if (tasks.isEmpty()) {
            synchronized (this) {
                waiting = true;
                try {
                    wait();
                } catch (InterruptedException ie) {
                    waiting = false;
                }
            }
        }
        return tasks.remove(0);
    }
    
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (!shutdown) {
                Command r = take();
                r.execute();
            }
        }
    }
}
```
###  MaxCommand.java
```java
package ex17;

import java.util.concurrent.TimeUnit;

/**
 * Завдання для пошуку максимального значення Y
 * 
 * @author Student
 * @version 1.0
 */
public class MaxCommand implements Command {
    
    private int result = -1;
    private int progress = 0;
    private ViewResult viewResult;
    
    public MaxCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }
    
    public int getResult() { return result; }
    public boolean running() { return progress < 100; }
    
    @Override
    public void execute() {
        progress = 0;
        
        int size = viewResult.getItems().size();
        result = 0;
        
        for (int idx = 1; idx < size; idx++) {
            if (viewResult.getItems().get(result).getY() < 
                viewResult.getItems().get(idx).getY()) {
                result = idx;
            }
            progress = idx * 100 / size;
            
            try {
                TimeUnit.MILLISECONDS.sleep(3000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        
        progress = 100;
    }
}
```
### AvgCommand.java
```java
package ex17;

import java.util.concurrent.TimeUnit;

/**
 * Завдання для обчислення середнього значення Y
 * 
 * @author Student
 * @version 1.0
 */
public class AvgCommand implements Command {
    
    private double result = 0.0;
    private int progress = 0;
    private ViewResult viewResult;
    
    public AvgCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }
    
    public double getResult() { return result; }
    public boolean running() { return progress < 100; }
    
    @Override
    public void execute() {
        progress = 0;
        
        result = 0.0;
        int idx = 1;
        int size = viewResult.getItems().size();
        
        for (Item2d item : viewResult.getItems()) {
            result += item.getY();
            progress = idx * 100 / size;
            idx++;
            
            try {
                TimeUnit.MILLISECONDS.sleep(2000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        
        result /= size;
        progress = 100;
    }
}
```
### MinMaxCommand.java
```java
package ex17;

import java.util.concurrent.TimeUnit;

/**
 * Завдання для пошуку мінімального позитивного та максимального негативного значення Y
 * 
 * @author Student
 * @version 1.0
 */
public class MinMaxCommand implements Command {
    
    private int resultMin = -1;
    private int resultMax = -1;
    private int progress = 0;
    private ViewResult viewResult;
    
    public MinMaxCommand(ViewResult viewResult) {
        this.viewResult = viewResult;
    }
    
    public int getResultMin() { return resultMin; }
    public int getResultMax() { return resultMax; }
    public boolean running() { return progress < 100; }
    
    @Override
    public void execute() {
        progress = 0;
        
        int idx = 0;
        int size = viewResult.getItems().size();
        
        for (Item2d item : viewResult.getItems()) {
            if (item.getY() < 0) {
                if ((resultMax == -1) || 
                    (viewResult.getItems().get(resultMax).getY() < item.getY())) {
                    resultMax = idx;
                }
            } else {
                if ((resultMin == -1) || 
                    (viewResult.getItems().get(resultMin).getY() > item.getY())) {
                    resultMin = idx;
                }
            }
            idx++;
            progress = idx * 100 / size;
            
            try {
                TimeUnit.MILLISECONDS.sleep(5000 / size);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        
        progress = 100;
    }
}
```
### ExecuteConsoleCommand.java
```java
package ex17;

import java.util.concurrent.TimeUnit;

/**
 * Консольна команда Execute all threads
 * 
 * @author Student
 * @version 1.0
 */
public class ExecuteConsoleCommand implements Command {
    
    private ViewResult view;
    
    public ExecuteConsoleCommand(ViewResult view) {
        this.view = view;
    }
    
    @Override
    public void execute() {
        if (view.getItems().isEmpty()) {
            System.out.println("Дані відсутні.");
            return;
        }
        
        CommandQueue queue1 = new CommandQueue();
        CommandQueue queue2 = new CommandQueue();
        
        MaxCommand maxCommand = new MaxCommand(view);
        AvgCommand avgCommand = new AvgCommand(view);
        MinMaxCommand minMaxCommand = new MinMaxCommand(view);
        
        queue1.put(minMaxCommand);
        queue2.put(maxCommand);
        queue2.put(avgCommand);
        
        try {
            while (avgCommand.running() || maxCommand.running() || minMaxCommand.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            
            queue1.shutdown();
            queue2.shutdown();
            TimeUnit.SECONDS.sleep(1);
            
        } catch (InterruptedException e) {
            System.err.println(e);
        }
        
        System.out.println("\nРезультати обчислень:");
        System.out.println("  Максимальне Y: елемент #" + (maxCommand.getResult() + 1) + 
                         " (" + String.format("%.2f", view.getItems().get(maxCommand.getResult()).getY()) + ")");
        System.out.println("  Середнє Y: " + String.format("%.2f", avgCommand.getResult()));
        
        if (minMaxCommand.getResultMin() > -1) {
            System.out.println("  Мінімальне позитивне Y: елемент #" + (minMaxCommand.getResultMin() + 1) + 
                             " (" + String.format("%.2f", view.getItems().get(minMaxCommand.getResultMin()).getY()) + ")");
        } else {
            System.out.println("  Мінімальне позитивне Y: не знайдено");
        }
        
        if (minMaxCommand.getResultMax() > -1) {
            System.out.println("  Максимальне негативне Y: елемент #" + (minMaxCommand.getResultMax() + 1) + 
                             " (" + String.format("%.2f", view.getItems().get(minMaxCommand.getResultMax()).getY()) + ")");
        } else {
            System.out.println("  Максимальне негативне Y: не знайдено");
        }
        
        view.viewShow();
    }
}
```
### Main.java
```java
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
```
### MainTest.java
```java
package ex17;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Тестування розроблених класів
 * 
 * @author Student
 * @version 1.0
 */
public class MainTest {
    
    private static final int N = 100;
    private static ViewResult view = new ViewResult(N);
    private static MaxCommand max1 = new MaxCommand(view);
    private static MaxCommand max2 = new MaxCommand(view);
    private static AvgCommand avg1 = new AvgCommand(view);
    private static AvgCommand avg2 = new AvgCommand(view);
    private static MinMaxCommand min1 = new MinMaxCommand(view);
    private static MinMaxCommand min2 = new MinMaxCommand(view);
    private CommandQueue queue = new CommandQueue();
    
    @BeforeClass
    public static void setUpBeforeClass() {
        view.viewInit();
        assertEquals(N, view.getItems().size());
    }
    
    @AfterClass
    public static void tearDownAfterClass() {
        assertEquals(max1.getResult(), max2.getResult());
        assertEquals(avg1.getResult(), avg2.getResult(), 1e-10);
        assertEquals(min1.getResultMax(), min2.getResultMax());
        assertEquals(min1.getResultMin(), min2.getResultMin());
    }
    
    @Test
    public void testMax() {
        max1.execute();
        assertTrue(max1.getResult() > -1);
    }
    
    @Test
    public void testAvg() {
        avg1.execute();
        assertTrue(avg1.getResult() != 0.0);
    }
    
    @Test
    public void testMin() {
        min1.execute();
        assertTrue(min1.getResultMin() > -1 || min1.getResultMax() > -1);
    }
    
    @Test
    public void testMaxQueue() {
        queue.put(max2);
        try {
            while (max2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testAvgQueue() {
        queue.put(avg2);
        try {
            while (avg2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            fail(e.toString());
        }
    }
    
    @Test
    public void testMinQueue() {
        queue.put(min2);
        try {
            while (min2.running()) {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            queue.shutdown();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            fail(e.toString());
        }
    }
}
```

## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/tаsk6.1.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task6.2.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task6.3.png)
