/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Brenton Millers and Anis Jonischkeit
 */
public class Weight
{
    //private variables
    private double value;

    //constructor to create a weight object.
    public Weight (double value) 
    {
        this.value = value;
    }
    
    //setter method that sets the weight of the object.
    public void setWeight(double weight)
    {
    	this.value = weight;
    }

    //accessor method that returns the weight of the object.
    public double getWeight()
    {
    	return value;
    }
}