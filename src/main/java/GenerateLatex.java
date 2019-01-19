import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenerateLatex {

    private final static String[] colors = {"red", "blue", "green", "yellow", "purple", "orange", "black", "brown", "gray", "awesome", "cyan", "azure", "bazaar", "blue-green", "brightube"};
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public static HashMap<String, String> createLatexFile(List<String> legends, LinkedHashMap<Integer, Stats> statistics) {
        HashMap<String, String> result = createGraph(legends, statistics);
        result.putAll(createTable(legends,statistics));

        return result;
    }

    private static HashMap<String, String> createGraph(List<String> legends, LinkedHashMap<Integer, Stats> statistics) {
        StringBuilder resCompileTime = new StringBuilder();
        StringBuilder resStates = new StringBuilder();
        StringBuilder resTransition = new StringBuilder();
        StringBuilder resPlys = new StringBuilder();


        for (int i = 0; i < legends.size(); i++) {
            resCompileTime.append(" \\addlegendentry{" + legends.get(i) + "} \n \\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resStates.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resTransition.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");
            resPlys.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n");

            for (Map.Entry<Integer, Stats> s : statistics.entrySet()) {
                resCompileTime.append("(" + s.getKey() + "," + s.getValue().compileTime.get(i) + ")\n");
                resStates.append("(" + s.getKey() + "," + s.getValue().states.get(i) + ")\n");
                resTransition.append("(" + s.getKey() + "," + s.getValue().transitions.get(i) + ")\n");
                resPlys.append("(" + s.getKey() + "," + s.getValue().plys.get(i) + ")\n");
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

    private static HashMap<String, String> createTable(List<String> legends, LinkedHashMap<Integer, Stats> statistics) {
        StringBuilder table = new StringBuilder();

        table.append("& & " + legends.stream().collect(Collectors.joining("&")) +"\\\\ \\hline \n");

        for(Map.Entry<Integer,Stats> row : statistics.entrySet()){
            Stats s = row.getValue();
            table.append("\\multirow{4}{*}{"+ row.getKey()+"} & States & " + s.states.stream().map(df2::format).collect(Collectors.joining("&"))+ "\\\\[1mm]   \\cline{2-"+(legends.size() + 2)  +"} \n ");
            table.append("& Transitions &" + s.transitions.stream().map(df2::format).collect(Collectors.joining("&"))+ "\\\\[1mm]  \\cline{2-"+(legends.size() + 2) +"} \n ");
            table.append(" & Plys & " + s.plys.stream().map(df2::format).collect(Collectors.joining("&"))+ "\\\\[1mm]  \\cline{2-"+(legends.size() + 2)  +"} \n ");
            table.append(" & Time &" + s.compileTime.stream().map(df2::format).collect(Collectors.joining("&"))+ "\\\\[1mm] \\hline \n");
        }


        return new HashMap<String, String>() {{
            put("statstable", table.toString());
            put("columns", IntStream.rangeClosed(1, legends.size() + 2).mapToObj(i->"c").collect(Collectors.joining("|")));
        }};
    }
}
