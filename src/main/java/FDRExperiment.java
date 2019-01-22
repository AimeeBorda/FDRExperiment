import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;

//import org.json.JSONObject;

class FDRExperiment {

    TreeMap<String, Stats> statistics = new TreeMap<>();

    public FDRExperiment() throws IOException {

        File[] listOfFiles = new File("/Users/aimee/phd/dissertation/CSP Examples/Stadium").listFiles();
        for (File file : listOfFiles) {

            if (file.getName().startsWith("someFile") && file.getName().endsWith(".json")) {
                parseJSON(file);
            } else if (file.getName().startsWith("otherFile") && file.getName().endsWith(".txt")) {
                parseTXT(file);
            }
        }

        HashMap<String, String> map = GenerateLatex.createLatexFile(statistics);
        modifyFile("/Users/aimee/phd/dissertation/CSP Examples/Stadium/LatexTemplate.s", "R", map);
    }

    private void parseTXT(File listOfFile) throws IOException {
        FileInputStream fis = new FileInputStream(listOfFile);
        byte[] data = new byte[(int) listOfFile.length()];
        fis.read(data);
        fis.close();

        String regex = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
        String someFile = listOfFile.getName().replace("otherFile", "").replace(".txt", "");
        someFile = someFile.replaceAll("(?<=[A-Z])(?=[A-Z])|(?<=[a-z])(?=[A-Z])|(?<=\\D)$", "1");
        String[] atoms = someFile.split(regex);
        String size = atoms[0];
        String req = atoms[1];

        Stats s = new Stats();
        if (this.statistics.containsKey(size))
            s = this.statistics.get(size);
        else {
            statistics.put(size, s);
        }
        String str = new String(data, "UTF-8");
        str = str.substring(str.indexOf("user") + 4, str.indexOf("sys")).trim();
        String[] time = str.split("[ms]");
        double execTime = Integer.parseInt(time[0])*60 + Double.parseDouble(time[1]);
        s.compileTime.put(req, execTime);
    }

    private void parseJSON(File listOfFile) throws IOException {
        FileInputStream fis = new FileInputStream(listOfFile);
        byte[] data = new byte[(int) listOfFile.length()];
        fis.read(data);
        fis.close();


        String regex = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";
        String someFile = listOfFile.getName().replace("someFile", "").replace(".json", "");
        someFile = someFile.replaceAll("(?<=[A-Z])(?=[A-Z])|(?<=[a-z])(?=[A-Z])|(?<=\\D)$", "1");
        String[] atoms = someFile.split(regex);
        String size = atoms[0];
        String req = atoms[1];
        Stats s = new Stats();
        if (statistics.containsKey(size)) {
            s = statistics.get(size);
        } else {
            statistics.put(size, s);
        }

        String source = new String(data, "UTF-8");
        JSONObject next = ((JSONArray) new JSONObject(source).get("results")).getJSONObject(0);
        s.transitions.put(req, next.getDouble("visited_transitions"));
        s.states.put(req, next.getDouble("visited_states"));
        s.plys.put(req, next.getDouble("visited_plys"));

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