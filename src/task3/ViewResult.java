package ex17;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * ConcreteProduct (шаблон проектування Factory Method)<br>
 * Обчислення координат тіла, збереження та відображення результатів
 * 
 * @author Student
 * @version 2.0
 * @see View
 */
public class ViewResult implements View {
    
    private static final String FNAME = "items.bin";
    private static final int DEFAULT_NUM = 10;
    private ArrayList<Item2d> items = new ArrayList<Item2d>();

    public ViewResult() {
        this(DEFAULT_NUM);
    }

    public ViewResult(int n) {
        for (int ctr = 0; ctr < n; ctr++) {
            items.add(new Item2d());
        }
    }

    public ArrayList<Item2d> getItems() {
        return items;
    }

    private Item2d calc(double velocity, double angle, double time) {
        Item2d item = new Item2d();
        item.setVAT(velocity, angle, time);
        return item;
    }

    public void init(double[] velocities, double[] angles, double[] times) {
        int size = items.size();
        for (int i = 0; i < size && i < velocities.length; i++) {
            items.set(i, calc(velocities[i], angles[i], times[i]));
        }
    }

    @Override
    public void viewInit() {
        int size = items.size();
        double[] velocities = new double[size];
        double[] angles = new double[size];
        double[] times = new double[size];
        
        for (int i = 0; i < size; i++) {
            velocities[i] = 10 + Math.random() * 90;
            angles[i] = Math.random() * 90;
            times[i] = Math.random() * 10;
        }
        init(velocities, angles, times);
    }

    @Override
    public void viewSave() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME));
        os.writeObject(items);
        os.flush();
        os.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void viewRestore() throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME));
        items = (ArrayList<Item2d>) is.readObject();
        is.close();
    }

    @Override
    public void viewHeader() {
        System.out.println("Результати обчислень координат тіла:");
    }

    @Override
    public void viewBody() {
        int i = 0;
        for (Item2d item : items) {
            System.out.printf("%2d. %s%n", ++i, item);
        }
    }

    @Override
    public void viewFooter() {
        // пустий метод
    }

    @Override
    public void viewShow() {
        viewHeader();
        viewBody();
        viewFooter();
    }
}
