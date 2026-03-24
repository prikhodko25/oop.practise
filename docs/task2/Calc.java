package ex16;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Містить реалізацію методів для обчислення координат фізичного тіла.
 * 
 * @author Student
 * @version 1.0
 */
public class Calc {

    /** Ім'я файлу, що використовується при серіалізації. */
    private static final String FNAME = "Item2d.bin";

    /** Зберігає результат обчислень. Об'єкт класу {@linkplain Item2d} */
    private Item2d result;

    /**
     * Ініціалізує {@link Calc#result}
     */
    public Calc() {
        result = new Item2d();
    }

    /**
     * Встановити значення {@link Calc#result}
     * 
     * @param result - нове значення посилання на об'єкт {@linkplain Item2d}
     */
    public void setResult(Item2d result) {
        this.result = result;
    }

    /**
     * Отримати значення {@link Calc#result}
     * 
     * @return поточне значення посилання на об'єкт {@linkplain Item2d}
     */
    public Item2d getResult() {
        return result;
    }

    /**
     * Обчислює координати X та Y за формулами:
     * X = v0 * cos(α) * t
     * Y = v0 * sin(α) * t - (g * t²) / 2
     * 
     * @param velocity початкова швидкість (м/с)
     * @param angle    кут до горизонту (градуси)
     * @param time     час польоту (с)
     * @return результат обчислень {@link Item2d}
     */
    public Item2d init(double velocity, double angle, double time) {
        result.setVAT(velocity, angle, time);
        return result;
    }

    /**
     * Виводить результат обчислень.
     */
    public void show() {
        System.out.println(result);
    }

    /**
     * Зберігає {@link Calc#result} у файлі {@link Calc#FNAME}
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FNAME));
        os.writeObject(result);
        os.flush();
        os.close();
        System.out.println("Стан збережено у файл " + FNAME);
    }

    /**
     * Відновлює {@link Calc#result} з файлу {@link Calc#FNAME}
     * 
     * @throws Exception
     */
    public void restore() throws Exception {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(FNAME));
        result = (Item2d) is.readObject();
        is.close();
        System.out.println("Стан відновлено з файлу " + FNAME);
    }
}
