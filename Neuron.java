/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*;
/**
 *
 * @author Brenton Millers and Anis Jonischkeit
 */
public class Neuron implements Comparable
{
    //local variables
    private int id;
    private double neuronValue = 0.0;
    private double delta = 0.0;
    private HashMap<Neuron, Weight> parents = new HashMap<Neuron, Weight>();
    private HashMap<Neuron, Weight> children = new HashMap<Neuron, Weight>();

    //constructor for a neuron
    public Neuron(int id)
    {
        this.id = id;
    }
    
    //setter method to add a parent to the neuron.
    public void addParent(Neuron n, Weight w)
    {
        parents.put(n, w);
    }

    //setter method to add a child to the neuron.
    public void addChild(Neuron n, Weight w)
    {
        children.put(n, w);
    }

    //accessor method to get all parent neurons from the current neuron
    public HashMap<Neuron, Weight> getParents()
    {
        return parents;
    }

    //accessor method to get all child neurons from the current neuron
    public HashMap<Neuron, Weight> getChildren()
    {
        return children;
    }
    
    //accessor to return the neuron's value
    public double getNeuronValue()
    {
        return neuronValue;
    }
    
    //setter method to set the neuron's value
    public void setNeuronValue(double neuronValue)
    {
        this.neuronValue = neuronValue;
    }

    //getter method to access the neuron's id as a string
    public String toString()
    {
        return Integer.toString(id);
    }
    
    //getter method to access the neuron's id as an integer
    public int getID()
    {
        return this.id;
    }
    
    //method used to set the delta value if this neoron is part of the output layer.
    public double setDeltaForOuterNeuron(double desired)
    {
        //sets delta and returns the error
        double error = desired - this.neuronValue;
        this.delta = this.neuronValue*(1-this.neuronValue)*error;
        return error;
    }
    
    //method used to set the delta value if this neoron is an internal hidden node.
    public void setDeltaForInnerNeuron(double sumOfDeltaByWeightForAllChildren)
    {
        //sets delta
        this.delta = this.neuronValue * ( 1 - this.neuronValue ) * sumOfDeltaByWeightForAllChildren; 
    }
    
    //getter method to return the current delta value of the node.
    public double getDelta()
    {
        return this.delta;
    }
    
    //override the compareto method to sort hashmaps containing neurons.
    @Override
    public int compareTo(Object arg0)
    {
        Neuron n = (Neuron)(arg0);
        if (this.getID() < n.getID())
        {
            return -1;
        }
        else if (this.getID() > n.getID())
        {
            return 1;
        }
        return 0;
    }
}