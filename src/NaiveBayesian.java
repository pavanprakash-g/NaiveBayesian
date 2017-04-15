import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by pavan on 3/21/17.
 */
public class NaiveBayesian {
    private int docCount;
    private ArrayList<DataClass> classes = new ArrayList<>();
    private ArrayList<String> vocabulary = new ArrayList<>();
    public  ArrayList<String> stopWords = new ArrayList<>();

    public void identifyClasses(File[] files, int directoryCount) {
        try {
            identifyStopWords();
            for (File file : files) {
                if (directoryCount > 0) {
                    DataClass dataClass = new DataClass();
                    dataClass.className = file.getName();
                    if (vocabulary != null) {
                        dataClass.wordCount = new ArrayList<Integer>(Collections.nCopies(vocabulary.size(), 0));
                    }
                    else {
                        dataClass.wordCount = new ArrayList<Integer>();
                    }
                    System.out.println("Directory Name:"+file.getName());
                    processData(dataClass, file);
                    calculatePrior();
                    classes.add(dataClass);
                    directoryCount--;
                }
            }

            calculatePrior();
            findConditionalProbability();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void identifyStopWords(){
        String content;
        try {
            URL url = getClass().getResource("english.txt");
            File file = new File(url.getPath());
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((content = br.readLine()) != null) {
                content = content.trim();
                stopWords.add(content);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void calculatePrior(){
        for(DataClass dataClass : classes){
            dataClass.prior = (float)dataClass.noOfDocs/(float) docCount;
        }
    }

    private void findConditionalProbability(){
        float value = 0;
        int wordCount = 0;
        for(DataClass dataClass : classes) {
            dataClass.conditionalProbability = new ArrayList<Float>(Collections.nCopies(vocabulary.size(), (float)0.0));
            for (int i = 0; i < vocabulary.size(); i++) {
                if(dataClass.wordCount.size() > i){
                    wordCount = dataClass.wordCount.get(i);
                }else{
                    wordCount = 0;
                }
                value = (float)(wordCount +1)/(float)(dataClass.totalWordCount+vocabulary.size());
                dataClass.conditionalProbability.set(i, value);
            }
        }
    }

    public void processData(DataClass dataClass, File file){
        String content;
        BufferedReader br;
        try {
            File[] subFolders = file.listFiles();
            for (int i = 0; i < subFolders.length; i++) {
                br = new BufferedReader(new FileReader(subFolders[i].toString()));
                while ((content = br.readLine()) != null && !content.contains("Lines:")) {
                    //ignoring the header part
                }
                while ((content = br.readLine()) != null) {
                    content = content.replaceAll("[^a-zA-Z]+|^\\s", " ");
                    content = content.trim().replaceAll(" +", " ").toLowerCase();
                    String[] words = content.split(" ");
                    for(String word : words){
                        if (stopWords.contains(word)) {
                            continue;
                        }else {
                            int index = vocabulary.size();
                            if (!vocabulary.contains(word)) {
                                vocabulary.add(word);
                            } else {
                                index = vocabulary.indexOf(word);
                            }
                            if (dataClass.wordCount.size() > index) {
                                dataClass.wordCount.set(index, dataClass.wordCount.get(index) + 1);
                            } else {
                                dataClass.wordCount.add(1);
                            }
                            dataClass.totalWordCount++;
                        }
                    }
                }
                //br.close();
            }
            dataClass.noOfDocs = subFolders.length;
            docCount += subFolders.length;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public float testData(File[] files, int directoryCount){
        int correctCount = 0;
        int totalCount = 0;
        String predictedClass = "";
        for (File file: files) {
            if(directoryCount > 0) {
                File[] docs = file.listFiles();
                for (File doc : docs) {
                    predictedClass = findClass(getWordsData(doc.getAbsolutePath()));
                    if (predictedClass.equals(file.getName())) {
                        correctCount++;
                    } else {
                        //System.out.println("Wrong file Name:" + doc.getName());
                    }

                    totalCount++;
                }
                //System.out.println("Class::" + file.getName() + " CorrectCount:" + correctCount + " TotalCount:" + totalCount);
                directoryCount--;
            }
        }
        return (float) correctCount / totalCount;
    }

    public ArrayList<String> getWordsData(String filePath){
        String content = "";
        BufferedReader br;
        ArrayList<String> finalContent= new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(filePath));
            while ((content = br.readLine()) != null && !content.contains("Lines:")) {
                //ignoring the header part
            }
            while ((content = br.readLine()) != null) {
                content = content.replaceAll("[^a-zA-Z]+|^\\s", " ");
                content = content.trim().replaceAll(" +", " ").toLowerCase();
                finalContent.addAll(Arrays.asList(content.split(" ")));
            }
            //br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return finalContent;
    }

    private String findClass(ArrayList<String> words){
        float prob = 0;
        float maxProb = -1000000000;
        String predictedClass = "";
        for(DataClass dataClass : classes){
            prob += Math.log(dataClass.prior);
            for(String word : words){
                if(stopWords.contains(word)){
                    continue;
                }
                if (!vocabulary.contains(word)) {
                    prob *= 1;
                }else {
                    prob += Math.log(dataClass.conditionalProbability.get(vocabulary.indexOf(word)));
                }
            }
            //System.out.println("Prob::"+prob+" ClassName:"+dataClass.className+" maxProb:"+maxProb+" predicted:"+predictedClass);
            if(prob >= maxProb){
                predictedClass = dataClass.className;
                maxProb = prob;
            }
            prob = 0;
        }
        return predictedClass;
    }
}
