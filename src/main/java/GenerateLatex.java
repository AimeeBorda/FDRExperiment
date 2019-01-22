import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateLatex {

    private final static String[] colors = {"red", "blue", "green", "yellow", "purple", "orange", "black", "brown", "gray", "awesome", "cyan", "azure", "bazaar", "blue-green", "brightube"};
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public static HashMap<String, String> createLatexFile(TreeMap<String, Stats> statistics) {
        List<String> legends = statistics.values().iterator().next().states.keySet().stream().collect(Collectors.toList());
        HashMap<String, String> result = createGraph(legends,statistics);
        result.putAll(createTable(legends,statistics));

        return result;
    }

    private static HashMap<String, String> createGraph(List<String> legends, TreeMap<String, Stats> statistics) {
        StringBuilder resCompileTime = new StringBuilder();
        StringBuilder resStates = new StringBuilder();
        StringBuilder resTransition = new StringBuilder();
        StringBuilder resPlys = new StringBuilder();


        for (int i = 0; i < legends.size(); i++) {
            String key = legends.get(i);
            resCompileTime.append(" \\addlegendentry{" + key + "} \n \\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resStates.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resTransition.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resPlys.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");

            for (Map.Entry<String, Stats> s : statistics.entrySet()) {
                resCompileTime.append("(" + s.getKey() + "," + df2.format(s.getValue().compileTime.get(key)) + ")\n");
                resStates.append("(" + s.getKey() + "," + s.getValue().states.get(key) + ")\n");
                resTransition.append("(" + s.getKey() + "," + s.getValue().transitions.get(key) + ")\n");
                resPlys.append("(" + s.getKey() + "," + s.getValue().plys.get(key) + ")\n");
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

    private static HashMap<String, String> createTable(List<String> legends, TreeMap<String, Stats> statistics) {
        StringBuilder table = new StringBuilder();

        table.append("& & " +  statistics.keySet().stream().collect(Collectors.joining("&")) +"\\\\ \\hline \n");

        for(Map.Entry<String,Stats> row : statistics.entrySet()){
            Stats s = row.getValue();
            table.append("\\multirow{4}{*}{"+ row.getKey()+"} & States & " + s.states.values().stream().map(df2::format).collect(Collectors.joining(" & "))+ " \\\\[1mm]   \\cline{2-"+(legends.size() + 2)  +"} \n ");
            table.append("& Transitions &" + s.transitions.values().stream().map(df2::format).collect(Collectors.joining(" & "))+ " \\\\[1mm]  \\cline{2-"+(legends.size() + 2) +"} \n ");
            table.append(" & Plys & " + s.plys.values().stream().map(df2::format).collect(Collectors.joining(" & "))+ " \\\\[1mm]  \\cline{2-"+(legends.size() + 2)  +"} \n ");
            table.append(" & Time (ms) &" + s.compileTime.values().stream().map(df2::format).collect(Collectors.joining(" & "))+ " \\\\[1mm] \\hline \n");
        }


        return new HashMap<String, String>() {{
            put("statstable", table.toString());
            put("columns", IntStream.rangeClosed(1, legends.size() + 2).mapToObj(i->"c").collect(Collectors.joining("|")));
        }};
    }
}
