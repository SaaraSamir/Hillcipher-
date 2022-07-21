/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hillcipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Hillcipher {
 
    public static void main(String[] args) throws FileNotFoundException, IOException {
         File key = new File("key.txt");
        File plainTextFile = new File("encyption.txt");
        File cipherTextFile = new File("decryption.txt");
    encryptfile(plainTextFile, key);
    decryptfile(cipherTextFile,key);
        
       
    }
    
    
   static int firstPositiveNum(int a) {
    while (a < 0) {
      a += 26;
    }
    a %= 26;
    return a;
  }

    static String encryptionPart(String plain, int[][] key) {
        String cipherText = "";
        int[][] plainMat = new int[1][plain.length()];
        int[][] MutiplyMat = new int[plainMat.length][key[0].length];
        for (int i = 0; i < plainMat[0].length; i++) { //plain text as matrix
            plainMat[0][i] = (plain.charAt(i) - 97);
        }
        for (int i = 0; i < plainMat.length; i++) {
            for (int j = 0; j < key[0].length; j++) {
                for (int x = 0; x < plainMat[0].length; x++) {
                    MutiplyMat[i][j] += plainMat[i][x] * key[x][j];
                }
            }
        }
        for (int i = 0; i < MutiplyMat.length; i++) {
            for (int j = 0; j < MutiplyMat[i].length; j++) {
                
                    MutiplyMat[i][j] = firstPositiveNum(MutiplyMat[i][j]);
                
            }
        }
        for (int i = 0; i < MutiplyMat[0].length; i++) {
            cipherText += (char) (MutiplyMat[0][i] + 97);
        }

        return cipherText;
    }
   
   static String encriptionPlain(String plain ,int[][] key){
       String partofplain ;
      String cipherText="";
       for(int i=0 ; i< plain.length() ;i+=(key.length) ){
           partofplain = plain.substring(i,i+key.length);
           cipherText+= encryptionPart (partofplain,key);
          
       }
       return cipherText;
   }
    static void encryptfile(File plainTextFile ,File key) throws IOException {
        Scanner scannerplain = new Scanner(plainTextFile);
         Scanner scannerKey = new Scanner(key);
         int size = scannerKey.nextInt();
         int[][] keyMat = new int[size][size];
         while (scannerKey.hasNextInt()) {
            for (int i = 0; i < keyMat.length; i++) {
                for (int j = 0; j < keyMat[i].length; j++) {
                    keyMat[i][j] = scannerKey.nextInt();
                }
            }
        }

        File output = new File("encrypt.txt");
        PrintWriter pw = new PrintWriter(output);

        while (scannerplain.hasNext()) {
            pw.println(encriptionPlain(scannerplain.nextLine(),keyMat));
        }

        pw.flush();
        pw.close();
    }
    static int det(int[][] a) {
        int n = a.length;
        if (n == 1) {
            return a[0][0];
        }
        int res = 0;
        int[][] c = new int[n - 1][n - 1];
        for (int col = 0; col < n; col++) {

            for (int i = 1; i < n; i++) {
                int J = 0;
                for (int j = 0; j < n - 1; j++) {
                    if (j == col) {
                        J++;
                    }
                    c[i - 1][j] = a[i][J++];
                }
            }

            res += a[0][col] * det(c) * (col % 2 == 0 ? 1 : -1);
        }
        return res;
    }
    

    static int[][] trans(int[][] a) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = a[j][i];
            }
        }
        return res;
    }
    static int[][] adj(int[][] a) {
        int n = a.length;
        int[][] res = new int[n][n];

        int[][] c = new int[n - 1][n - 1];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                int II = 0;
                for (int i = 0; i < n; i++) {
                    if (i == row) {
                        continue;
                    }
                    int JJ = 0;
                    for (int j = 0; j < n; j++) {
                        if (j == col) {
                            continue;
                        }
                        c[II][JJ] = a[i][j];
                        JJ++;
                    }
                    II++;
                }

                res[row][col] = det(c) * ((row + col) % 2 == 0 ? 1 : -1);
            }
        }

        return trans(res);
    }
    
   static public int inv(int a) {
    for (int i = 1; i < 26; i++) {
      if ((a * i) % 26 == 1) {
        return i;
      }
    }
    return -1;
  }
   
    static int[][] inverse(int[][]mat){
        int [][] adjmat =adj(mat);
        int deter = det(mat);
         int deterinv =inv(firstPositiveNum(deter));
        int[][] invMat = new int[mat.length][mat[0].length];
        for (int i = 0; i < adjmat.length; i++) {
            for (int j = 0; j < adjmat[i].length; j++) {
                invMat[i][j] =  firstPositiveNum(adjmat[i][j] * deterinv);

            }
        }
        System.out.println(invMat);
        
        return invMat;
    }
    static String decryptionPart(String cipher, int[][] key) {
        String plainText = "";
        int[][] cipherMat = new int[1][cipher.length()];
        int[][] MutiplyMat = new int[cipherMat.length][key[0].length];
        int[][] keyinv = inverse(key);
        for (int i = 0; i < cipherMat[0].length; i++) { // covert ciphertext to matrix
            cipherMat[0][i] = (cipher.charAt(i) - 97);
        }
        for (int i = 0; i < cipherMat.length; i++) {
            for (int j = 0; j < keyinv[0].length; j++) {
                for (int x = 0; x < cipherMat[0].length; x++) {

                    MutiplyMat[i][j] += cipherMat[i][x] * keyinv[x][j];
                }
            }
        }
        for (int i = 0; i < MutiplyMat.length; i++) {
            for (int j = 0; j < MutiplyMat[i].length; j++) {
                MutiplyMat[i][j] = firstPositiveNum(MutiplyMat[i][j]);
            }
        }
       
        for (int i = 0; i < cipherMat[0].length; i++) {
            plainText += (char) (MutiplyMat[0][i] + 97);
        }
        return plainText;

    }
    
  static String decryptionPlain(String cipher ,int[][] key){
       String partofcipher ;
      String plainText="";
       for(int i=0 ; i< cipher.length() ;i+=(key.length) ){
           partofcipher = cipher.substring(i,i+key.length);
           plainText+= decryptionPart (partofcipher,key);
          
       }
       return plainText;
   }
  
  static void decryptfile(File cipherTextFile ,File key) throws IOException {
        Scanner scannercipher = new Scanner(cipherTextFile);
         Scanner scannerKey = new Scanner(key);
         int size = scannerKey.nextInt();
         int[][] keyMat = new int[size][size];
         while (scannerKey.hasNextInt()) {
            for (int i = 0; i < keyMat.length; i++) {
                for (int j = 0; j < keyMat[i].length; j++) {
                    keyMat[i][j] = scannerKey.nextInt();
                }
            }
        }

        File output = new File("decrypt.txt");
        PrintWriter pw = new PrintWriter(output);

        while (scannercipher.hasNext()) {
            pw.println(decryptionPlain(scannercipher.nextLine(),keyMat));
        }

        pw.flush();
        pw.close();
    }
 
}
