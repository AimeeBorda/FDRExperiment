import java.io.IOException;

public class Main {

    public static void main(String[] args){

        try {
            new FDRExperiment(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
