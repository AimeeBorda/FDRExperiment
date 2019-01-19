import org.json.JSONArray;
import org.json.JSONObject;
import uk.ac.ox.cs.fdr.Assertion;
import uk.ac.ox.cs.fdr.AssertionList;
import uk.ac.ox.cs.fdr.Session;
import uk.ac.ox.cs.fdr.fdr;

import java.io.*;
import java.util.*;

class FDRExperiment {

    HashMap hashMap;
    String fileName;
    int interval;
    int max;
    int min;
    String placeholder;

    LinkedHashMap<Integer, Stats> statistics = new LinkedHashMap<>();
    ArrayList<String> legends = new ArrayList<>();

    public FDRExperiment(int interval, int max, int min, String placeholder, String fileName,  HashMap hashMap) {
        this.interval = interval;
        this.max = max;
        this.min = min;
        this.placeholder = placeholder;
        this.fileName = fileName;
        this.hashMap = hashMap;
    }

    public void getData() throws IOException, InterruptedException {

        for (int i = this.min; i <= this.max; i += this.interval) {
            System.out.print("Started by api "+i+" for "+placeholder) ;
            this.hashMap.put(this.placeholder, String.valueOf(i));
            File file = modifyFile(this.fileName, this.placeholder, this.hashMap);

            if (i == this.min) {
                generateLegends(file);
            }
            Stats s = byAPI(file);
            System.out.println(" now moved on to command line") ;
            byCommandLine(file, s);
            statistics.put(i, s);

            file.delete();
        }

        HashMap<String, String> map = GenerateLatex.createLatexFile(legends, statistics);
        modifyFile("LatexTemplate.tex", this.placeholder + this.min + this.max+this.interval, map);

    }

    private void generateLegends(File file) {
        Session session = new Session();
        session.loadFile(file.getName());

        for (Assertion assertion : session.assertions())
            legends.add(assertion.toString().substring(0,assertion.toString().indexOf("[") - 1 ));

    }

    private void byCommandLine(File file, Stats s) throws IOException, InterruptedException {

        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "/Applications/FDR4.app/Contents/MacOS/refines \"" + file.getAbsolutePath() + "\" --format=json"});
        p.waitFor();

        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        JSONObject jsonObj = new JSONObject(b.readLine());
        Iterator<Object> results = ((JSONArray) jsonObj.get("results")).iterator();

        while (results.hasNext()) {
            JSONObject next = (JSONObject) results.next();
            s.transitions.add(next.getDouble("visited_transitions"));
            s.states.add(next.getDouble("visited_states"));
            s.plys.add(next.getDouble("visited_plys"));
        }
        b.close();
    }

    private Stats byAPI(File file) {

        Stats s = new Stats();
        Session session = new Session();
        session.loadFile(file.getName());


        AssertionList assertions = session.assertions();
        s.initializeLists(assertions.size());

        for (Assertion assertion : assertions) {

            double time = System.nanoTime();
            assertion.execute(null);
            time = (System.nanoTime() - time) / 1000000.0;

            s.compileTime.add(time);
        }

        fdr.libraryExit();

        return s;
    }

    private File modifyFile(String filePath, String prefix, HashMap<String, String> def) throws IOException {
        File fileToBeModified = new File(filePath);
        String fileName = fileToBeModified.getName().replace(".", prefix + ".");
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


        return new File(fileName);
    }

}