import java.io.File;
import java.util.ArrayList;

/**
 * Created by pavan on 3/21/17.
 */
public class DataClass {
    public String className;
    public int noOfDocs;
    public float prior;
    public ArrayList<Integer> wordCount = new ArrayList<>();
    public ArrayList<Float> conditionalProbability;
    public int totalWordCount;

    DataClass(){
    }
}
