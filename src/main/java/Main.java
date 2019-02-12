import java.io.IOException;

public class Main {




    public static void main(String[] args){

        try {
            new FDRExperiment("/Users/aimee/downloads/NoMinimization/gallery/");
//            new FDRExperiment("/Users/aimee/downloads/NoMinimization/sections/");
//            new FDRExperiment("/Users/aimee/downloads/NoMinimization/numsections/");


//            new FDRExperiment("/Users/aimee/downloads/WithMinimization/gallery/");
//            new FDRExperiment("/Users/aimee/downloads/WithMinimization/sections/");
//            new FDRExperiment("/Users/aimee/downloads/WithMinimization/numsections/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
