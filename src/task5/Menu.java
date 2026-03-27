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
