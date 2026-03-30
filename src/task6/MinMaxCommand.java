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
