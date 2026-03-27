# Завдання 5
## Коди
### Command.java
```java
package ex17;

/**
 * Інтерфейс команди або задачі
 * Шаблони: Command, Worker Thread
 * 
 * @author Student
 * @version 1.0
 */
public interface Command {
    /**
     * Виконання команди
     */
    void execute();
    
    /**
     * Скасування команди (для undo)
     */
    default void undo() {
        // за замовчуванням - пустий метод
    }
}
```
### ConsoleCommand.java
```java
package ex17;

/**
 * Інтерфейс консольної команди
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public interface ConsoleCommand extends Command {
    /**
     * Гаряча клавіша команди
     * @return символ гарячої клавіші
     */
    char getKey();
    
    /**
     * Опис команди
     * @return рядок з описом
     */
    String getDescription();
}
```
### ChangeItemCommand.java
```java
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
```
### ChangeConsoleCommand.java
```java
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
```
### GenerateConsoleCommand.java
```java
package ex17;

/**
 * Консольна команда Generate
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class GenerateConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public GenerateConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'g';
    }
    
    @Override
    public String getDescription() {
        return "генерувати випадкові дані";
    }
    
    @Override
    public String toString() {
        return "'g' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Генерація випадкових даних...");
        view.viewInit();
        view.viewShow();
    }
    
    @Override
    public void undo() {
        System.out.println("Неможливо скасувати генерацію.");
    }
}
```
### ViewConsoleCommand.java
```java
package ex17;

/**
 * Консольна команда View
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class ViewConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public ViewConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'v';
    }
    
    @Override
    public String getDescription() {
        return "переглянути результати";
    }
    
    @Override
    public String toString() {
        return "'v' - " + getDescription();
    }
    
    @Override
    public void execute() {
        view.viewShow();
    }
}
```
### SaveConsoleCommand.java
```java
package ex17;

import java.io.IOException;

/**
 * Консольна команда Save
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class SaveConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public SaveConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 's';
    }
    
    @Override
    public String getDescription() {
        return "зберегти у файл";
    }
    
    @Override
    public String toString() {
        return "'s' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Збереження даних...");
        try {
            view.viewSave();
            System.out.println("Збережено.");
        } catch (IOException e) {
            System.out.println("Помилка: " + e);
        }
    }
}
```
### RestoreConsoleCommand.java
```java
package ex17;

/**
 * Консольна команда Restore
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class RestoreConsoleCommand implements ConsoleCommand {
    
    /** Об'єкт для відображення результатів */
    private View view;
    
    /**
     * Ініціалізує команду
     * @param view об'єкт View
     */
    public RestoreConsoleCommand(View view) {
        this.view = view;
    }
    
    @Override
    public char getKey() {
        return 'r';
    }
    
    @Override
    public String getDescription() {
        return "відновити з файлу";
    }
    
    @Override
    public String toString() {
        return "'r' - " + getDescription();
    }
    
    @Override
    public void execute() {
        System.out.println("Відновлення даних...");
        try {
            view.viewRestore();
            System.out.println("Відновлено.");
            view.viewShow();
        } catch (Exception e) {
            System.out.println("Помилка: " + e);
        }
    }
}
```
### WidthConsoleCommand.java
```java
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
```
### UndoCommand.java
```java
package ex17;

import java.util.Stack;

/**
 * Команда для скасування останньої операції
 * Шаблон Command
 * 
 * @author Student
 * @version 1.0
 */
public class UndoCommand implements ConsoleCommand {
    
    private Stack<Command> history;
    
    public UndoCommand(Stack<Command> history) {
        this.history = history;
    }
    
    @Override
    public char getKey() {
        return 'u';
    }
    
    @Override
    public String getDescription() {
        return "скасувати останню операцію";
    }
    
    @Override
    public String toString() {
        return "'u' - " + getDescription();
    }
    
    @Override
    public void execute() {
        if (!history.isEmpty()) {
            Command lastCommand = history.pop();
            lastCommand.undo();
            System.out.println("Операцію скасовано.");
        } else {
            System.out.println("Немає операцій для скасування.");
        }
    }
}
```
### Menu.java
```java
package ex17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Макрокоманда (шаблон Command)
 * Колекція об'єктів ConsoleCommand
 * 
 * @author Student
 * @version 1.0
 */
public class Menu implements Command {
    
    /** Колекція консольних команд */
    private List<ConsoleCommand> menu = new ArrayList<>();
    
    /** Історія команд для undo */
    private Stack<Command> history = new Stack<>();
    
    /** Команда undo */
    private UndoCommand undoCommand;
    
    /**
     * Додає нову команду в колекцію
     * @param command реалізує ConsoleCommand
     * @return command
     */
    public ConsoleCommand add(ConsoleCommand command) {
        menu.add(command);
        return command;
    }
    
    /**
     * Встановлює команду undo
     * @param undoCommand команда undo
     */
    public void setUndoCommand(UndoCommand undoCommand) {
        this.undoCommand = undoCommand;
        menu.add(undoCommand);
    }
    
    /**
     * Додає команду в історію
     * @param command команда для збереження
     */
    public void addToHistory(Command command) {
        history.push(command);
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\nВведіть команду:\n");
        for (ConsoleCommand c : menu) {
            s.append(c.toString()).append("\n");
        }
        s.append("'q' - вихід\n> ");
        return s.toString();
    }
    
    @Override
    public void execute() {
        String s = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            System.out.print(this);
            
            try {
                s = in.readLine();
            } catch (IOException e) {
                System.out.println("Помилка: " + e);
                System.exit(0);
            }
            
            if (s == null || s.length() == 0) continue;
            
            char key = s.charAt(0);
            
            if (key == 'q') {
                System.out.println("Вихід.");
                break;
            }
            
            boolean found = false;
            for (ConsoleCommand c : menu) {
                if (key == c.getKey()) {
                    c.execute();
                    if (!(c instanceof UndoCommand) && !(c instanceof ViewConsoleCommand)) {
                        history.push(c);
                    }
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                System.out.println("Невідома команда.");
            }
        }
    }
}
```
### Application.java
```java
package ex17;

import java.util.Stack;

/**
 * Формує та відображає меню
 * Реалізує шаблон Singleton
 * 
 * @author Student
 * @version 1.0
 */
public class Application {
    
    /** Посилання на єдиний екземпляр класу Application */
    private static Application instance = new Application();
    
    /** Закритий конструктор (шаблон Singleton) */
    private Application() {}
    
    /**
     * Повертає посилання на екземпляр класу Application
     * @return єдиний екземпляр
     */
    public static Application getInstance() {
        return instance;
    }
    
    /** Об'єкт для відображення результатів (табличне представлення) */
    private View view = new ViewableTable().getView();
    
    /** Макрокоманда Menu */
    private Menu menu = new Menu();
    
    /** Історія команд для undo */
    private Stack<Command> history = new Stack<>();
    
    /**
     * Обробка команд користувача
     */
    public void run() {
        // Додавання команд до меню
        menu.add(new ViewConsoleCommand(view));
        menu.add(new GenerateConsoleCommand(view));
        menu.add(new ChangeConsoleCommand(view));
        menu.add(new SaveConsoleCommand(view));
        menu.add(new RestoreConsoleCommand(view));
        menu.add(new WidthConsoleCommand(view));
        
        // Додавання команди undo
        UndoCommand undoCommand = new UndoCommand(history);
        menu.setUndoCommand(undoCommand);
        
        // Запуск меню
        menu.execute();
    }
}
```
### Main.java
```java
package ex17;

/**
 * Головний клас програми
 * 
 * @author Student
 * @version 4.0
 */
public class Main {
    
    /**
     * Точка входу в програму
     * @param args параметри запуску
     */
    public static void main(String[] args) {
        Application app = Application.getInstance();
        app.run();
    }
}
```
### MainTest.java
```java
package ex17;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.Assert;
import java.io.IOException;

/**
 * Тестування розроблених класів
 * 
 * @author Student
 * @version 4.0
 */
public class MainTest {
    
    /**
     * Перевірка методу ChangeItemCommand.execute()
     */
    @Test
    public void testExecute() {
        ChangeItemCommand cmd = new ChangeItemCommand();
        cmd.setItem(new Item2d());
        
        double x, y, scale;
        for (int ctr = 0; ctr < 100; ctr++) {
            x = Math.random() * 100.0;
            y = Math.random() * 100.0;
            scale = Math.random() * 10.0;
            
            cmd.getItem().setX(x);
            cmd.getItem().setY(y);
            cmd.setScale(scale);
            cmd.execute();
            
            assertEquals(x, cmd.getItem().getX(), 1e-10);
            assertEquals(y * scale, cmd.getItem().getY(), 1e-10);
        }
    }
    
    /**
     * Перевірка методу undo
     */
    @Test
    public void testUndo() {
        ChangeItemCommand cmd = new ChangeItemCommand();
        Item2d item = new Item2d();
        item.setX(10.0);
        item.setY(20.0);
        cmd.setItem(item);
        cmd.setScale(2.0);
        
        cmd.execute();
        assertEquals(40.0, item.getY(), 1e-10);
        
        cmd.undo();
        assertEquals(20.0, item.getY(), 1e-10);
    }
    
    /**
     * Перевірка класу ChangeConsoleCommand
     */
    @Test
    public void testChangeConsoleCommand() {
        ChangeConsoleCommand cmd = new ChangeConsoleCommand(new ViewResult());
        cmd.getView().viewInit();
        
        assertEquals('c', cmd.getKey());
        assertTrue(cmd.toString().contains("c"));
    }
    
    /**
     * Перевірка класу Application (Singleton)
     */
    @Test
    public void testSingleton() {
        Application app1 = Application.getInstance();
        Application app2 = Application.getInstance();
        
        assertSame(app1, app2);
    }
    
    /**
     * Перевірка поліморфізму команд
     */
    @Test
    public void testCommandPolymorphism() {
        View view = new ViewResult();
        
        ConsoleCommand[] commands = {
            new ViewConsoleCommand(view),
            new GenerateConsoleCommand(view),
            new ChangeConsoleCommand(view),
            new SaveConsoleCommand(view),
            new RestoreConsoleCommand(view)
        };
        
        for (ConsoleCommand cmd : commands) {
            assertNotNull(cmd.getKey());
            assertNotNull(cmd.getDescription());
            assertTrue(cmd instanceof Command);
        }
    }
}
```


## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/tаsk5.1.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task5.2.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task5.3.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task5.4.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task5.5.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/task5.6.png)


