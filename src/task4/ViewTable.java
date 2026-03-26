package ex17;

import java.util.Formatter;

/**
 * ConcreteProduct (шаблон проектування Factory Method)<br>
 * Виведення результатів у вигляді текстової таблиці
 * 
 * @author Student
 * @version 3.0
 */
public class ViewTable extends ViewResult {
    
    private static final int DEFAULT_WIDTH = 30;
    private int width;

    public ViewTable() {
        width = DEFAULT_WIDTH;
    }

    public ViewTable(int width) {
        this.width = width;
    }

    public ViewTable(int width, int n) {
        super(n);
        this.width = width;
    }

    public int setWidth(int width) {
        return this.width = width;
    }

    public int getWidth() {
        return width;
    }

    private void outLine() {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
    }

    private void outLineLn() {
        outLine();
        System.out.println();
    }

    private void outHeader() {
        int colWidth = (width - 3) / 2;
        String format = "%" + colWidth + "s | %" + colWidth + "s%n";
        System.out.printf(format, "x (м)", "y (м)");
        outLineLn();
    }

    private void outBody() {
        int colWidth = (width - 3) / 2;
        String format = "%" + colWidth + ".2f | %" + colWidth + ".2f%n";
        
        for (Item2d item : getItems()) {
            System.out.printf(format, item.getX(), item.getY());
        }
    }

    public final void init(int width) {
        this.width = width;
        viewInit();
    }

    public final void init(int width, double stepX) {
        this.width = width;
        init(stepX);
    }

    @Override
    public void init(double stepX) {
        super.init(stepX);
    }

    @Override
    public void viewInit() {
        super.viewInit();
    }

    @Override
    public void viewHeader() {
        System.out.println();
        outHeader();
    }

    @Override
    public void viewBody() {
        if (getItems().isEmpty() || getItems().get(0).getVelocity() == 0) {
            System.out.println("Дані відсутні.");
            return;
        }
        outBody();
    }

    @Override
    public void viewFooter() {
        outLineLn();
    }
}
