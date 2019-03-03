import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateLatex {

    private final static String[] colors = {"red", "blue", "green", "yellow", "purple", "orange", "black", "brown", "gray", "awesome", "cyan", "azure", "bazaar", "blue-green", "brightube"};
    private final static String[] symbol = {"x", "diamond", "square", "star", "triangle","otimes", "oplus", "halfcircle"};
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static HashMap<String, String> createLatexFile(TreeMap<Integer, Stats> statistics) {
        List<String> legends = statistics.values().iterator().next().states.keySet().stream().collect(Collectors.toList());
        HashMap<String, String> result = createGraph(legends, statistics);
        result.putAll(createTable(legends, statistics));

        return result;
    }

    private static HashMap<String, String> createGraph(List<String> legends, TreeMap<Integer, Stats> statistics) {
        StringBuilder resCompileTime = new StringBuilder();
        StringBuilder resStates = new StringBuilder();
        StringBuilder resTransition = new StringBuilder();
        StringBuilder resPlys = new StringBuilder();


        for (int i = 0; i < legends.size(); i++) {
            String key = legends.get(i);
            resCompileTime.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resStates.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resTransition.append(" \\addlegendentry{" + key + "} \n \\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resPlys.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");

            for (Map.Entry<Integer, Stats> s : statistics.entrySet()) {
                Stats value = s.getValue();
                if (value.compileTime.containsKey(key) && value.states.containsKey(key)) {
                    resCompileTime.append("(" + s.getKey() + "," + df2.format(value.compileTime.get(key)) + ")\n");
                    resStates.append("(" + s.getKey() + "," + value.states.get(key) + ")\n");
                    resTransition.append("(" + s.getKey() + "," + value.transitions.get(key) + ")\n");
                    resPlys.append("(" + s.getKey() + "," + value.plys.get(key) + ")\n");
                }
            }
            resCompileTime.append("};\n");
            resStates.append("};\n");
            resTransition.append("};\n");
            resPlys.append("};\n");
        }

        return new HashMap<String, String>() {{
            put("compileTimeGraph", resCompileTime.toString());
            put("statesGraph", resStates.toString());
            put("transitionsGraph", resTransition.toString());
            put("plysGraph", resPlys.toString());
        }};
    }

    private static HashMap<String, String> createTable(List<String> legends, TreeMap<Integer, Stats> statistics) {
        StringBuilder table = new StringBuilder();

        Set<String> names = statistics.values().stream().findAny().map(e -> e.transitions.keySet()).get();
        String collect = names.stream().collect(Collectors.joining("&"));
        table.append("& & " + collect  + "\\\\ \\hline \n");

        for (Map.Entry<Integer, Stats> row : statistics.entrySet()) {
            Stats s = row.getValue();
            table.append("\\multirow{4}{*}{" + row.getKey() + "} & States & " + s.states.values().stream().map(df2::format).collect(Collectors.joining(" & "))  +" \\\\[1mm]   \\cline{2-" + (legends.size() + 2) + "} \n ");
            table.append("& Transitions &" + s.transitions.values().stream().map(df2::format).collect(Collectors.joining(" & "))  + " \\\\[1mm]  \\cline{2-" + (legends.size() + 2) + "} \n ");
            table.append(" & Plys & " + s.plys.values().stream().map(df2::format).collect(Collectors.joining(" & "))  + " \\\\[1mm]  \\cline{2-" + (legends.size() + 2) + "} \n ");
            table.append(" & Time (sec) &" + s.compileTime.values().stream().map(df2::format).collect(Collectors.joining(" & "))  + " \\\\[1mm] \\hline \n");
        }


        return new HashMap<String, String>() {{
            put("statstable", table.toString());
            put("columns", IntStream.rangeClosed(1, legends.size() + 2).mapToObj(i -> "c").collect(Collectors.joining("|")));
        }};
    }
}
