# NeuralNetwork
## Introduction
This applications is a neural network implementation that can interpret 12 x 12 pixel images.
## Usage
For usage instructions please reade the [How To Use](https://github.com/anisjonischkeit/NeuralNetwork/blob/master/How-To-Use.pdf) Document
## Limitations
- You cannot change the number of input nodes. 
- The image files must only consist of star and space characters.
- The image must be 12x12 pixels in size
- You cannot change the activation function or the learning algorithm. 
- The screen will not clear itself after running processes when run on a windows machine.
- Training data files must be named in the convention of NAME.txt
- Training and testing files must have the extension “.txt”
- You cannot have multiple training files for the same image.
- Since random weights are applied to the neural network to begin with, even when training the neural network to a high rms error, about every 1 in 20 times, the network will train itself but not be able to interpret the face with 10% random noise

