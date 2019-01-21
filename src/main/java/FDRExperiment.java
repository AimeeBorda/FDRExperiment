import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

class FDRExperiment {

    LinkedHashMap<String, Stats> statistics = new LinkedHashMap<>();
    ArrayList<String> legends = new ArrayList<>();

    public FDRExperiment() throws IOException {

        File[] listOfFiles = new File("/Users/aimee/phd/dissertation/CSP Examples/Stadium").listFiles();
        for (File file : listOfFiles) {

            if (file.getName().startsWith("someFile") && file.getName().endsWith(".json")) {
                parseJSON(file);
            } else if (file.getName().startsWith("otherFile") && file.getName().endsWith(".txt")) {
                parseTXT(file);
            }
        }

        HashMap<String, String> map = GenerateLatex.createLatexFile(legends, statistics);
        modifyFile("/Users/aimee/phd/dissertation/CSP Examples/Stadium/LatexTemplate.s", "R", map);
    }

    private void parseTXT(File listOfFile) throws IOException {
        FileInputStream fis = new FileInputStream(listOfFile);
        byte[] data = new byte[(int) listOfFile.length()];
        fis.read(data);
        fis.close();

        String someFile = listOfFile.getName().replace("otherFile", "").replace(".txt", "");
        Stats s = new Stats();
        if (this.statistics.containsKey(someFile))
            s = this.statistics.get(someFile);
        else {
            statistics.put(someFile, s);
        }

        for (String str : new String(data, "UTF-8").split("Finished")) {
            if(str.contains(" in ")) {
                double execTime = Double.parseDouble(str.substring(str.lastIndexOf("in ") + 3, str.lastIndexOf("seconds")).trim());
                s.compileTime.add(execTime);
            }
        }
    }

    private void parseJSON(File listOfFile) throws IOException {
        FileInputStream fis = new FileInputStream(listOfFile);
        byte[] data = new byte[(int) listOfFile.length()];
        fis.read(data);
        fis.close();

        String someFile = listOfFile.getName().replace("someFile", "").replace(".json", "");

        Stats s = new Stats();
        if (statistics.containsKey(someFile)) {
            s = statistics.get(someFile);
        }else {
            statistics.put(someFile, s);
        }

        boolean setLegends = legends.isEmpty();
        for(String result : new String(data, "UTF-8").split("\\n")) {

                JSONObject next = ((JSONArray) new JSONObject(result).get("results")).getJSONObject(0);
                s.transitions.add(next.getDouble("visited_transitions"));
                s.states.add(next.getDouble("visited_states"));
                s.plys.add(next.getDouble("visited_plys"));

                if (setLegends) {
                    legends.add(formatLegend(next.getString("assertion_string")));
                }
            }

    }

    private String formatLegend(String legend){
        if(legend.contains("[T=")|| legend.contains("[F=") || legend.contains("[FD=")){
            return legend.substring(0,legend.indexOf("["));
        }else{
            return legend.substring(legend.indexOf("[")+1, legend.lastIndexOf("]"));
        }
    }

    private void modifyFile(String filePath, String prefix, HashMap<String, String> def) throws IOException {
        File fileToBeModified = new File(filePath);
        String fileName = fileToBeModified.getAbsolutePath().replace(".s", prefix + ".tex");
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;


        reader = new BufferedReader(new FileReader(fileToBeModified));
        String line = reader.readLine();

        while (line != null) {
            oldContent = oldContent + line + System.lineSeparator();

            line = reader.readLine();
        }
        String newContent = oldContent;
        for (String key : def.keySet()) {
            newContent = newContent.replace("|" + key.toUpperCase() + "|", def.get(key));
        }
        writer = new FileWriter(fileName);
        writer.write(newContent);
        reader.close();

        writer.close();
    }
}