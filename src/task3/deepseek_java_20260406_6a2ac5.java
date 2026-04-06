// Єдиний файл: Main.java
// Компіляція: javac -encoding UTF-8 Main.java
// Запуск: java Main

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

// ==================== АНОТАЦІЇ ====================

/**
 * Анотація для позначення полів, які потрібно відстежувати
 * RetentionPolicy.RUNTIME - дозволяє використовувати рефлексію під час виконання
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface ObservableField {
    String description() default "";
}

/**
 * Анотація для позначення методів, які викликаються при зміні даних
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface OnChange {
    String value() default "";
}

/**
 * Анотація з політикою CLASS (зберігається в байт-коді, але не доступна під час виконання)
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@interface ClassAnnotation {
    String info();
}

/**
 * Анотація з політикою SOURCE (використовується тільки під час компіляції)
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@interface SourceAnnotation {
    String author();
}

// ==================== КЛАСИ ДАНИХ ====================

/**
 * Клас Point2D - точка з координатами
 */
class Point2D {
    @ObservableField(description = "X координата")
    private double x;
    
    @ObservableField(description = "Y координата")
    private double y;
    
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}

/**
 * Клас Item2d - оновлений з підтримкою спостереження
 */
class Item2d {
    @ObservableField(description = "Координата X")
    private double x;
    
    @ObservableField(description = "Координата Y")
    private double y;
    
    private String name;
    
    public Item2d(double x, double y) {
        this.x = x;
        this.y = y;
        this.name = "Точка";
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setName(String name) { this.name = name; }
    
    @OnChange("Значення змінено")
    public void onChange() {
        // Метод, який викликається при зміні даних
    }
    
    @Override
    public String toString() {
        return String.format("%s: x=%.2f, y=%.2f", name, x, y);
    }
}

// ==================== СПОСТЕРІГАЧІ (OBSERVER) ====================

/**
 * Інтерфейс спостерігача
 */
interface Observer {
    void update(ObservableData data);
    String getName();
}

/**
 * Інтерфейс спостережуваного об'єкта
 */
interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

/**
 * Клас даних, за яким спостерігають (Observable)
 */
class ObservableData implements Observable {
    private List<Point2D> points;
    private List<Observer> observers;
    private String status;
    
    public ObservableData() {
        points = new ArrayList<>();
        observers = new ArrayList<>();
        status = "Початковий стан";
        generateRandomData(10);
    }
    
    public void generateRandomData(int count) {
        points.clear();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            double x = rand.nextDouble() * 100;
            double y = rand.nextDouble() * 100 - 50;
            points.add(new Point2D(x, y));
        }
        status = "Згенеровано " + count + " точок";
        notifyObservers();
    }
    
    public void addPoint(double x, double y) {
        points.add(new Point2D(x, y));
        status = "Додано точку " + String.format("(%.2f, %.2f)", x, y);
        notifyObservers();
    }
    
    public void updatePoint(int index, double x, double y) {
        if (index >= 0 && index < points.size()) {
            points.get(index).setX(x);
            points.get(index).setY(y);
            status = "Оновлено точку #" + index;
            notifyObservers();
        }
    }
    
    public List<Point2D> getPoints() {
        return points;
    }
    
    public String getStatus() {
        return status;
    }
    
    public int getPointCount() {
        return points.size();
    }
    
    public double getMaxY() {
        return points.stream().mapToDouble(Point2D::getY).max().orElse(0);
    }
    
    public double getMinY() {
        return points.stream().mapToDouble(Point2D::getY).min().orElse(0);
    }
    
    public double getAvgY() {
        return points.stream().mapToDouble(Point2D::getY).average().orElse(0);
    }
    
    public double getMaxX() {
        return points.stream().mapToDouble(Point2D::getX).max().orElse(0);
    }
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }
}

/**
 * Спостерігач для виведення в консоль
 */
class ConsoleObserver implements Observer {
    private String name;
    
    public ConsoleObserver(String name) {
        this.name = name;
    }
    
    @Override
    public void update(ObservableData data) {
        System.out.println("\n[" + name + "] Стан змінено: " + data.getStatus());
        System.out.println("[" + name + "] Кількість точок: " + data.getPointCount());
        System.out.println("[" + name + "] Статистика: Max Y=" + String.format("%.2f", data.getMaxY()) + 
                          ", Min Y=" + String.format("%.2f", data.getMinY()) + 
                          ", Avg Y=" + String.format("%.2f", data.getAvgY()));
    }
    
    @Override
    public String getName() {
        return name;
    }
}

/**
 * Спостерігач для графічного відображення
 */
class GraphObserver extends JPanel implements Observer {
    private ObservableData data;
    private String name;
    
    public GraphObserver(String name) {
        this.name = name;
        setPreferredSize(new Dimension(450, 350));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("📊 Графік точок (x, y)"));
    }
    
    public void setData(ObservableData data) {
        this.data = data;
        repaint();
    }
    
    @Override
    public void update(ObservableData data) {
        this.data = data;
        repaint();
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.getPoints().isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("📭 Немає даних для відображення", 20, 50);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth() - 80;
        int height = getHeight() - 80;
        int margin = 50;
        
        double maxX = data.getMaxX();
        double maxY = data.getMaxY();
        double minY = data.getMinY();
        
        if (maxX == 0) maxX = 100;
        if (maxY == minY) {
            maxY = minY + 1;
        }
        
        // Малюємо сітку
        g2d.setColor(new Color(220, 220, 220));
        for (int i = 0; i <= 5; i++) {
            int x = margin + (i * width / 5);
            g2d.drawLine(x, margin, x, getHeight() - margin);
            int y = margin + (i * height / 5);
            g2d.drawLine(margin, y, getWidth() - margin, y);
        }
        
        // Малюємо осі
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(margin, getHeight() - margin, getWidth() - margin, getHeight() - margin);
        g2d.drawLine(margin, margin, margin, getHeight() - margin);
        
        // Стрілки на осях
        g2d.fillPolygon(new int[]{getWidth() - margin, getWidth() - margin - 5, getWidth() - margin - 5}, 
                        new int[]{getHeight() - margin, getHeight() - margin - 5, getHeight() - margin + 5}, 3);
        g2d.fillPolygon(new int[]{margin, margin - 5, margin + 5}, 
                        new int[]{margin, margin + 5, margin + 5}, 3);
        
        // Підписи осей
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("X", getWidth() - margin + 5, getHeight() - margin);
        g2d.drawString("Y", margin - 15, margin);
        
        // Малюємо точки та лінії
        g2d.setColor(new Color(255, 80, 80));
        g2d.setStroke(new BasicStroke(2));
        
        List<Point2D> points = data.getPoints();
        List<java.awt.Point> screenPoints = new ArrayList<>();
        
        for (Point2D point : points) {
            int screenX = (int) (margin + (point.getX() / maxX) * width);
            int screenY = (int) (getHeight() - margin - ((point.getY() - minY) / (maxY - minY)) * height);
            
            if (screenX >= margin && screenX <= getWidth() - margin && 
                screenY >= margin && screenY <= getHeight() - margin) {
                screenPoints.add(new java.awt.Point(screenX, screenY));
                g2d.fillOval(screenX - 5, screenY - 5, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 9));
                g2d.drawString(String.format("(%.0f,%.0f)", point.getX(), point.getY()), screenX + 6, screenY - 6);
                g2d.setColor(new Color(255, 80, 80));
            }
        }
        
        // З'єднуємо точки лініями
        g2d.setColor(new Color(100, 100, 255, 100));
        for (int i = 0; i < screenPoints.size() - 1; i++) {
            g2d.drawLine(screenPoints.get(i).x, screenPoints.get(i).y, 
                        screenPoints.get(i + 1).x, screenPoints.get(i + 1).y);
        }
        
        // Статистика на графіку
        g2d.setColor(new Color(0, 100, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("📈 Max Y: " + String.format("%.2f", data.getMaxY()), margin, margin - 15);
        g2d.drawString("📉 Min Y: " + String.format("%.2f", data.getMinY()), margin, margin - 2);
        g2d.drawString("📊 Avg Y: " + String.format("%.2f", data.getAvgY()), margin, margin + 11);
    }
}

/**
 * Спостерігач для табличного відображення
 */
class TableObserver implements Observer {
    private String name;
    private JTextArea textArea;
    
    public TableObserver(String name, JTextArea textArea) {
        this.name = name;
        this.textArea = textArea;
    }
    
    @Override
    public void update(ObservableData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("╔════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                       ").append(name).append("                       ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append("║ Статус: ").append(String.format("%-62s", data.getStatus())).append("║\n");
        sb.append("║ Кількість точок: ").append(String.format("%-55d", data.getPointCount())).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append("║  № │     X     │     Y     ║\n");
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        
        List<Point2D> points = data.getPoints();
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            sb.append(String.format("║ %2d │ %9.2f │ %9.2f ║\n", i + 1, p.getX(), p.getY()));
        }
        
        sb.append("╠════════════════════════════════════════════════════════════════╣\n");
        sb.append("║ СТАТИСТИКА:                                                  ║\n");
        sb.append(String.format("║   📈 Максимальне Y: %-42.2f ║\n", data.getMaxY()));
        sb.append(String.format("║   📉 Мінімальне Y:   %-42.2f ║\n", data.getMinY()));
        sb.append(String.format("║   📊 Середнє Y:     %-42.2f ║\n", data.getAvgY()));
        sb.append("╚════════════════════════════════════════════════════════════════╝\n");
        
        textArea.setText(sb.toString());
    }
    
    @Override
    public String getName() {
        return name;
    }
}

// ==================== РЕФЛЕКСІЯ ====================

/**
 * Клас для демонстрації рефлексії
 */
class ReflectionDemo {
    
    public static void inspectClass(Class<?> clazz) {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║              ІНСПЕКТУЄМО КЛАС: " + String.format("%-25s", clazz.getSimpleName()) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("║ ПОЛЯ:                                                         ║");
        for (Field field : fields) {
            System.out.println("║   🔹 " + String.format("%-20s", field.getName()) + 
                              " (" + field.getType().getSimpleName() + ")");
            if (field.isAnnotationPresent(ObservableField.class)) {
                ObservableField ann = field.getAnnotation(ObservableField.class);
                System.out.println("║       └─ @ObservableField: " + ann.description());
            }
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ МЕТОДИ:                                                       ║");
        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            System.out.println("║   🔸 " + String.format("%-20s", method.getName()) + "()");
            if (method.isAnnotationPresent(OnChange.class)) {
                OnChange ann = method.getAnnotation(OnChange.class);
                System.out.println("║       └─ @OnChange: " + ann.value());
            }
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ АНОТАЦІЇ КЛАСУ:                                              ║");
        for (Annotation ann : clazz.getAnnotations()) {
            System.out.println("║   📌 " + ann.toString());
        }
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
    
    public static void demonstrateRetention() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║     ДЕМОНСТРАЦІЯ ПОЛІТИК УТРИМАННЯ АНОТАЦІЙ                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ 1. RUNTIME – доступна під час виконання                       ║");
        System.out.println("║    → @ObservableField – отримали через рефлексію              ║");
        System.out.println("║                                                               ║");
        System.out.println("║ 2. CLASS – зберігається в .class, але не доступна під час     ║");
        System.out.println("║    виконання                                                   ║");
        System.out.println("║    → @ClassAnnotation – не буде видна через рефлексію         ║");
        System.out.println("║                                                               ║");
        System.out.println("║ 3. SOURCE – тільки під час компіляції                         ║");
        System.out.println("║    → @SourceAnnotation – видаляється після компіляції         ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
    }
}

// ==================== ДІАЛОГ ДЛЯ ДОДАВАННЯ ТОЧКИ ====================

class AddPointDialog extends JDialog {
    private JTextField xField, yField;
    private boolean confirmed = false;
    private double pointX, pointY;
    
    public AddPointDialog(JFrame parent) {
        super(parent, "Додати точку", true);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("X координата:"));
        xField = new JTextField("50");
        panel.add(xField);
        
        panel.add(new JLabel("Y координата:"));
        yField = new JTextField("0");
        panel.add(yField);
        
        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Скасувати");
        
        okBtn.addActionListener(e -> {
            try {
                pointX = Double.parseDouble(xField.getText());
                pointY = Double.parseDouble(yField.getText());
                confirmed = true;
                setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Введіть коректні числа!");
            }
        });
        
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    public boolean isConfirmed() { return confirmed; }
    public double getPointX() { return pointX; }
    public double getPointY() { return pointY; }
}

// ==================== ГОЛОВНЕ ВІКНО ====================

/**
 * Головне вікно програми
 */
class MainFrame extends JFrame {
    private ObservableData data;
    private GraphObserver graphObserver;
    private JTextArea textArea;
    private JLabel statusLabel;
    private JLabel statsLabel;
    
    public MainFrame() {
        setTitle("📊 Лабораторна робота №7 - Observer, Annotation, Reflection, Graphics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        
        // Створюємо дані
        data = new ObservableData();
        
        // Створюємо спостерігачів
        ConsoleObserver consoleObserver = new ConsoleObserver("Консольний спостерігач");
        graphObserver = new GraphObserver("Графічний спостерігач");
        graphObserver.setData(data);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        TableObserver tableObserver = new TableObserver("Табличний спостерігач", textArea);
        
        // Додаємо спостерігачів
        data.addObserver(consoleObserver);
        data.addObserver(graphObserver);
        data.addObserver(tableObserver);
        
        // Створюємо інтерфейс
        setupUI();
        
        // Початкове оновлення
        data.notifyObservers();
        
        // Демонстрація рефлексії в консолі
        ReflectionDemo.inspectClass(Item2d.class);
        ReflectionDemo.demonstrateRetention();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Верхня панель з кнопками
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("🎮 Управління"));
        topPanel.setBackground(new Color(240, 248, 255));
        
        JButton generateBtn = new JButton("🎲 Згенерувати випадкові дані");
        generateBtn.setBackground(new Color(100, 200, 100));
        generateBtn.addActionListener(e -> {
            String countStr = JOptionPane.showInputDialog(this, "Кількість точок:", "10");
            try {
                int count = Integer.parseInt(countStr);
                data.generateRandomData(count);
            } catch (NumberFormatException ex) {
                data.generateRandomData(10);
            }
        });
        
        JButton addPointBtn = new JButton("➕ Додати точку");
        addPointBtn.setBackground(new Color(100, 150, 255));
        addPointBtn.addActionListener(e -> {
            AddPointDialog dialog = new AddPointDialog(this);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                data.addPoint(dialog.getPointX(), dialog.getPointY());
            }
        });
        
        JButton reflectionBtn = new JButton("🔍 Показати рефлексію");
        reflectionBtn.setBackground(new Color(255, 200, 100));
        reflectionBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════════════════════════════════════════════════════╗\n");
            sb.append("║              РЕФЛЕКСІЯ КЛАСУ Item2d                           ║\n");
            sb.append("╠════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ ПОЛЯ:                                                         ║\n");
            for (Field field : Item2d.class.getDeclaredFields()) {
                sb.append("║   🔹 ").append(String.format("%-20s", field.getName()));
                sb.append(" (").append(field.getType().getSimpleName()).append(")\n");
                if (field.isAnnotationPresent(ObservableField.class)) {
                    sb.append("║       └─ @ObservableField: ")
                      .append(field.getAnnotation(ObservableField.class).description()).append("\n");
                }
            }
            sb.append("╠════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ МЕТОДИ:                                                       ║\n");
            for (java.lang.reflect.Method method : Item2d.class.getDeclaredMethods()) {
                sb.append("║   🔸 ").append(String.format("%-20s", method.getName())).append("()\n");
                if (method.isAnnotationPresent(OnChange.class)) {
                    sb.append("║       └─ @OnChange: ")
                      .append(method.getAnnotation(OnChange.class).value()).append("\n");
                }
            }
            sb.append("╚════════════════════════════════════════════════════════════════╝");
            JOptionPane.showMessageDialog(this, sb.toString(), "🔍 Рефлексія", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton statsBtn = new JButton("📊 Статистика");
        statsBtn.setBackground(new Color(100, 200, 200));
        statsBtn.addActionListener(e -> {
            String msg = String.format(
                "╔════════════════════════════════════════════╗\n" +
                "║              СТАТИСТИКА                    ║\n" +
                "╠════════════════════════════════════════════╣\n" +
                "║   📈 Максимальне Y: %24.2f ║\n" +
                "║   📉 Мінімальне Y:   %24.2f ║\n" +
                "║   📊 Середнє Y:     %24.2f ║\n" +
                "║   📌 Кількість точок: %23d ║\n" +
                "╚════════════════════════════════════════════╝",
                data.getMaxY(), data.getMinY(), data.getAvgY(), data.getPointCount());
            JOptionPane.showMessageDialog(this, msg, "📊 Статистика", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton clearBtn = new JButton("🗑 Очистити всі дані");
        clearBtn.setBackground(new Color(255, 150, 150));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Видалити всі точки?", "Підтвердження", 
                                                        JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                data.generateRandomData(0);
            }
        });
        
        topPanel.add(generateBtn);
        topPanel.add(addPointBtn);
        topPanel.add(reflectionBtn);
        topPanel.add(statsBtn);
        topPanel.add(clearBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Центральна панель (графік + таблиця)
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setLeftComponent(new JScrollPane(graphObserver));
        centerSplit.setRightComponent(new JScrollPane(textArea));
        centerSplit.setDividerLocation(550);
        
        add(centerSplit, BorderLayout.CENTER);
        
        // Нижня панель (статус та статистика)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        
        statusLabel = new JLabel("🟢 Готово");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statsLabel = new JLabel("📊 Очікування даних...");
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        bottomPanel.add(statusLabel);
        bottomPanel.add(statsLabel);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Таймер для оновлення статусу
        javax.swing.Timer timer = new javax.swing.Timer(300, e -> {
            statusLabel.setText("🟢 Статус: " + data.getStatus() + " | 📌 Точок: " + data.getPointCount());
            statsLabel.setText(String.format("📊 Статистика: Max Y=%.2f | Min Y=%.2f | Avg Y=%.2f", 
                                            data.getMaxY(), data.getMinY(), data.getAvgY()));
        });
        timer.start();
    }
}

// ==================== ГОЛОВНИЙ КЛАС ====================

/**
 * Головний клас програми
 */
public class Main {
    public static void main(String[] args) {
        // Встановлюємо Look and Feel для кращого вигляду
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Запускаємо графічний інтерфейс
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
        
        // Виводимо інформацію в консоль
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                    ЛАБОРАТОРНА РОБОТА №7                                     ║\n" +
            "║              Observer, Annotation, Reflection, Graphics                     ║\n" +
            "╠══════════════════════════════════════════════════════════════════════════════╣\n" +
            "║ Функції програми:                                                            ║\n" +
            "║   • Генерація випадкових точок                                               ║\n" +
            "║   • Додавання точок вручну                                                   ║\n" +
            "║   • Три спостерігачі: консольний, графічний, табличний                       ║\n" +
            "║   • Автоматичне оновлення графіка при зміні даних                            ║\n" +
            "║   • Рефлексія для перегляду структури класу                                  ║\n" +
            "║   • Анотації з різними політиками утримання                                  ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════╝\n");
    }
}