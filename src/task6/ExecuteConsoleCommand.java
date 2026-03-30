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
