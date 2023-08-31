package com.cbq.knn;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        KNN knn = new KNN();
        KNN.DataVector dataVector = knn.loadTrain("KNN/xl");
        //System.out.println(Arrays.toString(dataVector.getVector()[0]));

        File file = new File(Main.class.getClassLoader().getResource("KNN/test/0-1-test.png").getFile());
        System.out.println(file.getName());
        int[][] ints = knn.imageTrainBit(file);
        int[] ints1 = knn.bitImageTrainVector(ints);
        knn.polo(dataVector,ints1,3);
    }
}