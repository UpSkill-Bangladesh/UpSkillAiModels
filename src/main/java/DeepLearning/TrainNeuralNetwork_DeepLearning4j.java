package DeepLearning;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class TrainNeuralNetwork_DeepLearning4j {
    public static void main(String[] args) throws Exception {
        // Step 1: Load MNIST dataset
        int batchSize = 64; // Number of examples in each batch
        int outputNum = 10; // Number of output classes (digits 0–9)
        int rngSeed = 123; // Random seed for reproducibility
        int epochs = 20; // Number of training iterations
        
        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);
        
        // Step 2: Build the neural network
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
            .seed(rngSeed)
            .updater(new org.nd4j.linalg.learning.config.Adam(0.001)) // Optimizer
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(28 * 28) // Input size (28x28 pixels flattened)
                .nOut(128) // Number of neurons in this layer
                .activation(Activation.RELU)
                .build())
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nIn(128)
                .nOut(outputNum)
                .activation(Activation.SOFTMAX)
                .build())
            .build();
        
        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new ScoreIterationListener(10)); // Log score every 10 iterations

        // Step 3: Train the model
        System.out.println("Training the model...");
        for (int i = 0; i < epochs; i++) {
            model.fit(mnistTrain);
            System.out.println("Completed epoch " + (i + 1) + " of " + epochs);
        }
        
        // Step 4: Evaluate the model
        System.out.println("Evaluating the model...");
        org.deeplearning4j.eval.Evaluation eval = model.evaluate(mnistTest);
        System.out.println(eval.stats());

        // Step 5: Save the model
        System.out.println("Saving the model...");
        model.save(new java.io.File("mnist_model.zip"), true);
    }
    
 /*   Key Steps in the Code
    Dataset Loading:
    MnistDataSetIterator loads the MNIST dataset directly.
    Model Architecture:
    The model consists of one dense layer and one output layer.
    Training:
    The fit method trains the model for the specified number of epochs.
    Evaluation:
    Evaluates the model's performance using test data.
    Model Saving:
    Saves the trained model to a file for future use.
    */
}
