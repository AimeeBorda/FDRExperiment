import java.util.ArrayList;

public class Stats {
    ArrayList<Double> compileTime = null;
    ArrayList<Double> transitions = null;
    ArrayList<Double> states = null;
    ArrayList<Double> plys = null;

    public void initializeLists(int size) {
        compileTime = new ArrayList<>(size);
        states = new ArrayList<>(size);
        transitions = new ArrayList<>(size);
        plys = new ArrayList<>(size);
    }
}