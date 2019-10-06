import java.util.List;

public class Point {
    private List<Double> value;
    private int output;


    public Point(List<Double> value, int output) {
        this.value = value;
        this.output = output;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public List<Double> getValue() {
        return value;
    }

    public void setValue(List<Double> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Point{" +
                "value=" + value +
                ", output=" + output +
                '}';
    }
}
