/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Node {

    int Id;
    Chain chain;
    Block Last;
    long NextEventTime;

    Node(Block Genesis, int Id) {

        //next event time intialize
        int NextMA = Sharables.TimeToMine;
        int Minus50 = (int) (NextMA * 0.5);
        int Plus50 = NextMA + Minus50;
        long time = Functions.getRandomNumberInRange(Minus50, Plus50); //generate new time
        NextEventTime = Sharables.startTime + time;

        Last = Genesis;
        chain = new Chain(Genesis);
        this.Id = Id;

    }

    public void init() {
        Scheduler.addEvent(Id, NextEventTime, 1, this);
    }

    public void CallBlack(long EventTime, int BlockNumber) {

//            //mined a new block
        int timeTakentoMine = (int) ((EventTime - Last.TimeStamp));
        int NextMA = (int) (Last.NextMA * ((double) Sharables.TimeToMine / timeTakentoMine));

        //creat the new block
        Block temp = new Block(Last.NodeNumber, Last.NodeNumber + 1, EventTime, NextMA, Id);
        Last = temp;
        chain.add(temp); //add to chain

        //System.out.println("Mined a block and forwarding by node " + Id + " current tree length is " + chain.TreeHeight);
        Broadcast(Last);

    }

    public void NewBlock(Block temp, int HopCount, int ReceivedFrom) {

        if (!chain.exists(temp)) {

            chain.add(temp);
            //System.out.println("Tree height at " + Id + " is " + chain.TreeHeight + " Hopcount: " + HopCount);

            //determining the broadcast time
            if (HopCount > Sharables.MaxHopCount) {
                Sharables.MaxHopCount = HopCount;
            }

            //System.out.println("block received by "+Id);
        }

    }

    void Broadcast(Block temp) {

        class Ntemp {

            int NodeNumber;
            int HopCount;

            public Ntemp(int NodeNumber, int HopCount) {
                this.NodeNumber = NodeNumber;
                this.HopCount = HopCount;
            }
        }

        Queue<Ntemp> queue = new LinkedList<>();

        HashSet<Integer> InQueue = new HashSet<>();
        InQueue.add(Id);

        //add this node connections to the queue
        int MyConnectionsCount = Sharables.ConnectionsCount[Id];

        if (MyConnectionsCount == 0) {
            System.out.println("This node doesnot have connection:" + Id);
            return;
        }

        int delay = (MyConnectionsCount-1)*Sharables.Tdelay + Sharables.LatencyDelay;
        for (int i = 0; i < MyConnectionsCount; i++) {
            int NodeNumber = Sharables.Connections[Id][i];
            Ntemp n = new Ntemp(NodeNumber, delay);
            queue.add(n);
            InQueue.add(NodeNumber);
        }

        //now start the BFS
        while (queue.size() != 0) {

            Ntemp Vertex = queue.remove();
            int NodeNumber = Vertex.NodeNumber;
            int HopCount = Vertex.HopCount;

            //if it is not visited call that Node Newblock function
            Sharables.Nodes[NodeNumber].NewBlock(temp, HopCount, Id);

            //add it connections to the queue
            for (int i = 0; i < Sharables.ConnectionsCount[NodeNumber]; i++) {
                int Itemp = Sharables.Connections[NodeNumber][i];
                if (!InQueue.contains(Itemp)) {
                    Ntemp n = new Ntemp(Itemp, HopCount + (Sharables.ConnectionsCount[NodeNumber]-1)*Sharables.Tdelay + Sharables.LatencyDelay);
                    queue.add(n);
                    InQueue.add(Itemp);
                }
            }

        }
    }
}
