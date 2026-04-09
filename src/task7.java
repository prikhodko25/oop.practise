// Імпортуємо класи для графічного інтерфейсу
import javax.swing.*;
// Імпортуємо класи для малювання графіки
import java.awt.*;
// Імпортуємо класи для роботи з колекціями та випадковими числами
import java.util.*;
import java.util.List;

// КЛАСИ ДАНИХ
// Клас, що представляє точку з координатами X та Y
class Point2D {
    // Приватне поле для зберігання X координати
    private double x;
    // Приватне поле для зберігання Y координати
    private double y;
    
    // Конструктор, який викликається при створенні нової точки
    public Point2D(double x, double y) {
        // Присвоюємо значення полю x
        this.x = x;
        // Присвоюємо значення полю y
        this.y = y;
    }
    
    // Метод, що повертає значення X
    public double getX() { return x; }
    // Метод, що повертає значення Y
    public double getY() { return y; }
    // Метод, що встановлює нове значення Y
    public void setY(double y) { this.y = y; }
}

// ==================== ШАБЛОН OBSERVER ====================

// Інтерфейс спостерігача - всі спостерігачі повинні мати метод update()
interface Observer {
    // Метод, який викликається при зміні даних
    void update();
}

// Клас, за яким спостерігають (Observable)
class ObservableData {
    // Список точок (колекція даних)
    private List<Point2D> points = new ArrayList<>();
    // Список спостерігачів, які слідкують за змінами
    private List<Observer> observers = new ArrayList<>();
    
    // Метод для додавання нового спостерігача
    public void addObserver(Observer observer) {
        // Додаємо спостерігача до списку
        observers.add(observer);
    }
    
    // Приватний метод для сповіщення всіх спостерігачів про зміни
    private void notifyObservers() {
        // Проходимо по всіх спостерігачах
        for (Observer observer : observers) {
            // Викликаємо метод update() у кожного спостерігача
            observer.update();
        }
    }
    
    // Генерує випадкові дані
    public void generateRandomData(int count) {
        // Очищаємо список точок
        points.clear();
        // Створюємо генератор випадкових чисел
        Random rand = new Random();
        // Створюємо задану кількість точок
        for (int i = 0; i < count; i++) {
            // Додаємо точку з номером i+1 та випадковим Y від -50 до 50
            points.add(new Point2D(i + 1, rand.nextDouble() * 100 - 50));
        }
        // Сповіщаємо всіх спостерігачів про зміну даних
        notifyObservers();
    }
    
    // Додає одну точку вручну
    public void addPoint(double y) {
        // Додаємо нову точку з наступним номером та заданим Y
        points.add(new Point2D(points.size() + 1, y));
        // Сповіщаємо спостерігачів
        notifyObservers();
    }
    
    // Повертає список всіх точок
    public List<Point2D> getPoints() { return points; }
    // Повертає кількість точок
    public int size() { return points.size(); }
    
    // Обчислює максимальне значення Y
    public double getMaxY() {
        // Перетворюємо точки на потік, беремо значення Y, знаходимо максимум
        return points.stream().mapToDouble(Point2D::getY).max().orElse(0);
    }
    
    // Обчислює мінімальне значення Y
    public double getMinY() {
        // Перетворюємо точки на потік, беремо значення Y, знаходимо мінімум
        return points.stream().mapToDouble(Point2D::getY).min().orElse(0);
    }
    
    // Обчислює середнє значення Y
    public double getAvgY() {
        // Перетворюємо точки на потік, беремо значення Y, обчислюємо середнє
        return points.stream().mapToDouble(Point2D::getY).average().orElse(0);
    }
}

// ==================== КОНСОЛЬНИЙ СПОСТЕРІГАЧ ====================

// Спостерігач, який виводить статистику в консоль
class ConsoleObserver implements Observer {
    // Посилання на дані, за якими спостерігаємо
    private ObservableData data;
    
    // Конструктор, який отримує дані для спостереження
    public ConsoleObserver(ObservableData data) {
        // Зберігаємо посилання на дані
        this.data = data;
    }
    
    // Метод, який викликається при зміні даних
    @Override
    public void update() {
        // Виводимо кількість точок
        System.out.println("Points: " + data.size());
        // Виводимо максимальне значення Y
        System.out.println("Max Y: " + String.format("%.2f", data.getMaxY()));
        // Виводимо мінімальне значення Y
        System.out.println("Min Y: " + String.format("%.2f", data.getMinY()));
        // Виводимо середнє значення Y
        System.out.println("Avg Y: " + String.format("%.2f", data.getAvgY()));
    }
}

// ==================== ГРАФІЧНИЙ СПОСТЕРІГАЧ (СТОВПЦЕВА ДІАГРАМА) ====================

// Спостерігач, який малює стовпцеву діаграму
class BarChartPanel extends JPanel implements Observer {
    // Посилання на дані
    private ObservableData data;
    
    // Конструктор панелі для малювання
    public BarChartPanel(ObservableData data) {
        // Зберігаємо дані
        this.data = data;
        // Встановлюємо білий фон
        setBackground(Color.WHITE);
        // Встановлюємо бажаний розмір панелі
        setPreferredSize(new Dimension(800, 500));
    }
    
    // Метод, що викликається при зміні даних - перемальовує графік
    @Override
    public void update() {
        // Перемальовуємо панель
        repaint();
    }
    
    // Метод, що малює графік
    @Override
    protected void paintComponent(Graphics g) {
        // Викликаємо батьківський метод для очищення
        super.paintComponent(g);
        
        // Якщо немає даних - виводимо повідомлення
        if (data == null || data.getPoints().isEmpty()) {
            // Малюємо текст про відсутність даних
            g.drawString("No data. Press 'Generate'", 20, 50);
            // Виходимо з методу
            return;
        }
        
        // Перетворюємо Graphics в Graphics2D для кращого малювання
        Graphics2D g2d = (Graphics2D) g;
        // Вмикаємо згладжування для красивих ліній
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Отримуємо ширину та висоту панелі
        int w = getWidth();
        int h = getHeight();
        // Відступи від країв для графіка
        int marginLeft = 60, marginRight = 40, marginTop = 30, marginBottom = 60;
        // Ширина області для графіка
        int chartW = w - marginLeft - marginRight;
        // Висота області для графіка
        int chartH = h - marginTop - marginBottom;
        
        // Отримуємо список точок
        List<Point2D> points = data.getPoints();
        // Кількість стовпців
        int barCount = points.size();
        // Ширина одного стовпця (70% від доступного місця)
        double barW = (double) chartW / barCount * 0.7;
        // Відстань між стовпцями (30% від доступного місця)
        double spacing = (double) chartW / barCount * 0.3;
        
        // Отримуємо максимальне та мінімальне значення Y
        double maxY = data.getMaxY();
        double minY = data.getMinY();
        // Різниця між максимумом і мінімумом
        double range = maxY - minY;
        // Якщо всі значення однакові - робимо діапазон 1
        if (range == 0) range = 1;
        
        // МАЛЮЄМО СІТКУ
        // Встановлюємо сірий колір для сітки
        g2d.setColor(new Color(220, 220, 220));
        // Малюємо 6 горизонтальних ліній сітки
        for (int i = 0; i <= 5; i++) {
            // Обчислюємо Y координату лінії
            int y = marginTop + (int)(chartH - i * chartH / 5.0);
            // Малюємо горизонтальну лінію
            g2d.drawLine(marginLeft, y, marginLeft + chartW, y);
            // Обчислюємо значення для підпису
            double val = minY + i * range / 5.0;
            // Встановлюємо чорний колір для тексту
            g2d.setColor(Color.BLACK);
            // Малюємо підпис значення
            g2d.drawString(String.format("%.0f", val), marginLeft - 35, y + 4);
        }
        
        // МАЛЮЄМО ОСІ
        // Встановлюємо чорний колір
        g2d.setColor(Color.BLACK);
        // Встановлюємо товщину лінії 2 пікселі
        g2d.setStroke(new BasicStroke(2));
        // Малюємо вісь Y (вертикальна лінія)
        g2d.drawLine(marginLeft, marginTop, marginLeft, marginTop + chartH);
        // Малюємо вісь X (горизонтальна лінія)
        g2d.drawLine(marginLeft, marginTop + chartH, marginLeft + chartW, marginTop + chartH);
        
        // ПІДПИСИ ОСЕЙ
        // Підпис для осі X
        g2d.drawString("Point number", w/2 - 40, h - 15);
        // Повертаємо графіку для підпису осі Y
        g2d.rotate(-Math.PI/2);
        // Підпис для осі Y
        g2d.drawString("Y value", -h/2, 20);
        // Повертаємо графіку назад
        g2d.rotate(Math.PI/2);
        
        // МАЛЮЄМО СТОВПЦІ
        // Проходимо по всіх точках
        for (int i = 0; i < barCount; i++) {
            // Отримуємо поточну точку
            Point2D p = points.get(i);
            // Обчислюємо висоту стовпця в пікселях
            double barH = (p.getY() - minY) / range * chartH;
            // Якщо висота від'ємна - робимо 0
            if (barH < 0) barH = 0;
            // Якщо висота більша за область - обмежуємо
            if (barH > chartH) barH = chartH;
            
            // X координата стовпця
            int x = (int)(marginLeft + i * (barW + spacing));
            // Y координата стовпця (верхня точка)
            int y = (int)(marginTop + chartH - barH);
            
            // ВИБІР КОЛЬОРУ СТОВПЦЯ
            // Якщо значення додатнє - зелений колір
            if (p.getY() >= 0) {
                g2d.setColor(new Color(100, 200, 100));
            } else {
                // Якщо від'ємне - червоний колір
                g2d.setColor(new Color(255, 100, 100));
            }
            // Малюємо заповнений стовпець
            g2d.fillRect(x, y, (int)barW, (int)barH);
            // Встановлюємо чорний колір для рамки
            g2d.setColor(Color.BLACK);
            // Малюємо рамку навколо стовпця
            g2d.drawRect(x, y, (int)barW, (int)barH);
            
            // ПІДПИС ЗНАЧЕННЯ НАД СТОВПЦЕМ
            // Встановлюємо шрифт
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            // Форматуємо значення для виведення
            String val = String.format("%.0f", p.getY());
            // Малюємо значення над стовпцем
            g2d.drawString(val, x + (int)barW/2 - 10, y - 5);
            
            // ПІДПИС НОМЕРА ПІД СТОВПЦЕМ
            // Малюємо номер точки під стовпцем
            g2d.drawString(String.valueOf(i+1), x + (int)barW/2 - 5, marginTop + chartH + 18);
        }
        
        // ЛІНІЯ СЕРЕДНЬОГО ЗНАЧЕННЯ
        // Обчислюємо середнє значення
        double avg = data.getAvgY();
        // Якщо середнє значення в межах діапазону
        if (avg >= minY && avg <= maxY) {
            // Обчислюємо Y координату лінії середнього
            int avgY = (int)(marginTop + chartH - (avg - minY) / range * chartH);
            // Встановлюємо червоний колір
            g2d.setColor(Color.RED);
            // Встановлюємо пунктирну лінію
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0));
            // Малюємо горизонтальну лінію середнього
            g2d.drawLine(marginLeft, avgY, marginLeft + chartW, avgY);
            // Підпис біля лінії
            g2d.drawString("Avg: " + String.format("%.0f", avg), marginLeft + chartW - 70, avgY - 5);
        }
    }
}

// ==================== ГОЛОВНЕ ВІКНО ПРОГРАМИ ====================

// Головне вікно програми
class MainFrame extends JFrame {
    // Дані для відображення
    private ObservableData data;
    // Панель з графіком
    private BarChartPanel chartPanel;
    
    // Конструктор головного вікна
    public MainFrame() {
        // Встановлюємо заголовок вікна
        setTitle("Lab #7 - Bar Chart");
        // Дія при закритті вікна - завершення програми
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Встановлюємо розмір вікна
        setSize(900, 600);
        // Розміщуємо вікно по центру екрану
        setLocationRelativeTo(null);
        
        // Створюємо об'єкт з даними
        data = new ObservableData();
        
        // СТВОРЮЄМО ТА ДОДАЄМО СПОСТЕРІГАЧІВ
        // Створюємо графічний спостерігач
        chartPanel = new BarChartPanel(data);
        // Додаємо його до списку спостерігачів
        data.addObserver(chartPanel);
        
        // Створюємо консольний спостерігач
        ConsoleObserver consoleObserver = new ConsoleObserver(data);
        // Додаємо його до списку спостерігачів
        data.addObserver(consoleObserver);
        
        // Налаштовуємо інтерфейс користувача
        setupUI();
        
        // Генеруємо початкові дані (8 точок)
        data.generateRandomData(8);
    }
    
    // Метод для налаштування інтерфейсу
    private void setupUI() {
        // Встановлюємо менеджер розташування BorderLayout
        setLayout(new BorderLayout());
        
        // СТВОРЕННЯ ПАНЕЛІ З КНОПКАМИ (ВГОРІ)
        // Створюємо панель для кнопок
        JPanel topPanel = new JPanel();
        // Встановлюємо фон
        topPanel.setBackground(new Color(240, 248, 255));
        // Встановлюємо відступи
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // КНОПКА "ЗГЕНЕРУВАТИ ВИПАДКОВІ ДАНІ"
        // Створюємо кнопку
        JButton generateBtn = new JButton("Generate Random Data");
        // Встановлюємо колір фону кнопки
        generateBtn.setBackground(new Color(100, 200, 100));
        // Додаємо обробник натискання
        generateBtn.addActionListener(e -> {
            // Запитуємо кількість точок
            String cnt = JOptionPane.showInputDialog(this, "Number of points:", "8");
            try {
                // Генеруємо випадкові дані
                data.generateRandomData(Integer.parseInt(cnt));
            } catch (Exception ex) {
                // Якщо помилка - генеруємо 8 точок
                data.generateRandomData(8);
            }
        });
        
        // КНОПКА "ДОДАТИ ТОЧКУ"
        JButton addBtn = new JButton("Add Point");
        addBtn.setBackground(new Color(100, 150, 255));
        addBtn.addActionListener(e -> {
            // Запитуємо значення Y
            String y = JOptionPane.showInputDialog(this, "Y value (-50 to 50):", "0");
            try {
                // Додаємо нову точку
                data.addPoint(Double.parseDouble(y));
            } catch (Exception ex) {
                // Показуємо помилку
                JOptionPane.showMessageDialog(this, "Enter a number!");
            }
        });
        
        // КНОПКА "ПОКАЗАТИ СТАТИСТИКУ"
        JButton statsBtn = new JButton("Show Statistics");
        statsBtn.setBackground(new Color(255, 200, 100));
        statsBtn.addActionListener(e -> {
            // Показуємо вікно зі статистикою
            JOptionPane.showMessageDialog(this,
                "Statistics:\n" +
                "  Points: " + data.size() + "\n" +
                "  Max Y: " + String.format("%.2f", data.getMaxY()) + "\n" +
                "  Min Y: " + String.format("%.2f", data.getMinY()) + "\n" +
                "  Avg Y: " + String.format("%.2f", data.getAvgY()));
        });
        
        // КНОПКА "ОЧИСТИТИ ВСЕ"
        JButton clearBtn = new JButton("Clear All");
        clearBtn.setBackground(new Color(255, 150, 150));
        clearBtn.addActionListener(e -> {
            // Підтвердження видалення
            int confirm = JOptionPane.showConfirmDialog(this, "Delete all points?", "Confirm", 
                                                        JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Генеруємо 0 точок (очищаємо)
                data.generateRandomData(0);
            }
        });
        
        // Додаємо кнопки на панель
        topPanel.add(generateBtn);
        topPanel.add(addBtn);
        topPanel.add(statsBtn);
        topPanel.add(clearBtn);
        
        // Розміщуємо панель з кнопками у верхній частині вікна
        add(topPanel, BorderLayout.NORTH);
        
        // Розміщуємо графік у центральній частині
        add(chartPanel, BorderLayout.CENTER);
        
        // СТАТУСНА ПАНЕЛЬ (ВНИЗУ)
        // Створюємо підпис з поясненням
        JLabel statusLabel = new JLabel(" Green = positive values, Red = negative values, Dashed line = average");
        // Додаємо рамку
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        // Встановлюємо шрифт
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        // Розміщуємо в нижній частині вікна
        add(statusLabel, BorderLayout.SOUTH);
    }
}

// ==================== ГОЛОВНИЙ КЛАС ДЛЯ ЗАПУСКУ ====================

// Головний клас, з якого починається виконання програми
public class Main {
    // Метод main - точка входу в програму
    public static void main(String[] args) {
        // Запускаємо графічний інтерфейс в окремому потоці (правильний спосіб для Swing)
        SwingUtilities.invokeLater(() -> {
            // Створюємо головне вікно
            MainFrame frame = new MainFrame();
            // Робимо вікно видимим
            frame.setVisible(true);
        });
    }
}
