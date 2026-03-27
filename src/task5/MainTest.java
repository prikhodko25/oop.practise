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
