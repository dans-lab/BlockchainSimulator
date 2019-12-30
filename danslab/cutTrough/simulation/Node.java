/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.cutTrough.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class Node {

    int Id;
    long NextEventTime;
    long CurrentHeader = 0;
    int ReceivedFrom = -1;
    int Delay = 0;

    Node(Block Genesis, int Id) {

        //next event time intialize
        int NextMA = Sharables.TimeToMine;
        int Minus50 = (int) (NextMA * 0.5);
        int Plus50 = NextMA + Minus50;
        long time = Functions.getRandomNumberInRange(Minus50, Plus50); //generate new time
        NextEventTime = Sharables.startTime + time;

        this.Id = Id;

    }

    public void init() {
        Scheduler.addEvent(Id, NextEventTime, 1, this);
    }

    public void CallBlack(long EventTime, int BlockNumber) {

        //System.out.println("Mined a block and forwarding by node " + Id + " current tree length is " + chain.TreeHeight);
        Broadcast(new Random().nextLong());

    }

    public void NewData(long Header, int TimeTaken, int ReceivedFrom) {

        if (CurrentHeader == 0) {
            CurrentHeader = Header;
            this.Delay = TimeTaken;
            this.ReceivedFrom = ReceivedFrom;
            if (Sharables.MaxTimeTaken < Delay) {
                Sharables.MaxTimeTaken = Delay;
            }
        } else if (CurrentHeader == Header && this.ReceivedFrom == ReceivedFrom) {
            Delay = Delay + TimeTaken;
            if (Sharables.MaxTimeTaken < Delay) {
                Sharables.MaxTimeTaken = Delay;
            }

        }
    }

    void Broadcast(long BlockHash) {

        class Ntemp {

            int NodeNumber;
            int TimeTaken;
            int Parent;

            public Ntemp(int NodeNumber, int TimeTaken, int Parent) {
                this.NodeNumber = NodeNumber;
                this.TimeTaken = TimeTaken;
                this.Parent = Parent;
            }
        }

        for (int k = 0; k < 1; k++) { //one more for header

            Queue<Ntemp> queue = new LinkedList<>();

            HashSet<Integer> InQueue = new HashSet<>();
            InQueue.add(Id);

            //add this node connections to the queue
            int MyConnectionsCount = Sharables.ConnectionsCount[Id];

            if (MyConnectionsCount == 0) {
                System.out.println("This node doesnot have connection:" + Id);
                return;
            }

            int delay = 0;
            if (k == 0) {
                delay = MyConnectionsCount * (Sharables.HeaderTdelay + 1) + Sharables.LatencyDelay;
            } else {
                delay = MyConnectionsCount * Sharables.ChunkTdelay;
            }

            //System.out.println("Delay = "+delay);
            for (int i = 0; i < MyConnectionsCount; i++) {
                int NodeNumber = Sharables.Connections[Id][i];
                Ntemp n = new Ntemp(NodeNumber, delay, Id);;
//                if (k == 0) {
//                    n = new Ntemp(NodeNumber, (i + 1) * delay);
//                } else {
//                    n = new Ntemp(NodeNumber, delay);
//                }

                queue.add(n);
                InQueue.add(NodeNumber);
            }

            //now start the BFS
            while (queue.size() != 0) {

                Ntemp Vertex = queue.remove();
                int NodeNumber = Vertex.NodeNumber;
                int HopCount = Vertex.TimeTaken;
                //System.out.println(HopCount);

                //if it is not visited call that Node Newblock function
                Sharables.Nodes[NodeNumber].NewData(BlockHash, HopCount, Vertex.Parent);

                //add it connections to the queue
                for (int i = 0; i < Sharables.ConnectionsCount[NodeNumber]; i++) {
                    int Itemp = Sharables.Connections[NodeNumber][i];
                    if (!InQueue.contains(Itemp)) {
                        Ntemp n = null;
                        if (k == 0) {
                            n = new Ntemp(Itemp, HopCount + delay, NodeNumber);
                        } else {
                            n = new Ntemp(Itemp, delay, NodeNumber);
                        }
                        queue.add(n);
                        InQueue.add(Itemp);
                    }
                }

            }
        }

        Sharables.MaxTimeTaken += Sharables.NoOfChunks * Sharables.ChunkTdelay * Sharables.Cm;
    }
}
