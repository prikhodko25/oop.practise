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
