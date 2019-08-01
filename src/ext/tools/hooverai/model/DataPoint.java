package ext.tools.hooverai.model;

/**
 *
 * Simple x, y data point.
 */
public class DataPoint {

    public DataPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x;
    int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void showMe() {
        System.out.println("DataPoint X is " + x + " Y is " + y);
    }

}
