# Завдання 7
## Код
```java
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// КЛАСИ ДАНИХ 

class Point2D {
    private double x;
    private double y;
    
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
}

// OBSERVER

interface Observer {
    void update();
}

class ObservableData {
    private List<Point2D> points = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    
    public void generateRandomData(int count) {
        points.clear();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            points.add(new Point2D(i + 1, rand.nextDouble() * 100 - 50));
        }
        notifyObservers();
    }
    
    public void addPoint(double y) {
        points.add(new Point2D(points.size() + 1, y));
        notifyObservers();
    }
    
    public List<Point2D> getPoints() { return points; }
    public int size() { return points.size(); }
    
    public double getMaxY() {
        return points.stream().mapToDouble(Point2D::getY).max().orElse(0);
    }
    
    public double getMinY() {
        return points.stream().mapToDouble(Point2D::getY).min().orElse(0);
    }
    
    public double getAvgY() {
        return points.stream().mapToDouble(Point2D::getY).average().orElse(0);
    }
}

// КОНСОЛЬНИЙ СПОСТЕРІГАЧ 

class ConsoleObserver implements Observer {
    private ObservableData data;
    
    public ConsoleObserver(ObservableData data) {
        this.data = data;
    }
    
    @Override
    public void update() {
        System.out.println("Points: " + data.size());
        System.out.println("Max Y: " + String.format("%.2f", data.getMaxY()));
        System.out.println("Min Y: " + String.format("%.2f", data.getMinY()));
        System.out.println("Avg Y: " + String.format("%.2f", data.getAvgY()));
    }
}

// ГРАФІК 

class BarChartPanel extends JPanel implements Observer {
    private ObservableData data;
    
    public BarChartPanel(ObservableData data) {
        this.data = data;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 500));
    }
    
    @Override
    public void update() {
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (data == null || data.getPoints().isEmpty()) {
            g.drawString("No data. Press 'Generate'", 20, 50);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        int marginLeft = 60, marginRight = 40, marginTop = 30, marginBottom = 60;
        int chartW = w - marginLeft - marginRight;
        int chartH = h - marginTop - marginBottom;
        
        List<Point2D> points = data.getPoints();
        int barCount = points.size();
        double barW = (double) chartW / barCount * 0.7;
        double spacing = (double) chartW / barCount * 0.3;
        
        double maxY = data.getMaxY();
        double minY = data.getMinY();
        double range = maxY - minY;
        if (range == 0) range = 1;
        
        // Сітка
        g2d.setColor(new Color(220, 220, 220));
        for (int i = 0; i <= 5; i++) {
            int y = marginTop + (int)(chartH - i * chartH / 5.0);
            g2d.drawLine(marginLeft, y, marginLeft + chartW, y);
            double val = minY + i * range / 5.0;
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.format("%.0f", val), marginLeft - 35, y + 4);
        }
        
        // Осі
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(marginLeft, marginTop, marginLeft, marginTop + chartH);
        g2d.drawLine(marginLeft, marginTop + chartH, marginLeft + chartW, marginTop + chartH);
        
        g2d.drawString("Point number", w/2 - 40, h - 15);
        g2d.rotate(-Math.PI/2);
        g2d.drawString("Y value", -h/2, 20);
        g2d.rotate(Math.PI/2);
        
        // Стовпці
        for (int i = 0; i < barCount; i++) {
            Point2D p = points.get(i);
            double barH = (p.getY() - minY) / range * chartH;
            if (barH < 0) barH = 0;
            if (barH > chartH) barH = chartH;
            
            int x = (int)(marginLeft + i * (barW + spacing));
            int y = (int)(marginTop + chartH - barH);
            
            if (p.getY() >= 0) {
                g2d.setColor(new Color(100, 200, 100));
            } else {
                g2d.setColor(new Color(255, 100, 100));
            }
            g2d.fillRect(x, y, (int)barW, (int)barH);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, (int)barW, (int)barH);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String val = String.format("%.0f", p.getY());
            g2d.drawString(val, x + (int)barW/2 - 10, y - 5);
            
            g2d.drawString(String.valueOf(i+1), x + (int)barW/2 - 5, marginTop + chartH + 18);
        }
        
        // Лінія середнього
        double avg = data.getAvgY();
        if (avg >= minY && avg <= maxY) {
            int avgY = (int)(marginTop + chartH - (avg - minY) / range * chartH);
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5, 5}, 0));
            g2d.drawLine(marginLeft, avgY, marginLeft + chartW, avgY);
            g2d.drawString("Avg: " + String.format("%.0f", avg), marginLeft + chartW - 70, avgY - 5);
        }
    }
}

// ГОЛОВНЕ ВІКНО 

class MainFrame extends JFrame {
    private ObservableData data;
    private BarChartPanel chartPanel;
    
    public MainFrame() {
        setTitle("Lab #7 - Bar Chart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        data = new ObservableData();
        
        // Спостерігачі
        chartPanel = new BarChartPanel(data);
        data.addObserver(chartPanel);
        
        ConsoleObserver consoleObserver = new ConsoleObserver(data);
        data.addObserver(consoleObserver);
        
        setupUI();
        
        data.generateRandomData(8);
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Панель кнопок
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(240, 248, 255));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton generateBtn = new JButton("Generate Random Data");
        generateBtn.setBackground(new Color(100, 200, 100));
        generateBtn.addActionListener(e -> {
            String cnt = JOptionPane.showInputDialog(this, "Number of points:", "8");
            try {
                data.generateRandomData(Integer.parseInt(cnt));
            } catch (Exception ex) {
                data.generateRandomData(8);
            }
        });
        
        JButton addBtn = new JButton("Add Point");
        addBtn.setBackground(new Color(100, 150, 255));
        addBtn.addActionListener(e -> {
            String y = JOptionPane.showInputDialog(this, "Y value (-50 to 50):", "0");
            try {
                data.addPoint(Double.parseDouble(y));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter a number!");
            }
        });
        
        JButton statsBtn = new JButton("Show Statistics");
        statsBtn.setBackground(new Color(255, 200, 100));
        statsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Statistics:\n" +
                "  Points: " + data.size() + "\n" +
                "  Max Y: " + String.format("%.2f", data.getMaxY()) + "\n" +
                "  Min Y: " + String.format("%.2f", data.getMinY()) + "\n" +
                "  Avg Y: " + String.format("%.2f", data.getAvgY()));
        });
        
        JButton clearBtn = new JButton("Clear All");
        clearBtn.setBackground(new Color(255, 150, 150));
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete all points?", "Confirm", 
                                                        JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                data.generateRandomData(0);
            }
        });
        
        topPanel.add(generateBtn);
        topPanel.add(addBtn);
        topPanel.add(statsBtn);
        topPanel.add(clearBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        add(chartPanel, BorderLayout.CENTER);
        
        JLabel statusLabel = new JLabel(" Green = positive values, Red = negative values, Dashed line = average");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        add(statusLabel, BorderLayout.SOUTH);
    }
}

// ГОЛОВНИЙ КЛАС

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
```
## Результат
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/cns.png)
![alt text](https://github.com/prikhodko25/oop.practise/blob/main/image/gr.png)
