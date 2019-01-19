import java.io.IOException;
import java.util.HashMap;

public class Main {




    public static void main(String[] args){
//        getData(new FDRExperiment(5,30,10,"NumSections","Stadium.csp","1",new HashMap(){{put("SectionSize","10"); put("Visitors","20"); }}));

        try {
            new FDRExperiment(5,30,10,"SectionSize","Stadium.csp",new HashMap(){{ put("NumSections","5"); }}).getData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
