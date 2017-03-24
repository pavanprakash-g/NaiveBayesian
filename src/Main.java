import java.io.File;

/**
 * Created by pavan on 3/21/17.
 */
public class Main {
    public static void main(String[] args){
        String trainingPath = args[0];
        File[] files = new File(trainingPath).listFiles(File::isDirectory);
        NaiveBayesian naiveBayesian = new NaiveBayesian();
        long startTime = System.currentTimeMillis();
        naiveBayesian.identifyClasses(files, 5);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken for Training = " +(endTime - startTime)/1000+ " seconds");
        startTime = System.currentTimeMillis();
        String testPath = args[1];
        //String testPath = trainingPath;
        files = new File(testPath).listFiles(File::isDirectory);
        float accuracy = naiveBayesian.testData(files, 5)*100;
        endTime = System.currentTimeMillis();
        System.out.println("Time taken for Testing = " +(endTime - startTime)/1000+ " seconds");
        System.out.println("Testing Accuracy:"+accuracy);
    }
}
