import org.json.JSONObject;
import uk.ac.ox.cs.fdr.Assertion;
import uk.ac.ox.cs.fdr.AssertionList;
import uk.ac.ox.cs.fdr.Session;
import uk.ac.ox.cs.fdr.fdr;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Plot {

    private final String[] colors = {"red", "blue", "green", "yellow", "purple", "orange", "black", "brown", "gray", "awesome", "cyan", "azure", "bazaar", "blue-green", "brightube"};

    HashMap hashMap;
    String prefix;
    String fileName;
    int interval;
    int max;
    int min;
    String placeholder;

    ArrayList<StringBuilder> compileTime = null;
    ArrayList<StringBuilder> states = null;
    ArrayList<StringBuilder> transitions = null;

    public Plot(int interval, int max, int min, String placeholder, String fileName, String prefix, HashMap hashMap) {
        this.interval = interval;
        this.max = max;
        this.min = min;
        this.placeholder = placeholder;
        this.fileName = fileName;
        this.prefix = prefix;
        this.hashMap = hashMap;
    }

    public void drawGraph() throws IOException, InterruptedException {
        for (int i = this.min; i <= this.max; i += this.interval) {
            this.hashMap.put(this.placeholder,String.valueOf(i));
            File file = modifyFile(this.fileName,  this.prefix, this.hashMap);

            byCommandLine(file, String.valueOf(i));
            byAPI(file, String.valueOf(i));

            file.delete();
        }

        createLatexFile();

    }

    private void createLatexFile() throws IOException {
        StringBuilder resCompileTime = new StringBuilder();
        StringBuilder resStates = new StringBuilder();
        StringBuilder resTransition = new StringBuilder();
        for (int i = 0; i < compileTime.size(); i++) {
            resCompileTime.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n" + compileTime.get(i).toString() + "};\n");
            resStates.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n" + states.get(i).toString() + "};\n");
            resTransition.append("\\addplot[color=" + colors[i] + ",mark=x] coordinates {\n" + transitions.get(i).toString() + "};\n");
        }
        modifyFile("LatexTemplate.tex", this.prefix, new HashMap<String,String>(){{
            put("compileTime", resCompileTime.toString());
            put("states",resStates.toString());
            put("transitions",resTransition.toString());
        }});
    }

    private void byCommandLine(File file,String xaxis) throws IOException, InterruptedException {

        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "/Applications/FDR4.app/Contents/MacOS/refines \"" + file.getAbsolutePath() + "\" --format=json"});
        p.waitFor();

        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
        JSONObject jsonObj = new JSONObject(b.readLine());

        b.close();
    }

    private void byAPI(File file,String xaxis) {

        Session session = new Session();
        session.loadFile(file.getName());


        AssertionList assertions = session.assertions();
        if (compileTime == null) {
            compileTime = (ArrayList<StringBuilder>) IntStream.rangeClosed(0, assertions.size()).mapToObj(StringBuilder::new).collect(Collectors.toList());
            states = (ArrayList<StringBuilder>) IntStream.rangeClosed(0, assertions.size()).mapToObj(StringBuilder::new).collect(Collectors.toList());
            transitions = (ArrayList<StringBuilder>) IntStream.rangeClosed(0, assertions.size()).mapToObj(StringBuilder::new).collect(Collectors.toList());
        }

        for (int k = 0; k < assertions.size(); k++) {
            Assertion assertion = assertions.get(k);

            double time1 = System.nanoTime();
            assertion.execute(null);
            time1 = (System.nanoTime() - time1) / 1000000.0;


            double time = time1;

            compileTime.get(k).append("(" +xaxis + "," + time + " )\n");
        }

        fdr.libraryExit();
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