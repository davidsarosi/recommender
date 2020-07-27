package com.company;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixDimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;


import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws MatrixDimensionMismatchException {
        Scanner finput;
        finput = new Scanner(System.in);
        int rowcount = finput.nextInt();
        int usercount = finput.nextInt();
        int bookcount = finput.nextInt();


        double[][] simplematrix = new double[usercount][bookcount];

        for (int i = 0; i < rowcount; i++) {
            simplematrix[finput.nextInt()][finput.nextInt()] = finput.nextInt();
        }
        RealMatrix matrix = new Array2DRowRealMatrix(simplematrix);
        int[][] boolmatrix = new int[usercount][bookcount];


        for (int i = 0; i < usercount; i++) {
            for (int k = 0; k < bookcount; k++) {
                boolmatrix[i][k] = matrix.getEntry(i, k) == 0 ? 0 : 1;
            }
        }


        int magicfactor = 4;

        RealMatrix a = new Array2DRowRealMatrix(usercount, magicfactor);
        randomize(usercount, magicfactor, a);
        RealMatrix b = new Array2DRowRealMatrix(magicfactor, bookcount);
        randomize(magicfactor, bookcount, b);
        RealMatrix youngera = new Array2DRowRealMatrix(usercount, magicfactor);
        RealMatrix youngerb = new Array2DRowRealMatrix(magicfactor, bookcount);
        RealMatrix result = new Array2DRowRealMatrix(usercount, bookcount);
        for (int i = 0; i < 5500; i++) {
            youngera = a;
            youngerb = b;
            mask(usercount, bookcount, a.multiply(b).subtract(matrix), boolmatrix);
            a = a.add(youngerb.transpose().multiply(result).scalarMultiply(2).subtract(youngera.scalarMultiply(0.05f)).scalarMultiply(0.0005f));
            b = b.add(youngera.transpose().multiply(result).scalarMultiply(2).subtract(youngerb.scalarMultiply(0.05f)).scalarMultiply(0.0005f));


        }

        RealMatrix final_prediction = a.multiply(b);
        mask(usercount, bookcount, final_prediction, boolmatrix);


        int x = 0;
        int y = 0;
        double current = 0;

        int konyvek[] = new int[usercount * bookcount];


        int start = 0;

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < usercount; i++) {
                for (int k = 0; k < 200; k++) {
                    if (final_prediction.getEntry(i, k) > current) {
                        current = final_prediction.getEntry(i, k);
                        x = i;
                        y = k;
                    }
                }
                konyvek[start++] = y;
                final_prediction.setEntry(x, y, 0);
                current = 0;
            }
        }
        start=0;
        for (int i=0; i<usercount;i++){
            for (int k=0;k<10;k++) {
                System.out.print(konyvek[start++]);
                System.out.print('\t');
            }
            System.out.println();
        }


    }

    static void randomize(int row, int col, RealMatrix matrix) {
        Random random = new Random();
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                matrix.setEntry(i, k, random.nextDouble());
            }
        }
    }

    static void mask(int row, int col, RealMatrix m, int[][] n) {
        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                m.setEntry(i, k, (m.getEntry(i, k) * n[i][k]));
            }
        }
    }

}
