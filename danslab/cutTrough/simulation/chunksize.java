/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.cutTrough.simulation;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class chunksize {

    public static void main(String args[]) {

        System.out.println("Chunk size(KB)\t Propagation Time(S)");
        System.out.println("-----------------------------------------");
        int ChunkSize = 1 * 8 * 1024; //1 KB

        while (ChunkSize < 1024 * 8 * 1024) {

            int NoOfChunks = (int) (Math.ceil((double) ((8 * 1024 * 1024) - 940) / ChunkSize));
            double Ans = 3.64 + ((double)(ChunkSize+512)*NoOfChunks*8) / (100 * 1024 * 1024);
            System.out.println(ChunkSize/(1 * 8 * 1024) + "\t\t" + Ans);
            ChunkSize *= 2;
        }

    }
}
