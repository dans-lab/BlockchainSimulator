/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danslab.regular.simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author AyinalaKaushik(UMKC-
 */
public class BlockchainSimulator {

    public static void main(String args[]) throws InterruptedException, IOException {

        int Cm = 50;
        int Tm = 60000;

        Sharables.TimeToMine = Tm;
        System.out.println("Avg Bandwidth in Mbps:" + Sharables.Bandwidth / (1024 * 1024));
        System.out.println("Avg Latency :" + Sharables.LatencyDelay);
        System.out.println("Max connections:" + Cm);
        System.out.print("No_of_nodes\t");
        System.out.print("Blocksize(KB)\t");
        System.out.print("Propagation time(millis)\n");
        System.out.println("--------------------------------------------------------------------------");

        int NoOfNodes = 1024;
        long Blocksize = 16 * 8 * 1024; //16 KB

        for (int k = 0; k < 11; k++) { //iterate no of nodes

            Sharables.NoOfNodes = NoOfNodes;

            //System.out.println("intitalizing the topology");
            initRandomGraphTopology(Cm);

            for (int l = 0; l < 13; l++) { //iterate blocksize

                Sharables.MaxHopCount = 0;

                long startTime = System.currentTimeMillis();

                //intialize program start time
                Sharables.startTime = System.currentTimeMillis();
                //initialize the values
                Sharables.Bandwidth = 100 * 1024 * 1024;
                Sharables.BlockSize = Blocksize;
                Sharables.Tdelay = (int) (((double) (Blocksize * 1000) / Sharables.Bandwidth)); //in millis

                ///
                Block Genesis = new Block(-1, 0, System.currentTimeMillis(), Sharables.TimeToMine, -1);
                Node[] Nodes = new Node[Sharables.NoOfNodes];

                //initalize the scheduler
                Scheduler.init();

                //declare and register nodes
                for (int i = 0; i < Nodes.length; i++) {
                    Nodes[i] = new Node(Genesis, i);
                }

                //init events for only first node
                Nodes[0].init();

                //initialize the threads to sharables
                Sharables.Nodes = Nodes;

                //start the scheduler
                //System.out.println("starting the scheduler");
                Scheduler.start();

                //print output
                System.out.print(NoOfNodes + "\t");
                System.out.print(Blocksize / (1 * 8 * 1024) + "\t");
                System.out.print(Sharables.MaxHopCount + "\t\n");

                Blocksize *= 2;

            }

            NoOfNodes *= 2;
            Blocksize = 16 * 8 * 1024;
        }
    }

    private static void initRandomGraphTopology(int Cm) {

        long startTime = System.currentTimeMillis();
        int NoOfNodes = Sharables.NoOfNodes;
        int ConnectionsCount[] = new int[NoOfNodes];
        int Connections[][] = new int[NoOfNodes][Cm];

        ArrayList<Integer> ToDo = new ArrayList<Integer>();
        for (int i = 0; i < NoOfNodes; i++) {
            ToDo.add(i);
        }

        Collections.shuffle(ToDo);
        //System.out.println(ToDo.toString());
        while (ToDo.size() > 1) {
            //System.out.println("\nArraylist size is " + ToDo.size() + "\n");
            int NodeX;

            // remove the first elemet from the list and connect it other
            NodeX = ToDo.remove(0);
            //System.out.println("Removed node " + NodeX);

            //for no of connection for Nodex equal to Cm do the following
            int count = 0;
            while (ConnectionsCount[NodeX] < Cm) {

                //get a random value from the list and connect to it
                int randValue;

                if (ToDo.size() > 1) {
                    randValue = Functions.getRandomNumberInRange(0, ToDo.size() - 1);
                } else if (ToDo.size() == 1) {
                    randValue = 0;
                } else {
                    break;
                }
                int NodeY = ToDo.get(randValue);
                //System.out.println("randValue=" + randValue + " NodeY=" + NodeY);

                //check whether the new random node is already connected
                boolean AlreadyExist = false;
                for (int j = 0; j < ConnectionsCount[NodeX]; j++) {
                    if (Connections[NodeX][j] == NodeY) {
                        AlreadyExist = true;
                        count++;
                        //System.out.println("Count value="+count);
                        break;
                    }
                }

                //if new random node doesnot exist in the connections of X then add the connection
                if (!AlreadyExist) {
                    Connections[NodeX][ConnectionsCount[NodeX]++] = NodeY;
                    Connections[NodeY][ConnectionsCount[NodeY]++] = NodeX;

                    if (!(ConnectionsCount[NodeY] < Cm)) {
                        ToDo.remove(randValue);
                        //System.out.println("Removed node " + NodeY + " limit reached");
                    }

                }

                if (count > (10 * Cm)) {
                    break;
                }
            }
        }

        Sharables.ConnectionsCount = ConnectionsCount;
        Sharables.Connections = Connections;

        //printing for debugging
//        System.out.println("Out of the topology and count of while loop is ");
//        System.out.println("time taken is " + (System.currentTimeMillis() - startTime));
//
        //System.out.println(Arrays.toString(ConnectionsCount));
        for (int i = 0; i < NoOfNodes; i++) {
            // System.out.println(Arrays.toString(Connections[i]));
        }

    }

}
