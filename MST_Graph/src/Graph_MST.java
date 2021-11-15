
import com.sun.istack.internal.Nullable;
import java.util.*;
import java.io.*;
import javafx.util.Pair;

/**
 *
 * @author Rawan
 */
public class Graph_MST {
//numVertices => number of Vertices
//numEdges => number of Edges

    static int numVertices, numEdges;
    static int choice; //assign to selected case

    /**
     *
     * @param args
     */
//1.Main calss 
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("\t\t\t---->CPCS324 Project - Phase1<----");

        System.out.println("\t> Run Time comparison of the the minimum spanning-tree algorithms <\n\n(1) Kruskal's Algorithm VS. Prim's Algorithm (based on Priority Queue)"
                + "\n(2) Prim's Algorithm (based on Min Heap) VS. Prim's Algorithm(based on Priority Queue)");

        System.out.print("\nWhich two algorithms would you like to compare?\n--> Enter your choice (1 or 2): ");

        int Task = in.nextInt();

//if the user choice wrong number of the task.
        if (Task != 1 && Task != 2) {
            System.out.println("Invalid input!");
            System.exit(0);
        }
// Display the cases.
        System.out.println("\nSelect one of the following cases to generate a graph:\n");
        System.out.println("\t         Vertices \tEdges\n"
                + "\t        -----------------------\n"
                + "\tCase 1:\t   1000\t\t10000 \n"
                + "\tCase 2:\t   1000\t\t15000 \n"
                + "\tCase 3:\t   1000\t\t25000 \n"
                + "\tCase 4:\t   5000\t\t15000 \n"
                + "\tCase 5:\t   5000\t\t25000 \n"
                + "\tCase 6:\t   10000\t15000 \n"
                + "\tCase 7:\t   10000\t25000 \n"
                + "\tCase 8:\t   20000\t200000 \n"
                + "\tCase 9:\t   20000\t300000 \n"
                + "\tCase 10:   50000\t1000000 \n");

        System.out.print("Enter Your case Number: ");

        choice = in.nextInt();
//Send the selected case to Cases method to define number of vertices and edges
        Cases(choice);
        //
        Graph MST_graph = new Graph(numVertices, numEdges);
        MST_graph.Make_Graph(MST_graph);

        switch (Task) {
            // to perform Task 2
            case 1:
                System.out.println("----------- Prim Based on Priority Queue ------------\n");
                MST_graph.PrimPQ();
                System.out.println("\n----------- Kruskal Algorithm ------------\n");
                MST_graph.KruskalMST();
                break;
            // to perform Task 3
            case 2:
                System.out.println("----------- Prim Based on Priority Queue ------------\n");
                MST_graph.PrimPQ();
                System.out.println("----------- Prim’s algorithm using min-heap ------------\n");
                MST_graph.PrimMH();
                break;
        }
    }
//-------------------------------------------------------------------

    /**
     *
     * @param CaseNum
     */
//2.cases
    public static void Cases(int CaseNum) {

        switch (CaseNum) {
            case 1:
                numVertices = 1000;
                numEdges = 10000;
                break;
            case 2:
                numVertices = 1000;
                numEdges = 15000;
                break;
            case 3:
                numVertices = 1000;
                numEdges = 25000;
                break;
            case 4:
                numVertices = 5000;
                numEdges = 15000;
                break;
            case 5:
                numVertices = 5000;
                numEdges = 25000;
                break;
            case 6:
                numVertices = 10000;
                numEdges = 15000;
                break;
            case 7:
                numVertices = 10000;
                numEdges = 25000;
                break;
            case 8:
                numVertices = 20000;
                numEdges = 200000;
                break;
            case 9:
                numVertices = 20000;
                numEdges = 300000;
                break;
            case 10:
                numVertices = 50000;
                numEdges = 1000000;
                break;
            default:
                System.out.println("Wrong choice of case!");
                System.exit(0);
        }
    }
//-------------------------------------------------------------------

//3.Edge class
    /**
     *
     */
    public static class Edge {

        int source;
        int destination;
        int weight;
// constructor 

        /**
         *
         * @param source
         * @param destination
         * @param weight
         */
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        /**
         *
         * @return
         */
        public String toString() {
            return source + "-" + destination + ": " + weight;
        }
    }
//-------------------------------------------------------------------

//4.Heap Node class
    /**
     *
     */
    public static class HeapNode {

        int vertex;
        int key;
    }
//-------------------------------------------------------------------

//5.Heap Node class
    /**
     *
     */
    public static class ResultSet {

        int parent;
        int weight;
    }
//-------------------------------------------------------------------      

//6.Min Heap class
    /**
     *
     */
    public static class MinHeap {

        int capacity;
        int currentSize;
        HeapNode[] mH;
        int[] indexes; //will be used to decrease the key

        /**
         *
         * @param capacity
         */
        public MinHeap(int capacity) {
            this.capacity = capacity;
            mH = new HeapNode[capacity + 1];
            indexes = new int[capacity];
            mH[0] = new HeapNode();
            mH[0].key = Integer.MIN_VALUE;
            mH[0].vertex = -1;
            currentSize = 0;
        }

        /**
         *
         */
        public void display() {
            for (int i = 0; i <= currentSize; i++) {
                System.out.println(" " + mH[i].vertex + "   key   " + mH[i].key);
            }
            System.out.println("________________________");
        }

        /**
         *
         * @param x
         */
        public void insert(HeapNode x) {
            currentSize++;
            int idx = currentSize;
            mH[idx] = x;
            indexes[x.vertex] = idx;
            bubbleUp(idx);
        }

        /**
         *
         * @param pos
         */
        public void bubbleUp(int pos) {
            int parentIdx = pos / 2;
            int currentIdx = pos;
            while (currentIdx > 0 && mH[parentIdx].key > mH[currentIdx].key) {
                HeapNode currentNode = mH[currentIdx];
                HeapNode parentNode = mH[parentIdx];

                //swap the positions
                indexes[currentNode.vertex] = parentIdx;
                indexes[parentNode.vertex] = currentIdx;
                swap(currentIdx, parentIdx);
                currentIdx = parentIdx;
                parentIdx = parentIdx / 2;
            }
        }

        /**
         *
         * @return
         */
        public HeapNode extractMin() {
            HeapNode min = mH[1];
            HeapNode lastNode = mH[currentSize];
//            update the indexes[] and move the last node to the top
            indexes[lastNode.vertex] = 1;
            mH[1] = lastNode;
            mH[currentSize] = null;
            sinkDown(1);
            currentSize--;
            return min;
        }

        /**
         *
         * @param k
         */
        public void sinkDown(int k) {
            int smallest = k;
            int leftChildIdx = 2 * k;
            int rightChildIdx = 2 * k + 1;
            if (leftChildIdx < heapSize() && mH[smallest].key > mH[leftChildIdx].key) {
                smallest = leftChildIdx;
            }
            if (rightChildIdx < heapSize() && mH[smallest].key > mH[rightChildIdx].key) {
                smallest = rightChildIdx;
            }
            if (smallest != k) {

                HeapNode smallestNode = mH[smallest];
                HeapNode kNode = mH[k];

                //swap the positions
                indexes[smallestNode.vertex] = k;
                indexes[kNode.vertex] = smallest;
                swap(k, smallest);
                sinkDown(smallest);
            }
        }

        /**
         *
         * @param a
         * @param b
         */
        public void swap(int a, int b) {
            HeapNode temp = mH[a];
            mH[a] = mH[b];
            mH[b] = temp;
        }

        /**
         *
         * @return
         */
        public boolean isEmpty() {
            return currentSize == 0;
        }

        /**
         *
         * @return
         */
        public int heapSize() {
            return currentSize;
        }
    }
//-------------------------------------------------------------------

//7. Graph class
// The graph class has all of the algorithms.
    /**
     *
     */
    public static class Graph {

        int numVertices;
        int numEdges;
        //declaration startTime, endTime, estimated Time, to Calculate time execution of algorithm
        double totalTime, endTime, startTime;
        LinkedList<Edge>[] adjacencylist;

        /**
         *
         * @param vertices
         * @param edges
         */
        // constructor 
        Graph(int vertices, int edges) {
            this.numVertices = vertices;
            this.numEdges = edges;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        /**
         *
         * @param source
         * @param destination
         * @param weight
         */
        // this method is used in make_graph() method to add a new edge
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge);

            edge = new Edge(destination, source, weight);
            adjacencylist[destination].addFirst(edge); //for undirected graph

        }

        // Prim's Algorithm using Priprity Queue
        /**
         *
         */
        public void PrimPQ() {
            //start time
            startTime = System.currentTimeMillis();
            boolean[] mst = new boolean[numVertices];
            ResultSet[] resultSet = new ResultSet[numVertices];
            int[] key = new int[numVertices];  //keys used to store the key to know whether priority queue update is required

            //Initialize all the keys to infinity and
            //initialize resultSet for all the vertices
            for (int i = 0; i < numVertices; i++) {
                key[i] = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
            }

            //Initialize priority queue
            //override the comparator to do the sorting based keys
            PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(numVertices, new Comparator<Pair<Integer, Integer>>() {
                /**
                 *
                 * @param p1
                 * @param p2
                 * @return
                 */
                @Override
                public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                    //sort using key values
                    int key1 = p1.getKey();
                    int key2 = p2.getKey();
                    return key1 - key2;
                }
            });

            //create the pair for for the first index, 0 key 0 index
            key[0] = 0;
            Pair<Integer, Integer> p0 = new Pair<>(key[0], 0);
            //add it to pq
            pq.offer(p0);
            resultSet[0] = new ResultSet();
            resultSet[0].parent = -1;

            //while priority queue is not empty
            while (!pq.isEmpty()) {
                //extract the min
                Pair<Integer, Integer> extractedPair = pq.poll();

                //extracted vertex
                int extractedVertex = extractedPair.getValue();
                mst[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is not present in mst
                    if (mst[edge.destination] == false) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            //add it to the priority queue
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            //update the resultSet for destination vertex
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            //update the key[]
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //finish time of the algorithm
            endTime = System.currentTimeMillis();
            // calculate run time of Prim’s algorithm using PQ
            totalTime = endTime - startTime;
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Algorithm in Second: " + (totalTime / 1000.0) + " s.");
            //print mst cost.
            printMST(resultSet);
        }

        // Prim's Algorithm using Minheap
        /**
         *
         */
        public void PrimMH() {
            //start time
            startTime = System.currentTimeMillis();
            boolean[] inHeap = new boolean[numVertices];
            ResultSet[] resultSet = new ResultSet[numVertices];
            //keys[] used to store the key to know whether min hea update is required
            int[] key = new int[numVertices];
            //create heapNode for all the vertices
            HeapNode[] heapNodes = new HeapNode[numVertices];
            for (int i = 0; i < numVertices; i++) {
                heapNodes[i] = new HeapNode();
                heapNodes[i].vertex = i;
                heapNodes[i].key = Integer.MAX_VALUE;
                resultSet[i] = new ResultSet();
                resultSet[i].parent = -1;
                inHeap[i] = true;
                key[i] = Integer.MAX_VALUE;
            }

            //decrease the key for the first index
            heapNodes[0].key = 0;

            //add all the vertices to the MinHeap
            MinHeap minHeap = new MinHeap(numVertices);
            //add all the vertices to priority queue
            for (int i = 0; i < numVertices; i++) {
                minHeap.insert(heapNodes[i]);
            }

            //while minHeap is not empty
            while (!minHeap.isEmpty()) {
                //extract the min
                HeapNode extractedNode = minHeap.extractMin();

                //extracted vertex
                int extractedVertex = extractedNode.vertex;
                inHeap[extractedVertex] = false;

                //iterate through all the adjacent vertices
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    //only if edge destination is present in heap
                    if (inHeap[edge.destination]) {
                        int destination = edge.destination;
                        int newKey = edge.weight;
                        //check if updated key < existing key, if yes, update if
                        if (key[destination] > newKey) {
                            decreaseKey(minHeap, newKey, destination);
                            //update the parent node for destination
                            resultSet[destination].parent = extractedVertex;
                            resultSet[destination].weight = newKey;
                            key[destination] = newKey;
                        }
                    }
                }
            }
            //finish time of the algorithm
            endTime = System.currentTimeMillis();
            // calculate run time of Prim’s algorithm using PQ
            totalTime = endTime - startTime;
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Algorithm in Second: " + (totalTime / 1000.0) + " s.");
            //print mst cost
            printMST(resultSet);
        }

        /**
         *
         * @param minHeap
         * @param newKey
         * @param vertex
         */
        public void decreaseKey(MinHeap minHeap, int newKey, int vertex) {

            //get the index which key's needs a decrease;
            int index = minHeap.indexes[vertex];

            //get the node and update its value
            HeapNode node = minHeap.mH[index];
            node.key = newKey;
            minHeap.bubbleUp(index);
        }

        /**
         *
         * @param resultSet
         */
        public void printMST(ResultSet[] resultSet) {
            int total_min_weight = 0;
            for (int i = 1; i < numVertices; i++) {
//                System.out.println("Edge: " + i + " - " + resultSet[i].parent
//                        + " weight: " + resultSet[i].weight);
                total_min_weight += resultSet[i].weight;
            }
            System.out.println("Total Minimum Spanning Tree Cost: " + total_min_weight);
        }

        // Kruskal's Algorithm
        /**
         *
         */
        public void KruskalMST() {
            // start time
            startTime = System.currentTimeMillis();
            String VerticesTree = ""; // this variable is used only in tracing
            LinkedList<Edge>[] allEdges = adjacencylist.clone(); // modified data type from ArrayList to LinkedList
            PriorityQueue<Edge> pq = new PriorityQueue<>(numEdges, Comparator.comparingInt(o -> o.weight));

            //add all the edges to priority queue, //sort the edges on weights
            for (int i = 0; i < allEdges.length; i++) {
                for (int j = 0; j < allEdges[i].size(); j++) {
                    pq.add(allEdges[i].get(j));
                }
            }
//                System.out.println("\nSorted list of Edges:");
//                for (Edge edge : pq) {
//                System.out.println(edge.toString());
//            }

            //create a parent []
            int[] parent = new int[numVertices];

            //makeset
            makeSet(parent);
            LinkedList<Edge> mst = new LinkedList<>();

            //process vertices - 1 => edges
            int index = 0;
            while (index < numVertices - 1 && !pq.isEmpty()) {
                Edge edge = pq.remove();
                //check if adding this edge creates a cycle
                int x_set = find(parent, edge.source);
                int y_set = find(parent, edge.destination);

                if (x_set == y_set) {
                    //ignore, will create cycle
                } else {
                    //add it to our final result
                    mst.add(edge);
                    treeVertices += edge.toString() + "\n";
//                    System.out.println("\nTree Vertex:");
//                    System.out.println(VerticesTree);
                    index++;
                    union(parent, x_set, y_set);
                }
//                System.out.println("Sorted list of Edges:");
//                for (Edge e : pq) {
//                    System.out.println(e.toString());
//                }
            }

            //finish time of the algorithm
            endTime = System.currentTimeMillis();
            // calculate run time of Prim’s algorithm using PQ
            totalTime = endTime - startTime;
            //print the total time consumed by the algorithm
            System.out.println("Total runtime of Algorithm in second: " + (totalTime / 1000.0) + " s.");
            //print MST
//            System.out.println("Minimum Spanning Tree: ");
            printGraph(mst);
        }

        /**
         *
         * @param parent
         */
        public void makeSet(int[] parent) {
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i < numVertices; i++) {
                parent[i] = i;
            }
        }

        /**
         *
         * @param parent
         * @param vertex
         * @return
         */
        public int find(int[] parent, int vertex) {
            //chain of parent pointers from x upwards through the tree
            // until an element is reached whose parent is itself
            if (parent[vertex] != vertex) {
                return find(parent, parent[vertex]);
            };
            return vertex;
        }

        /**
         *
         * @param parent
         * @param x
         * @param y
         */
        public void union(int[] parent, int x, int y) {
            int x_set_parent = find(parent, x);
            int y_set_parent = find(parent, y);
            //make x as parent of y
            parent[y_set_parent] = x_set_parent;
        }

        /**
         *
         * @param edgeList
         */
        public void printGraph(LinkedList<Edge> edgeList) {
            int Cost = 0;
            for (int i = 0; i < edgeList.size(); i++) {
                Edge edge = edgeList.get(i);
//                System.out.println("Edge-" + i + " source: " + edge.source
//                        + " destination: " + edge.destination
//                        + " weight: " + edge.weight);
                Cost += edge.weight;
            }
            System.out.println("Minimum Spanning Tree Cost = " + Cost);
        }

        /**
         *
         * @param graph
         */
        // Make_graph--> Generate Random graph for differant cases
        public void Make_Graph(Graph graph) {

            // instance of Random class
            Random random = new Random();
            // ensure that all vertices are connected
            for (int source = 0; source < numVertices - 1; source++) {
                int weight = random.nextInt(10) + 1;
                addEdge(source, source + 1, weight);
            }

            // generate random graph with the remaining edges 
            int remaning = numEdges - (numVertices - 1);
            for (int i = 0; i < remaning; i++) {
                int source = random.nextInt(graph.numVertices);
                int destination = random.nextInt(graph.numVertices);

                // to avoid self loops and duplicate edges
                if (destination == source || isLinked(source, destination, graph.adjacencylist)) {
                    i--;
                    continue;
                }
                // generate random weights in range 1 to 10
                int weight = random.nextInt(10) + 1;
                // add edge to the graph
                addEdge(source, destination, weight);
            }
//        printGraph(allEdges);
        }

        /**
         *
         * @param source
         * @param destination
         * @param allEdges
         * @return
         */
        // checks if the edge is already existed
        public boolean isLinked(int source, int destination, LinkedList<Edge>[] allEdges) {

            for (LinkedList<Edge> i : allEdges) {
                for (Edge edge : i) {
                    if ((edge.source == source && edge.destination == destination) || (edge.source == destination && edge.destination == source)) {
                        return true;
                    }
                }
            }
            return false;
        }

    }
//-------------------------------------------------------------------   
}
