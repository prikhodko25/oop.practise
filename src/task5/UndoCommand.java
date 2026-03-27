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
