import java.util.ArrayList;

public class Stats {
    ArrayList<Double> compileTime = null;
    ArrayList<Double> transitions = null;
    ArrayList<Double> states = null;
    ArrayList<Double> plys = null;

    public Stats(ArrayList<Double> compileTime) {
        this.compileTime = new ArrayList<>(compileTime);
        states = new ArrayList<>(compileTime.size());
        transitions = new ArrayList<>(compileTime.size());
        plys = new ArrayList<>(compileTime.size());
    }

    public Stats() {
        this.compileTime = new ArrayList<>();
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        plys = new ArrayList<>();
    }
}