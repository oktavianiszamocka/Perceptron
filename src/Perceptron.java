
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Perceptron {
    public List<Point> dataTrain;
    public List<Point> dataTest;
    public List<Point> Userdata;
    public List<Double> weight;
    public File train;
    public File test;
    public double threshold = 0.1;
    public double learningRate;
    public int epochRate;

    public Perceptron(String fileTrain, String fileTest){
        train = new File(fileTrain);
        test = new File(fileTest);
        instruction();
    }

    public void instruction(){
        dataTrain = new ArrayList<>();
        dataTest = new ArrayList<>();
        Userdata = new ArrayList<>();

        readFile(train, dataTrain);
        readFile(test, dataTest);
        getInput();
        weight = setweighttonull();
        learning();

        System.out.println("final weight :" + weight);
        System.out.println("final thresold: " + threshold);

        testing(dataTest);
        System.out.println("\ntesting user input....");
        testUser(Userdata);
        System.out.println();


    }

    public void getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert learning rate");
        learningRate = scanner.nextDouble();
        System.out.println("Insert epoch rate");
        epochRate = scanner.nextInt();
        List<Double> userValues = new ArrayList<>();
        for(int i = 0; i <dataTrain.get(0).getValue().size(); i++){
            System.out.println("Insert data");
            double data = scanner.nextDouble();
            userValues.add(data);
        }

        Userdata.add(new Point(userValues,  0));
        scanner.close();
    }

    public static void readFile(File file, List<Point> list) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            String line;

            while( (line = input.readLine()) != null) {
                inputParse(line, list);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void inputParse(String line, List<Point> list) {
        List <Double> listofValue = null;
        String [] output = line.split(",");
        int y = 0;
        listofValue = new ArrayList<>();
        for(int i = 0; i< output.length; i++) {
            if(i != output.length-1) {
                listofValue.add(new Double(Double.parseDouble(output[i])));
            }
            else {
                if((!output[i].equals("0")) && (!output[i].equals("1"))){
                    if( output[i].equals("Iris-versicolor")){
                        y = 0;
                    } else {
                        y = 1;
                    }
                } else {
                    y = Integer.parseInt(output[i]);
                }
            }
        }
        list.add(new Point(listofValue, y));
    }


    public List<Double> setweighttonull(){
        List<Double> weightnull = new ArrayList<>();
        for(int i = 0; i < dataTrain.get(0).getValue().size(); i++){
            weightnull.add(new Double(0));
        }
        return weightnull;
    }

    public int getOutput(double sum){
        int y = 0;
        if(sum >= threshold ) {
            y = 1;
        }
        return y;
    }

    public double countNet(double x, double weight){
        return x*weight;
    }

    public double countDeltaRule(double weight, int desiredOutput, int output, double alpha, double x){
        return weight + ((desiredOutput-output) * alpha * x);
    }

    public void learning(){

        List<Double> newWeight = new ArrayList<>();
        for(int n = 0; n < epochRate; n++){

            //epoch (learning dataTRAIN many times

            for(int i = 0; i < dataTrain.size(); i++){
                double sum = 0;

                for(int j = 0; j <dataTrain.get(i).getValue().size(); j++){
                    sum += countNet(dataTrain.get(i).getValue().get(j), weight.get(j));
                }

                int y = getOutput(sum);
                if(dataTrain.get(i).getOutput() == y){

                } else {
                    weight.add(threshold);
                    dataTrain.get(i).getValue().add(new Double(-1));

                    for(int k = 0; k <dataTrain.get(i).getValue().size(); k++){
                        double w = countDeltaRule(weight.get(k), dataTrain.get(i).getOutput(), y, learningRate,  dataTrain.get(i).getValue().get(k));
                        newWeight.add(w);
                    }

                    threshold = newWeight.get(newWeight.size()-1); //get new threshold
                    newWeight.remove(newWeight.size()-1); //remove thresold from weight
                    dataTrain.get(i).getValue().remove(dataTrain.get(i).getValue().size()-1); //remove -1 from x vector
                    weight = newWeight; //assigning new weight
                    newWeight = new ArrayList<>(); //empty new weight
                }
            }
        }

    }

    public void testing(List<Point> Test){

        double tp = 0;
        double fn = 0;
        double fp = 0;
        double tn = 0;
        for(int i = 0; i < Test.size(); i++){
            double sum = 0;

            for(int j = 0; j <Test.get(i).getValue().size(); j++){
                sum += countNet(Test.get(i).getValue().get(j), weight.get(j));
            }
            int y = getOutput(sum);

            if(Test.get(i).getOutput() == 0 && y == 0){
                tp++;
            }
            if(Test.get(i).getOutput() == 0 && y == 1){
                fn++;
            }
            if(Test.get(i).getOutput() == 1 && y == 0){
                fp++;
            }
            if(Test.get(i).getOutput() == 1 && y == 1){
                tn++;
            }

        }
        System.out.println("tp :" + tp + " tn :" + tn + " fn:" + fn + " fp: " + fp);
        System.out.println("accurancy : " + getAccuracy(tp, fn, fp, tn));
        System.out.println("precission : " + getPrecission(tp, fp));
        System.out.println("recall : " + getRecall(tp, fn));

    }

    public void testUser(List<Point> Test){

            for(int i = 0; i < Test.size(); i++){
                double sum = 0;

                for(int j = 0; j <Test.get(i).getValue().size(); j++){
                    sum += countNet(Test.get(i).getValue().get(j), weight.get(j));
                }

                int y = getOutput(sum);
                System.out.println("the output " + getUserOutput(y));

            }

        }

    public int getUserOutput(int a){
        return a;
    }

    public double getAccuracy(double  tp, double fn, double  fp, double tn ){
        return (tp + tn) / (tp+ fn + fp + tn);
    }

    public double getPrecission(double  TP, double  FP){
        return TP/(TP+FP);
    }

    public double getRecall (double  TP, double FN){
        return TP/(TP+FN);
    }
}
