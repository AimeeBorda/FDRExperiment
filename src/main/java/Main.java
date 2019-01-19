import java.io.IOException;
import java.util.HashMap;

public class Main {




    public static void main(String[] args){
//        drawGraph(new Plot(5,30,10,"NumSections","Stadium.csp","1",new HashMap(){{put("SectionSize","10"); put("Visitors","20"); }}));

        try {
            new Plot(5,30,10,"SectionSize","Stadium.csp","St",new HashMap(){{ put("NumSections","5"); }}).drawGraph();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
