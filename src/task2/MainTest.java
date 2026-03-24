package ex16;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import junit.framework.Assert;
import java.io.IOException;
import ex16.Calc;

/**
 * Виконує тестування розроблених класів.
 * 
 * @author Student
 * @version 1.0
 */
public class MainTest {

    /**
     * Перевірка основної функціональності класу {@linkplain Calc}
     * для обчислення координат тіла.
     */
    @Test
    public void testCalc() {
        Calc calc = new Calc();
        
        calc.init(10.0, 0.0, 2.0);
        assertEquals(20.0, calc.getResult().getX(), 1e-10);
        assertEquals(-19.62, calc.getResult().getY(), 1e-10);
        
        calc.init(10.0, 90.0, 1.0);
        assertEquals(0.0, calc.getResult().getX(), 1e-10);
        assertEquals(5.095, calc.getResult().getY(), 1e-10);
        
        calc.init(20.0, 45.0, 1.0);
        double expectedX = 20.0 * Math.cos(Math.toRadians(45));
        double expectedY = 20.0 * Math.sin(Math.toRadians(45)) - 4.905;
        assertEquals(expectedX, calc.getResult().getX(), 1e-10);
        assertEquals(expectedY, calc.getResult().getY(), 1e-10);
    }
    
    /**
     * Перевірка серіалізації. Коректність відновлення даних.
     */
    @Test
    public void testRestore() {
        Calc calc = new Calc();
        
        for (int ctr = 0; ctr < 100; ctr++) {
            double v0 = 10 + Math.random() * 90;
            double angle = Math.random() * 90;
            double time = Math.random() * 10;
            
            calc.init(v0, angle, time);
            double y = calc.getResult().getY();
            
            try {
                calc.save();
            } catch (IOException e) {
                Assert.fail(e.getMessage());
            }
            
            calc.init(Math.random() * 100, Math.random() * 90, Math.random() * 10);
            
            try {
                calc.restore();
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }
            
            assertEquals(v0, calc.getResult().getVelocity(), 1e-10);
            assertEquals(angle, calc.getResult().getAngle(), 1e-10);
            assertEquals(time, calc.getResult().getTime(), 1e-10);
            assertEquals(y, calc.getResult().getY(), 1e-10);
        }
    }
}
