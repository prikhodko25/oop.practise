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
