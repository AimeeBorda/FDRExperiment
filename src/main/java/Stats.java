import java.util.TreeMap;

public class Stats {
    TreeMap<String,Double> compileTime = null;
    TreeMap<String,Double> transitions = null;
    TreeMap<String,Double> states = null;
    TreeMap<String,Double> plys = null;



    public Stats() {
        this.compileTime = new TreeMap<>();
        states = new TreeMap<>();
        transitions = new TreeMap<>();
        plys = new TreeMap<>();
    }
}