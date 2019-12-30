/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.cutTrough.simulation;

import java.util.Arrays;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Block {

    int PrevNodeNumber;
    int NodeNumber;
    int NextMA; //next moving average
    long TimeStamp;
    int MinerId;
    int HopCount;

    public Block(int PrevNodeNumber, int NodeNumber, long TimeStamp, int NextMA, int MinerId) {

        this.PrevNodeNumber = PrevNodeNumber;
        this.NodeNumber = NodeNumber;
        this.TimeStamp = TimeStamp;
        this.NextMA = NextMA;
        this.MinerId=MinerId;
    }

    public String toString() {
        String temp = "PrevNodeNumber=" + PrevNodeNumber + " NodeNumber=" + NodeNumber + " TimeStamp=" + TimeStamp + " NextMA=" + NextMA+" MinerId="+MinerId;
        return temp;
    }

}
