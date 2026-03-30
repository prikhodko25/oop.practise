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
