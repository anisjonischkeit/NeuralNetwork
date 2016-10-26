/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import java.math.RoundingMode;

/**
 *
 * @author Brenton Millers and Anis Jonischkeit
 */
public class NeuralNetwork
{
    //global variables
    //array that contains all the layers. This gets bigger as required.
    public static Integer[] layers = {144, 12, null}; 
    //the learning rate
    public static double learningRate = 2;
    //the maximum iterations
    public static int maxIterations = 300;
    //the threashold
    public static double threshold = 0.05;
    //the location of the training data
    public static String inputTrainingData = "Training_Data";
    //the location of the testing data
    public static String inputTestingData = "Test_Data";
    //array that contains all the output from testing or training file.
    public static ArrayList<Double> outPut = new ArrayList<Double>();
    //array that contains all the rms errors for every training set.
    public static ArrayList<Double> rmsErrors = new ArrayList<Double>();
    //scanner object used for reading files.    
    public static Scanner sc = new Scanner(System.in);
    
    //main method that runs all the necessary functions to run and execute the program.
    public static void main(String[] args)
    {
        ArrayList<TTData> trainingData = new ArrayList<TTData>();
        ArrayList<Neuron> neuralNetwork = new ArrayList<Neuron>();
        ArrayList<TTData> testingData = new ArrayList<TTData>();
        
        int selection = 0;
        while (true)
        {
            clearScreen();
            System.out.println("Welcome to Anis and Brenton's Neural Network");
            System.out.println("This program is designed to teach a Neural Network");
            System.out.println("how to recognise 12 x 12 pixel images.\n");
            System.out.println("Main Menu\n");
            System.out.println("1 - Train Neural Network");
            System.out.println("2 - Run Test Data (Individual)");
            System.out.println("3 - Run Test Data (All)");
            System.out.println("4 - Config Menu");
            System.out.println("5 - Exit\n\n");
            System.out.print("Selection: ");
            while(sc.hasNext())
            {
                if (sc.hasNextInt())
                {
                    selection = sc.nextInt();
                    break;
                }
                else
                    sc.next();
            }
            switch(selection)
            {
                //train the neural network
                case 1:
                    if (inputTrainingData != null)
                    {
                        trainingData.clear();
                        trainingData = importData(inputTrainingData, true);
                        System.out.println("--- Neural Network ---");
                        System.out.print("Layers: " + layers[0]);
                        for (int i = 1; i<layers.length; i++)
                        {
                            System.out.print(", " + layers[i]);
                        }
                        System.out.println("\nLearning Rate: " + learningRate);
                        System.out.println("Maximum Iteration: " + maxIterations);
                        System.out.println("Threshold: " + threshold); 
                        System.out.println("Training Data: " + inputTrainingData);
                        System.out.print("\nTraining... ");
                        neuralNetwork = buildNetwork(trainingData);
                        trainNeuralNetwork(neuralNetwork, trainingData);
                        sc.nextLine();
                        System.out.println("Complete in " + rmsErrors.size() + " training iterations");
                        System.out.println("Press enter to continue or press 1 for summary");
                        if (sc.nextLine().equals("1")){
                            printSummary();
                            System.out.println("Press the enter key to return to the main menu.");
                            sc.nextLine();
                        }
                    } 
                    else 
                    {
                        sc.nextLine();
                        System.out.println("Please set training data in the config menu");
                        System.out.println("Press the enter key to return to the main menu.");
                        sc.nextLine();
                    }
                    break;
                //run individual test data
                case 2:
                    if (inputTestingData != null && neuralNetwork.size() > 0) 
                    {
                        testingData.clear();
                        testingData.addAll(importData(inputTestingData, false));
                        for (int i = 0; i < testingData.size(); i++)
                        {
                            System.out.println((i+1) + " - " + testingData.get(i).getName());
                        }
                        System.out.print("Selection: ");
                        if (sc.hasNextInt())
                        {
                            selection = sc.nextInt();
                            ArrayList<TTData> singleTestData = new ArrayList<TTData>();
                            singleTestData.add(testingData.get(selection-1));
                            testNeuralNetwork(neuralNetwork, singleTestData, trainingData);
                            sc.nextLine();
                            System.out.println("Task Complete. Press the enter key to return to the main menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                        sc.next();
                        
                    } 
                    else if (neuralNetwork.size() == 0)
                    {
                        sc.nextLine();
                        System.out.println("Please train the neural network");
                        System.out.println("Press the enter key to return to the main menu.");
                        sc.nextLine();
                    } 
                    else 
                    {
                        sc.nextLine();
                        System.out.println("Please set testing data in the config menu");
                        System.out.println("Press the enter key to return to the main menu.");
                        sc.nextLine();
                    }
                    break;
                //run all the test data
                case 3:
                    if (inputTestingData != null && neuralNetwork.size() > 0) 
                    {
                        testingData.clear();
                        testingData.addAll(importData(inputTestingData, false));
                        testNeuralNetwork(neuralNetwork, testingData, trainingData);
                        sc.nextLine();
                        System.out.println("Task Complete. Press the enter key to return to the main menu.");
                        sc.nextLine();
                    }
                    else if (neuralNetwork.size() == 0)
                    {
                        sc.nextLine();
                        System.out.println("Please train the neural network");
                        System.out.println("Press the enter key to return to the main menu.");
                        sc.nextLine();
                    }
                    else
                    {
                        sc.nextLine();
                        System.out.println("Please set testing data in the config menu");
                        System.out.println("Press the enter key to return to the main menu.");
                        sc.nextLine();
                    }
                    break;
                //open settings menu
                case 4:
                    settingsMenu();
                    break;
                //exit the program
                case 5:
                    clearScreen();
                    System.exit(0);
                    break;
            }
        }
    }
    
    //function that runs the test on the neural network. This can take one file or all files. Once tested it returns
    //what the object actually is and what the neural network thought it was.
    public static void testNeuralNetwork(ArrayList<Neuron> neuralNetwork, ArrayList<TTData> testingData, ArrayList<TTData> trainingData) 
    {
        for (TTData data : testingData)
        {
            outPut.clear();
            forwardPropagation(neuralNetwork, data, false);
            ArrayList<Integer> order = new ArrayList<Integer>();
            for (int i = 0; i < outPut.size(); i++)
            {
                order.add(i);
            }
            for(int i = 1; i < outPut.size(); i++) 
            {
                if (outPut.get(i) > outPut.get(i-1) )
                {
                    double temp = outPut.get(i-1);
                    outPut.set(i-1, outPut.get(i));
                    outPut.set(i, temp);
                    Integer temp2 = order.get(i-1);
                    order.set(i-1, order.get(i));
                    order.set(i, temp2);
                    if (i != 1)
                        i -= 2;
                }
            }
            System.out.println("\n\n");
            printPicture(data);
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            String result = trainingData.get(order.get(0)).getName() + "(" + df.format(outPut.get(0)) + ")";
            for (int i = 1; i < outPut.size() && outPut.get(i) > 0.2; i++)
            {
                result += " -> " + trainingData.get(order.get(i)).getName() + "(" + df.format(outPut.get(i)) + ")";
            }
            System.out.println("Input File Name: " + data.getName());
            System.out.println("Interpreted as: " + result );
            
        }
    }
    
    //function that trains the neural network. it all the necessary calls to the forward and back progogation methods.
    public static void trainNeuralNetwork(ArrayList<Neuron> neuralNetwork, ArrayList<TTData> trainingData)
    {
        rmsErrors.clear();
        double rmsError = 1;
        for (int i = 0; rmsError > threshold && rmsErrors.size() < maxIterations; i++)
        {
            double errors = 0;
            for(TTData data : trainingData)
            {
                errors += forwardPropagation(neuralNetwork, data, true);
                backPropagation(neuralNetwork);
            }
            rmsError = Math.sqrt(errors/(layers[layers.length-1] * layers[layers.length-1]));
            rmsErrors.add(rmsError);
        }
        
    }
    
    //function that prints rms errors, number of iterations taken to the terminal.
    //Additionally it saves the rms errors from each epoch to a csv file in the same dir
    //as the application is being run from.
    public static void printSummary()
    {
        System.out.println("Summary:");
        System.out.println( "Number of training iterations = " + rmsErrors.size());
        String csvFile = "./rmsErrorOutput.csv";
        try
        {
            FileWriter writer = new FileWriter(csvFile);
            for (int i = 0; i < rmsErrors.size(); ++i)
            {
                if (i != 0)
                {
                    writer.append("\n");
                }
                writer.append(rmsErrors.get(i).toString());
            }
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed writing to file. " + e.getMessage());
        }
        
        for (int i = 0; i < rmsErrors.size(); i++)
        {
            System.out.println( "RMS error at training iteration #" + (i+1) + " = " + rmsErrors.get(i) );
        }
    }
    
    //function that performs forward propagation on the neural network.
    public static double forwardPropagation(ArrayList<Neuron> neuralNetwork, TTData data, boolean training)
    {
        //set input layer to current data from image input 
        for(int i = 0; i<layers[0]; i++)
        {
            neuralNetwork.get(i).setNeuronValue(data.getImageData().get(i));
        }
        
        //used to track at which layer we are starting from (in this case we start at the begining of layer 2
        int currentPoint = layers[0];
        
        // this loop loops through each layer (used to get the certain layer's number of neurons )
        for(int k = 1; k<layers.length; k++)
        {    
            
            //loops through all neurons in the current layer(k). starts at the begining of the layer ie. 0 + currentPoint 
            //  where current point is the number of neurons that are before where the layer begins in the array. (eg. at k = 1 currentPoint would
            //  be 144 as position number 143 is the last neuron of the input layer so 144 would be the first of the second layer
            
            for (int i = 0 + currentPoint; i < layers[k] + currentPoint; i++) //0, 1, 2, 3  starts at the 0 pos of each layer then goes until the end of that layer
            {
                double runningTotal = 0;
                
                // loops through each parent neuron of the current neuron in the current layer. (see the output log of all items that have #1  on them to see this demonstrated
                for(Neuron parent : neuralNetwork.get(i).getParents().keySet() )
                {
                    // running total is the value of f( w11*x1 + w21*x2 + w31*x3 + etc ) [lec 8 pg 10]
                    //              (    NeuronValue         *    weight between current neuron and current parrent      )                                                          
                    runningTotal += (parent.getNeuronValue() * neuralNetwork.get(i).getParents().get(parent).getWeight() );
                }
                //use sigmoid function calculate the NeuronValue of the current neuron. set the current neuron's value
                neuralNetwork.get(i).setNeuronValue( 1/( 1+Math.pow(Math.E, -(runningTotal) ) ) );
            }
            currentPoint += layers[k];
        }
        
        //Calculate Errors
        //the output neuron that should be on
        int onNeuron = data.getID(); 
        double rmsTotal = 0;   
        for (int i=0; i < layers[layers.length - 1]; i++)
        {
            int nPos = neuralNetwork.size() - layers[layers.length - 1] + i;
            Neuron n = neuralNetwork.get( nPos );
            if (training)
            {
                double desired = 0.0;
                if (i == onNeuron)
                {
                    desired = 1.0;
                }
                double error = n.setDeltaForOuterNeuron(desired);
                rmsTotal += error * error;
            } 
            else 
            {
                outPut.add(n.getNeuronValue());
            }
            
        }
        return rmsTotal;
    }
    
    //function that performs the backwards propogation on the neural network.
    public static void backPropagation(ArrayList<Neuron> neuralNetwork)
    {  
        // loops through all hiden layers
        for (int i = neuralNetwork.size() - layers[layers.length - 1] - 1; i >= 0 ; i--)
        {
           Neuron n = neuralNetwork.get(i);
           HashMap<Neuron, Weight> children = n.getChildren();
           double total = 0;
           for (Neuron child : children.keySet())
           {
               double weight = children.get(child).getWeight() + learningRate * n.getNeuronValue() * child.getDelta();
               children.get(child).setWeight( weight );
               total += (child.getDelta() * weight);
           }
           n.setDeltaForInnerNeuron(total);
       }
    }
    
    //function that prints the picture to the terminal
    public static void printPicture(TTData data)
    {
        for(int j = 0; j < 12; j++)
        {
            for(int i = 0; i < 12; i++)
            {   
                if (data.getImageData().get(i + (j * 12) ) == 1 )
                {
                    System.out.print("*");
                } 
                else 
                {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
    
    //function that checks the entire folder directory for all the image text files, and then calls importFile() to
    //create all the required TTdata files.
    public static ArrayList<TTData> importData(String args, boolean trainingData)
    {
        //read in every file in folder containing training data
        ArrayList<TTData> ttDataArr = new ArrayList<TTData>();
        File folder = new File(args);
        File[] files = folder.listFiles();
        if (folder.isDirectory()){
            for (File f : files)
            {
                TTData data = importFile(f, ttDataArr.size(), trainingData);
                if (data != null){
                    ttDataArr.add(data);
                }
            }
        } 
        else if (folder.isFile())
        {
            TTData data = importFile(folder, ttDataArr.size(), trainingData);
            if (data != null)
            {
                ttDataArr.add(data);
            }
        }
        if (trainingData)
            layers[layers.length-1] = ttDataArr.size();
        return ttDataArr;
    }
    
    //function that takes a file pointer, an id and a bool to tell whether or not your working with training data
    //or test data, it then reads the file and builds all the TTData objects
    public static TTData importFile(File f, int id, boolean trainingData) 
    {
        TTData data = null;
        String[] temp = f.toString().split("\\.");
        ArrayList<String> name = new ArrayList<String>();       
        if (f.isFile() && (temp.length > 1 ) && temp[temp.length - 1].equals("txt") )
        {
            Scanner sc = null;
            try
            {
                sc = new Scanner(f).useDelimiter("");
                ArrayList<Integer> arr = new ArrayList<Integer>();
                while (sc.hasNext())
                {
                    String out = sc.next();
                    if(out.charAt(0) == '*')
                    {
                        arr.add(1);
                    } 
                    else if (out.charAt(0) == ' ') 
                    {
                        arr.add(0);
                    } 
                }
                String currName;
                if (trainingData)
                {
                     String[] t = temp[temp.length - 2].split("/"); 
                     currName = t[t.length - 1];
                } 
                else
                {
                    String[] t = f.toString().split("/"); 
                    currName = t[t.length - 1];
                }
                data = new TTData(id, currName, arr);
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Failed to read or open file.\n" + e.getMessage());
                return null;
            }
            finally
            {
                if (sc != null)
                {
                    sc.close();
                }
            }
        }
        return data;
    }

    //function that builds the neural network.
    public static ArrayList<Neuron> buildNetwork(ArrayList<TTData> trainingData) 
    {
        ArrayList<Neuron> neuralNetwork = new ArrayList<Neuron>();
        //create Neurons and add them to neural network array
        //all neurons are stored in a single array list
        int count = 0;
        for(int i = 0; i < layers.length; ++i )
        {
            for(int j = 0; j < layers[i]; ++j)
            {
                neuralNetwork.add(new Neuron(count++)); 
            }
        }
        int currentPoint = 0;
        for(int k = 1; k < layers.length; k++)
        {
            //connect input layer to hidden layer                   
            for (int i = 0 + currentPoint; i < layers[k - 1] + currentPoint; i++) //0, 1, 2, 3
            {
                for(int j = layers[k - 1] + currentPoint; j < layers[k - 1] + layers[k] + currentPoint; j++) //4, 5, 6 4+3+0=7
                {
                    Random r = new Random(); 
                    Weight weight = new Weight( -0.5 + r.nextDouble() * 1.0 );
                    neuralNetwork.get(i).addChild(neuralNetwork.get(j), weight);
                    neuralNetwork.get(j).addParent(neuralNetwork.get(i), weight);
                }
            }
            currentPoint += layers[k - 1];
        }
        return neuralNetwork;
    }
    
    //function that clears the screen on any unix terminal.
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    //function that controls the settings menu.
    //it updates all the global variables as they are changed by the user.
    public static void settingsMenu()
    {
        int selection = 0;
        String directory = "";
        while (true)
        {
            clearScreen();
            System.out.println("Config Menu\n");
            System.out.println("1 - Set Number of Hidden Layers");
            System.out.println("2 - Set Threashold");
            System.out.println("3 - Set Learning Rate");
            System.out.println("4 - Set Maximum Training Iterations");
            System.out.println("5 - Set Training Data");
            System.out.println("6 - Set Test Data");
            System.out.println("7 - Return to Main Menu\n\n");
            System.out.print("Selection: ");
            while(sc.hasNext())
            {
                if (sc.hasNextInt())
                {
                    selection = sc.nextInt();
                    break;
                }
                else
                    sc.next();
            }
            switch(selection)
            {
                //set the number of hidden layers
                case 1:
                    int layers1 = 0;
                    clearScreen();
                    System.out.println("Please enter the number of hidden layers\n\n");
                    System.out.print("Number: ");
                    while(sc.hasNext())
                    {
                        if (sc.hasNextInt())
                        {
                            layers1 = sc.nextInt();
                            if (layers1 <=0)
                            {
                                System.out.println("You must have at least one layer.");
                                System.out.print("Number: ");
                                continue;
                            }
                            clearScreen();
                            layers = new Integer[layers1 + 2];
                            layers[0] = 144;
                            for (int i = 1; i <= layers1;)
                            {
                                System.out.print("Enter the number of neurons you want on layer " + i + ": ");
                                int num = sc.nextInt();
                                if (num <= 0)
                                {
                                    System.out.println("You must have at least one neuron every layer.");
                                }
                                else
                                {
                                    layers[i] = num;   
                                    i++;
                                }
                            }
                            sc.nextLine();
                            System.out.println("Layers are now set!");
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                            sc.next();
                    }
                    break;
                //set the threshold
                case 2:
                    clearScreen();
                    System.out.println("Please enter a threshold between 0 (best) and 1 (worst)\n\n");
                    System.out.print("Threshold: ");
                    while(sc.hasNext())
                    {
                        if (sc.hasNextDouble())
                        {
                            threshold = sc.nextDouble();
                            if (threshold <= 0.0)
                            {
                                System.out.println("The threshold must be above 0.0");
                                System.out.print("Threshold: ");
                                continue;
                            }
                            else if (threshold >= 1.0)
                            {
                                System.out.println("The threshold must be below 1.0");
                                System.out.print("Threshold: ");
                                continue;
                            }
                            clearScreen();
                            sc.nextLine();
                            System.out.println("The threshold of " + threshold + " has been set.");
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                            sc.next();
                    }
                    break;
                //set the learning rate
                case 3:
                    clearScreen();
                    System.out.println("Please enter a learning rate above 0.0 \n\n");
                    System.out.print("Learning Rate: ");
                    while(sc.hasNext())
                    {
                        if (sc.hasNextDouble())
                        {
                            learningRate = sc.nextDouble();
                            if (learningRate <= 0.0)
                            {
                                System.out.println("The learning rate must be above 0.0");
                                System.out.print("Learning Rate: ");
                                continue;
                            }
                            clearScreen();
                            sc.nextLine();
                            System.out.println("The learning rate of " + learningRate + " has been set.");
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                            sc.next();
                    }
                    break;
                //set maximum training iterations
                case 4:
                    clearScreen();
                    System.out.println("Please enter the maximum training iterations\n\n");
                    System.out.print("Max Iterations: ");
                    while(sc.hasNext())
                    {
                        if (sc.hasNextInt())
                        {
                            maxIterations = sc.nextInt();
                            if (maxIterations <= 0)
                            {
                                System.out.println("The number of iterations must be above 0");
                                System.out.print("Max Iterations: ");
                                continue;
                            }
                            clearScreen();
                            sc.nextLine();
                            System.out.println("The maximum number of iterations has been set at " + maxIterations);
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                            sc.next();
                    }
                    break;
                //set training data
                case 5:
                    clearScreen();
                    System.out.println("Please enter the directory of the training data\n\n");
                    System.out.print( System.getProperty("user.dir") + "/ $ ");
                    sc.nextLine();
                    while(true)
                    {
                        directory = sc.nextLine();
                        directory = directory.replace("\\", "");
                        File file = new File(directory);
                        if (file.isDirectory())
                        {
                            clearScreen();
                            inputTrainingData = directory;
                            System.out.println("The directory \"" + inputTrainingData + "\" has been set as the location for training data." );
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            //
                            break;
                        }
                        else
                        {
                            System.out.println("The directory you provided is not a valid directory. Please try again.");  
                            System.out.print( System.getProperty("user.dir") + "/ $ ");
                        }  
                    }
                    break;
                //set test data
                case 6:
                    clearScreen();
                    System.out.println("Please enter the directory of the file or folder containing the test data. \n\n");
                    System.out.print( System.getProperty("user.dir") + "/ $ ");
                    sc.nextLine();
                    while(true)
                    {
                        directory = sc.nextLine();
                        directory = directory.replace("\\", "");
                        File file = new File(directory);
                        if (file.isDirectory())
                        {
                            clearScreen();
                            inputTestingData = directory;
                            System.out.println("The directory \"" + inputTestingData + "\" has been set as the location for test data." );
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        } 
                        else if (file.isFile())
                        {
                            clearScreen();
                            inputTestingData = directory;
                            System.out.println("The file \"" + file.getName() + "\" has been set as test data." );
                            System.out.println("Press the enter key to return to the config menu.");
                            sc.nextLine();
                            break;
                        }
                        else
                        {
                            System.out.println("The directory or file you provided is not valid. Please try again.");  
                            System.out.print( System.getProperty("user.dir") + "/ $ ");
                        }  
                    }
                    break;
                //return to main menu
                case 7:
                    if (true)
                        return;
                    break;
            }
        }
    }
}
