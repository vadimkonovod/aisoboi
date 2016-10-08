import java.io.*;
import java.util.*;

/**
 * @author Vadzim_Kanavod
 */
public class BollobashRiordan {
  public static void main(String[] args) throws IOException {
    int n = 4000;
    int m = 16;

    int[][] a = bollobashStepOne(n);
    int[][] b = bollobashStepTwo(a, n, m);

    writeMatrixToFile(a, n);
    writeMatrixToFile(b, n);
  }

  private static int[][] bollobashStepOne(int n) {
    int[][] a = new int[2][n];
    int[] degs = new int[n];

    a[0][0] = 1;
    a[1][0] = 1;
    degs[0] = 2;

    for (int i = 1; i < n; i++) {
      a[0][i] = i+1;
      a[1][i] = 0;
      degs[i] = 1;

      int x = getVertexConnectTo(probabilities(degs, i));

      degs[x-1]+=1;
      a[1][i] = x;
    }
    return a;
  }

  private static int[][] bollobashStepTwo(int[][] a, int n, int m) {
    int[][] b = new int[2][n];
    for (int i = 0; i < a[0].length; i++) {
      b[0][i] = recalculateVertex(a[0][i], m);
      b[1][i] = recalculateVertex(a[1][i], m);
    }
    return b;
  }

  private static int recalculateVertex(int a, int m) {
    return a/m + (a%m > 0 ? 1 : 0);
  }

  private static double[] probabilities(int[] degs, int n) {
    double[] a = new double[n+1];
    double divider = 2*(n+1)-1;
    for (int i = 0; i < n+1; i++) {
      a[i] = degs[i]/divider;
    }
    return a;
  }

  private static int getVertexConnectTo(double[] probabilities) {
    Random r = new Random();
    double rand = r.nextDouble();
    int inc = 0;
    double cumsum = 0;
    for (int i = 0; i < probabilities.length; i++) {
      cumsum += probabilities[i];
      if (rand >= cumsum) {
        inc++;
      } else {
        break;
      }
    }
    return ++inc;
  }

  private static void writeMatrixToFile(int[][] a, int n) throws IOException {
    File f = new File("D:\\Projects\\Solution\\src\\output.txt");
    BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));

    int count = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < n; j++) {
        writer.write(a[i][j] + " ");
        count++;
      }
      writer.newLine();
    }
    writer.newLine();
    System.out.println(count/2);
    writer.close();
  }
}