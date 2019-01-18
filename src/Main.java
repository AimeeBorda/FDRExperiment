import uk.ac.ox.cs.fdr.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static class Plot{
        HashMap hashMap;
        String prefix;
        String fileName;
        int interval;
        int max;
        int min;
        String placeholder;
        

        public Plot(int interval, int max, int min, String placeholder, String fileName, String prefix, HashMap hashMap) {
            this.interval = interval;
            this.max = max;
            this.min = min;
            this.placeholder = placeholder;
            this.fileName = fileName;
            this.prefix = prefix;
            this.hashMap = hashMap;
        }



    }

    public static void main(String[] args){

//        drawGraph(new Plot(5,30,10,"NumSections","Stadium.csp","1",new HashMap(){{put("SectionSize","10"); put("Visitors","20"); }}));
        drawGraph(new Plot(1,4,1,"Visitors","ExhibitionArea.csp","Art", new HashMap()));
    }



    public static void drawGraph(Plot p) {


        ArrayList<StringBuilder> values = new ArrayList<>();

        String[] colors = {"red","blue","green","yellow","purple","orange","black","brown","gray","awesome","cyan","azure","bazaar","blue-green","brightube"};
        for (int i = p.min; i <= p.max; i += p.interval) {
            System.out.println("started: "+ i);
            try {

                String fileName = modifyFile(p.fileName,p.placeholder,String.valueOf(i),p.prefix,p.hashMap);
                Session session = new Session();
                session.loadFile(fileName);


                AssertionList assertions = session.assertions();
                for(int k = 0 ; k < assertions.size() ; k++){
                    Assertion assertion = assertions.get(k);
                    if(values.size() <= k){
                        values.add(new StringBuilder());
                    }
                    double time = System.nanoTime();
                    assertion.execute(null);
                    time = (System.nanoTime() - time) / 1000000.0;
                    System.out.println(assertion.toString()  + (assertion.passed() ? " Passed" : " Failed") + " within " + time);
                    values.get(k).append("("+i +","+time +" )\n");
                }
                new File(fileName).delete();
            } catch (InputFileError error) {
                System.out.println(error);
            } catch (FileLoadError error) {
                System.out.println(error);
            }

            fdr.libraryExit();
        }

        StringBuilder res = new StringBuilder();
        for(int i = 0 ; i < values.size() ; i++){
            res.append("\\addplot[color="+colors[i]+",mark=x] coordinates {\n"  + values.get(i).toString() + "};\n");
        }
        modifyFile("LatexTemplate.tex","values",res.toString(),p.prefix,p.hashMap);

        System.out.println("Finished");
    }



    static String modifyFile(String filePath, String placeholder,String newString,String prefix, HashMap<String,String> def) {
        File fileToBeModified = new File(filePath);
        String fileName = fileToBeModified.getName().replace(".", prefix + ".");
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));
             String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }
            String newContent = oldContent.replace("|"+placeholder.toUpperCase()+"|",newString);
            for(String key : def.keySet()){
                newContent = newContent.replace("|"+key.toUpperCase()+"|", placeholder.equalsIgnoreCase(key)? newString : def.get(key));
            }
            writer = new FileWriter(fileName);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Closing the resources

                reader.close();

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }
}
