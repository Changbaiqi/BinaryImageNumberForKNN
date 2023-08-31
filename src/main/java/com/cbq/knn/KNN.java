package com.cbq.knn;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KNN {


    /**
     * 核心算法
     *
     * @param dataVector 这个是数据集
     * @param image      测试的图片二值化数据
     */
    public void polo(DataVector dataVector, int image[],int K) {
        int[] tables = dataVector.getTables();
        int[][] vector = dataVector.getVector();

        float resMat[][] = new float[vector.length][vector[0].length];
        //数据填充
        for (int y = 0; y < resMat.length; ++y)
            for (int x = 0; x < resMat[y].length; ++x)
                resMat[y][x] = image[x];

        //平方后的值
        float diffMat[][] = new float[vector.length][vector[0].length];
        for (int y = 0; y < resMat.length; ++y)
            for (int x = 0; x < resMat[y].length; ++x)
                diffMat[y][x] =  (float) Math.pow(vector[y][x] - resMat[y][x], 2);


        ArrayList<Point> pointArrayList = new ArrayList<>();

        //距离相加并开平方获得矢量差值
        //float loMat[] = new float[vector.length];
        for (int y = 0; y < resMat.length; ++y) {
            int ans = 0;
            for (int x = 0; x < resMat[y].length; ++x)
                ans += diffMat[y][x];

            pointArrayList.add(new Point(tables[y], (float) Math.sqrt(ans)));
        }

        pointArrayList.sort((a,b)->(int)((a.distance*100)-(b.distance*100)));

        int data[] = new int[100];

        int maxLabAns = 0;
        int maxLab = -1;
        for(int i=0  ; i < K ; ++i){
            ++data[pointArrayList.get(i).lab];
            if(maxLabAns<data[pointArrayList.get(i).lab]){
                maxLabAns = data[pointArrayList.get(i).lab];
                maxLab = pointArrayList.get(i).lab;
            }
        }
        System.out.println("识别数字："+maxLab);
    }

    /**
     * 用于加载所有的训练集
     *
     * @param trainPath
     */
    public DataVector loadTrain(String trainPath) {
        //获取URL
        URL url = this.getClass().getClassLoader().getResource(trainPath);
        File file = new File(url.getPath());
        File[] files = file.listFiles();

        //用于存储标签
        int tab[] = new int[files.length];
        //用于存储数据
        int imageData[][] = new int[files.length][25 * 25];
        for (int i = 0; i < files.length; ++i) {
            File file1 = files[i];
            String name = file1.getName();
            //样例对应的标签数字
            String tabName = name.split("-")[0];
            //System.out.println(name);
            int[][] ints = imageTrainBit(new File(file + "/" + name));
            int[] ints1 = bitImageTrainVector(ints);
            imageData[i] = ints1;
            tab[i] = Integer.parseInt(tabName);
            //System.out.println(Arrays.toString(imageData[i]));
        }

        //将数据装载
        DataVector dataVector = new DataVector();
        dataVector.setVector(imageData);
        dataVector.setTables(tab);
        return dataVector;
    }


    /**
     * 将图片二值化
     *
     * @param file
     */
    public int[][] imageTrainBit(File file) {
        try {

            BufferedImage read = ImageIO.read(file);

            int width = read.getWidth();
            int height = read.getHeight();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(read, 0, 0, null);
            graphics.dispose();


            int threshold = 128;
            //用于存储图片的二值化数组
            int data[][] = new int[height][width];
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    //获取图片指定点的rgb色彩
                    //int rgb = read.getRGB(x,y);
                    int gray = new Color(bufferedImage.getRGB(x, y)).getRed();
                    int binaryPixel = gray < threshold ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
                    //binaryImage.setRGB(x,y,binaryPixel)
                    data[y][x] = gray != 0 ? 1 : 0;
                }
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将二值化的图片转换为一维特征向量值
     *
     * @return
     */
    public int[] bitImageTrainVector(int imageBit[][]) {
        int data[] = new int[imageBit.length * imageBit[0].length];
        for (int y = 0; y < imageBit.length; ++y) {
            for (int x = 0; x < imageBit[y].length; ++x) {
                data[imageBit[y].length * y + x] = imageBit[y][x];
            }
        }
        return data;
    }


    public class DataVector {
        private int[] tables;
        private int[][] vector;

        public int[] getTables() {
            return tables;
        }

        public void setTables(int[] tables) {
            this.tables = tables;
        }

        public int[][] getVector() {
            return vector;
        }

        public void setVector(int[][] vector) {
            this.vector = vector;
        }
    }

    public class Point {
        private int lab;
        private float distance;

        public Point(int lab, float distance) {
            this.lab = lab;
            this.distance = distance;
        }

        public int getLab() {
            return lab;
        }

        public float getDistance() {
            return distance;
        }
    }
}
