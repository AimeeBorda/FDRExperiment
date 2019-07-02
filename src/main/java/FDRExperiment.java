import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.TreeMap;

class FDRExperiment {

    TreeMap<Integer, Stats> statistics = new TreeMap<>();



    public FDRExperiment(String dirName) throws IOException {

        File[] listOfFiles = new File(dirName).listFiles();
        for (File file : listOfFiles) {

            if (file.getName().endsWith(".json")) {
                parseJSON(file);
            } else if (file.getName().endsWith(".txt")) {
                parseTXT(file);
            }
        }

        HashMap<String, String> map = GenerateLatex.createLatexFile(statistics);
        modifyFile( map,dirName+"latexFile.tex");
    }

    private void parseTXT(File listOfFile) throws IOException {
        FileInputStream fis = new FileInputStream(listOfFile);
        byte[] data = new byte[(int) listOfFile.length()];
        fis.read(data);

        fis.close();

        String someFile = listOfFile.getName().replace(".txt", "");
        int size = Integer.parseInt(someFile.replaceAll("[a-zA-Z]",""));
        String req = someFile.substring(someFile.lastIndexOf(String.valueOf(size))).replaceAll("[0-9]","");

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


        String someFile = listOfFile.getName().replace(".json", "");
        int size = Integer.parseInt(someFile.replaceAll("[a-zA-Z]",""));
        String req = someFile.substring(someFile.lastIndexOf(String.valueOf(size))).replaceAll("[0-9]","");
        Stats s = new Stats();

        if (statistics.containsKey(size)) {
            s = statistics.get(size);
        } else {
            statistics.put(size, s);
        }

        String source = new String(data, "UTF-8");
        System.out.println(someFile);
        JSONObject jsonObject = new JSONObject(source);

        JSONObject next = ((JSONArray) jsonObject.get("results")).getJSONObject(0);

        s.transitions.put(req, next.getDouble("visited_transitions"));
        s.states.put(req, next.getDouble("visited_states"));
        s.plys.put(req, next.getDouble("visited_plys"));

    }


    private void modifyFile(HashMap<String, String> def,String fileName) throws IOException {
        File fileToBeModified = new File("LatexTemplate.tex");
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