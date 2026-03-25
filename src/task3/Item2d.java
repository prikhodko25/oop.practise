package ex17;

import java.io.Serializable;

/**
 * Зберігає вихідні дані та результати обчислень координат тіла.
 * Реалізує інтерфейс {@link Serializable} для можливості серіалізації.
 * 
 * @author Student
 * @version 2.0
 */
public class Item2d implements Serializable {

    private static final long serialVersionUID = 1L;
    private double velocity;
    private double angle;
    private double time;
    private transient double x;
    private double y;
    private static final double G = 9.81;

    public Item2d() {
        velocity = 0.0;
        angle = 0.0;
        time = 0.0;
        x = 0.0;
        y = 0.0;
    }

    public Item2d(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        this.x = 0.0;
        this.y = 0.0;
    }

    public double getVelocity() { return velocity; }
    public double setVelocity(double velocity) { return this.velocity = velocity; }
    public double getAngle() { return angle; }
    public double setAngle(double angle) { return this.angle = angle; }
    public double getTime() { return time; }
    public double setTime(double time) { return this.time = time; }
    public double getX() { return x; }
    public double setX(double x) { return this.x = x; }
    public double getY() { return y; }
    public double setY(double y) { return this.y = y; }
    public static double getG() { return G; }

    public void calculateCoordinates() {
        double angleRad = Math.toRadians(angle);
        this.x = velocity * Math.cos(angleRad) * time;
        this.y = velocity * Math.sin(angleRad) * time - (G * time * time) / 2;
    }

    public Item2d setVAT(double velocity, double angle, double time) {
        this.velocity = velocity;
        this.angle = angle;
        this.time = time;
        calculateCoordinates();
        return this;
    }

    @Override
    public String toString() {
        return String.format("v0=%.1f м/с, α=%.1f°, t=%.1f с → x=%.2f м, y=%.2f м", 
                velocity, angle, time, x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Item2d other = (Item2d) obj;
        if (Double.doubleToLongBits(velocity) != Double.doubleToLongBits(other.velocity)) return false;
        if (Double.doubleToLongBits(angle) != Double.doubleToLongBits(other.angle)) return false;
        if (Double.doubleToLongBits(time) != Double.doubleToLongBits(other.time)) return false;
        if (Math.abs(x - other.x) > .1e-10) return false;
        if (Math.abs(y - other.y) > .1e-10) return false;
        return true;
    }
}
