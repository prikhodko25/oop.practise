package ex16;

import java.io.Serializable;

/**
 * Зберігає вихідні дані та результати обчислень координат тіла.
 * Реалізує інтерфейс {@link Serializable} для можливості серіалізації.
 * 
 * @author Student
 * @version 1.0
 */
public class Item2d implements Serializable {

    /** Автоматично згенерована константа. */
    private static final long serialVersionUID = 1L;

    /** Початкова швидкість (м/с). */
    private double velocity;

    /** Кут до горизонту (градуси). */
    private double angle;

    /** Час польоту (с). */
    private double time;

    /** 
     * Координата X (горизонтальна).
     * Поле позначено як transient, щоб воно не серіалізувалося стандартним способом.
     * При десеріалізації отримає значення за замовчуванням (0.0).
     */
    private transient double x;

    /** Координата Y (вертикальна). */
    private double y;

    /** Прискорення вільного падіння (м/с²). */
    private static final double G = 9.81;

    /**
     * Конструктор за замовчуванням. Ініціалізує поля нульовими значеннями.
     */
    public Item2d() {
        velocity = 0.0;
        angle = 0.0;
        time = 0.0;
        x = 0.0;
        y = 0.0;
    }

    /**
     * Конструктор з параметрами.
     * 
     * @param velocity початкова швидкість (м/с)
     * @param angle    кут до горизонту (градуси)
     * @param time     час польоту (с)
     */
    public Item2d(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * Повертає початкову швидкість.
     * 
     * @return початкова швидкість (м/с)
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * Встановлює початкову швидкість.
     * 
     * @param velocity нове значення початкової швидкості
     * @return встановлене значення швидкості
     */
    public double setVelocity(double velocity) {
        return this.velocity = velocity;
    }

    /**
     * Повертає кут до горизонту.
     * 
     * @return кут (градуси)
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Встановлює кут до горизонту.
     * 
     * @param angle нове значення кута (градуси)
     * @return встановлене значення кута
     */
    public double setAngle(double angle) {
        return this.angle = angle;
    }

    /**
     * Повертає час польоту.
     * 
     * @return час (с)
     */
    public double getTime() {
        return time;
    }

    /**
     * Встановлює час польоту.
     * 
     * @param time нове значення часу (с)
     * @return встановлене значення часу
     */
    public double setTime(double time) {
        return this.time = time;
    }

    /**
     * Повертає координату X.
     * 
     * @return координата X (м)
     */
    public double getX() {
        return x;
    }

    /**
     * Встановлює координату X (використовується при обчисленнях).
     * 
     * @param x нове значення координати X (м)
     * @return встановлене значення X
     */
    public double setX(double x) {
        return this.x = x;
    }

    /**
     * Повертає координату Y.
     * 
     * @return координата Y (м)
     */
    public double getY() {
        return y;
    }

    /**
     * Встановлює координату Y.
     * 
     * @param y нове значення координати Y (м)
     * @return встановлене значення Y
     */
    public double setY(double y) {
        return this.y = y;
    }

    /**
     * Повертає прискорення вільного падіння (константа).
     * 
     * @return значення g = 9.81 м/с²
     */
    public static double getG() {
        return G;
    }

    /**
     * Обчислює координати X та Y за формулами руху тіла під дією сили тяжіння.
     * X = v0 * cos(α) * t
     * Y = v0 * sin(α) * t - (g * t²) / 2
     */
    public void calculateCoordinates() {
        double angleRad = Math.toRadians(angle);
        this.x = velocity * Math.cos(angleRad) * time;
        this.y = velocity * Math.sin(angleRad) * time - (G * time * time) / 2;
    }

    /**
     * Встановлює значення полів: швидкості, кута, часу
     * та обчислює координати.
     * 
     * @param velocity значення для ініціалізації поля {@link Item2d#velocity}
     * @param angle    значення для ініціалізації поля {@link Item2d#angle}
     * @param time     значення для ініціалізації поля {@link Item2d#time}
     * @return this
     */
    public Item2d setVAT(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        calculateCoordinates();
        return this;
    }

    /**
     * Представляє результат обчислень у вигляді рядка
     * з шістнадцятковим представленням цілої частини координат.
     * <br>{@inheritDoc}
     */
    @Override
    public String toString() {
        int intX = (int) x;
        int intY = (int) y;
        String hexX = Integer.toHexString(intX).toUpperCase();
        String hexY = Integer.toHexString(intY).toUpperCase();
        return String.format("x = %.2f (0x%s), y = %.2f (0x%s)", x, hexX, y, hexY);
    }

    /**
     * Автоматично згенерований метод.<br>{@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item2d other = (Item2d) obj;
        if (Double.doubleToLongBits(velocity) != Double.doubleToLongBits(other.velocity))
            return false;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle))
            return false;
        if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time))
            return false;
        if (Math.abs(x - other.x) > .1e-10)
            return false;
        if (Math.abs(y - other.y) > .1e-10)
            return false;
        return true;
    }
}
