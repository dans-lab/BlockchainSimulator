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
public class Sharables {

    static int Latency[][];
    static int NoOfNodes = 0;
    static int Cm=8;
    static int TimeToMine = 12000; //in milli seconds
    static int Bandwidth = 100 * 1024 * 1024; //in bits
    static int ChainLength = 1; //longest chain max length
    static int TimeToVerify = 0;
    static int MaxTimeTaken = 0;
    static long BlockSize = 1024 * 1024 * 8; //in bits
    static int LatencyDelay = 300;
    static long startTime;
    static int ConnectionsCount[];
    static int Connections[][];
    static Node Nodes[];
    static int HeaderSize = 904; //in bits
    static int ChunkSize = 4*8*1024;
    static int SignatureSize = 512;

    static void initVariables() {
        NoOfChunks = (int) (Math.ceil((double)(BlockSize - HeaderSize) / ChunkSize));
        Tdelay = (int) (((double) (BlockSize * 1000) / Bandwidth)); //in millis
        ChunkTdelay = (int) (((double) ((ChunkSize + SignatureSize) * 1000) / Bandwidth));
        HeaderTdelay = (int) (((double) (HeaderSize * 1000) / Bandwidth));
        MaxTimeTaken = 0;
    }
    static int NoOfChunks;
    static int Tdelay;
    static int ChunkTdelay;
    static int HeaderTdelay;
}
