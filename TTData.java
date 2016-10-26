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
public class TTData
{
    //private variables
    private int id;
    private String name;
    private ArrayList<Integer> data; 
    
    //constructor to create a Test/Training data object.
    public TTData(int id, String name, ArrayList<Integer> image)
    {
        this.id = id;
        this.data = image;
        this.name = name;
    }
    
    //accessor method that returns the Test/Training data's ID.
    public int getID()
    {
        return this.id;
    }
    
    //accessor method to return the image data from the Test/Training data object.
    public ArrayList<Integer> getImageData()
    {
        return data;
    }
       
    //accessor method to return the title of the Test/Training data object e.g: circle or cross
    public String getName()
    {
        return name;
    }
}
