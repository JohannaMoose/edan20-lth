package lppp.labb3.wekaglue;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;

/**
 * Created by Johanna on 2015-09-23.
 */
public class WekaGlue {

    Classifier classifier;
    Instances dataset;
    Instance instance;

    public int create(String modelName, String datasetName) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(modelName));
            classifier = (Classifier) is.readObject();
            dataset = new Instances(new FileReader(datasetName), 0);
            dataset.setClassIndex(dataset.numAttributes() - 1);

            instance = new DenseInstance(dataset.numAttributes());
            instance.setDataset(dataset);

            System.err.println("Loaded classifier.");
            System.err.flush();

            is.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.flush();
            return -1;
        }
    }

    public int getNAttributes() {
        try {
            return dataset.numAttributes() - 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.flush();
            return -1;
        }
    }

    public String classify(String[] arguments) {
        try {
            for (int i = 0; i < arguments.length; i++) {
                try {
                    instance.setValue(i, arguments[i]);
                } catch (IllegalArgumentException e) {
                    System.err.println("Couldn't use argument " + i + " to classifier: " + arguments[i]);
                    System.err.println("Probably not defined in ARFF header.");
                    e.printStackTrace();
                    System.err.flush();
                    return null;
                }
            }
            double d = classifier.classifyInstance(instance);
            return dataset.classAttribute().value((int) d);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.flush();
            return null;
        }
    }
}