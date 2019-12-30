/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Sharables {

    static int Latency[][];
    static int NoOfNodes = 0;
    static int TimeToMine = 12000; //in milli seconds
    static int Bandwidth = 100 * 1024 * 1024; //in bits
    static int ChainLength = 1; //longest chain max length
    static int TimeToVerify = 0;
    static int MaxHopCount = 0;
    static long BlockSize = 1024 * 1024 * 8; //in bits
    static int Tdelay = 0;
    static int LatencyDelay = 300;
    static long startTime;
    static int ConnectionsCount[];
    static int Connections[][];
    static Node Nodes[];

}
