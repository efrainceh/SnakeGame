package PathFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


public class PathFinder {
    
    LinkedList<Integer> queue;
    int numberOfNodes;
    int numberOfRows;
    int numberOfCols;
    boolean[][] graph;
    int[][] adjMatrix;
    boolean[] visited;
    int[] distance;
    int[] previous;

   public void loadMatrix(boolean[][] graph) {

        this.graph = graph;
        this.numberOfRows = graph.length;
        this.numberOfCols = graph[0].length;
        this.numberOfNodes = numberOfRows * numberOfCols;
        this.adjMatrix = new int[numberOfNodes][numberOfNodes];
        this.visited = new boolean[numberOfNodes];
        this.previous = new int[numberOfNodes];
        this.distance = new int[numberOfNodes];
        Arrays.fill(distance, Integer.MAX_VALUE);
        this.queue = new LinkedList<Integer>();
        createAdjMatrix();
    
    }

    public LinkedList<ArrayList<Integer>> findPath(int sourceRow, int sourceCol, int destRow, int destCol) {

        // CONVERT GRAPH LOCATIONS TO NODE
        int source = convertToNode(sourceRow, sourceCol);
        int destination = convertToNode(destRow, destCol);

        // EXIT IF INVALID POSITIONS
        if (outOfGraph(source) || outOfGraph(destination)) {

            return null;

        }

        // SET INITIAL VALUES FOR SOURCE NODE
        visited[source] = true;
        distance[source] = 0;
        previous[source] = -1;
        
        if (solve(source, destination)) {
            
            ArrayList<Integer> path = getPath(destination);

            return convertPathToCoordinates(path);

        }
        
        return null;

    }

    private void createAdjMatrix() {

        for (int node = 0; node < numberOfNodes; node++) {
            addNodeEdges(node);
        }

    }

    private void addNodeEdges(int node) {
        
        int[] nodes = new int[numberOfNodes];
        int row = getRow(node);
        int col = getCol(node);

        // NODE IS NOT ACCESSIBLE, RETURN ZEROS
        if (graph[row][col]) {
            adjMatrix[node] = nodes;
            return;
        }

        // CHECK ALL FOUR POSSIBLE LOCATIONS IN THE GRAPH NEXT TO NODE
        // (RIGHT, LEFT, UP, DOWN). WE CHECK WHETHER THE POSITION IS STILL
        // IN GRAPH AND WHETHER IT IS ACCESSIBLE
        int neighbour = 0;
        if (col + 1 < numberOfCols && !graph[row][col + 1]) {
            neighbour = convertToNode(row, col + 1);
            nodes[neighbour] = 1;
        }
        if (col - 1 >= 0 && !graph[row][col -1]) {
            neighbour = convertToNode(row, col - 1);
            nodes[neighbour] = 1;
        }
        if (row + 1 < numberOfRows && !graph[row + 1][col]) {
            neighbour = convertToNode(row + 1, col);
            nodes[neighbour] = 1;
        }
        if (row - 1 >= 0 && !graph[row - 1][col]) {
            neighbour = convertToNode(row - 1, col);
            nodes[neighbour] = 1;
        }

        adjMatrix[node] = nodes;

    }

    private int getRow(int node) {

        return (int)(node/ numberOfRows); 

    }

    private int getCol(int node) {

        return (node % numberOfCols);

    }

    private int convertToNode(int row, int col) {

        return (row * numberOfCols) + col;

    }

    private boolean outOfGraph(int node) {

        return node < 0 || node > numberOfNodes - 1;

    }

    private boolean solve(int source, int destination) {

        queue.add(source);

        while (!queue.isEmpty()) {

            // GET THE POSITIONS ADJACENT TO NODE
            int currentNode = queue.remove();
            int[] nextNodes = adjMatrix[currentNode];
            

            for (int node = 0; node < nextNodes.length; node++) {
                
                // IF NODE IS ADJACENT AND HASN'T BEEN VISITED
                // ADD IT TO THE QUEUE
                if ( nextNodes[node] == 1 && !visited[node]) {

                    visited[node] = true;
                    queue.add(node);

                    // THE NEW DISTANCE IS THE DISTANCE TO THE CURRENT NODE
                    // PLUS ONE, BECAUSE THE GRAPH IS NOT WEIGHTED
                    int dist = distance[currentNode] + 1;

                    // IF DISTANCE IS SMALLER THAN THE CURRENT DISTANCE TO NODE
                    // UPDATE DISTANCE AND PREVIOUS
                    if (distance[node] > dist) {
                        distance[node] = dist;
                        previous[node] = currentNode;
                    }
                    
                    // EARLY EXIT IF DESTINATION NODE IS REACHED
                    if (node == destination) {
                        
                        return true;
                    
                    }
                
                }
            
            }
        
        }
        
        // IF NO PATH IS FOUND, RETURN FALSE
        return false;

    }

    private ArrayList<Integer> getPath(int destination) {

        ArrayList<Integer> path = new ArrayList<Integer>();

        // FIRST ADD DESTINATION
        path.add(destination);

        // USE THE VALUES IN EACH NODE TO MOVE TO THE NEXT POSITION IN THE PATH
        int nextNode = previous[destination];
        while (nextNode != -1) {

            path.add(nextNode);
            nextNode = previous[nextNode];

        }

        Collections.reverse(path);

        return path;

    }

    private LinkedList<ArrayList<Integer>> convertPathToCoordinates(ArrayList<Integer> path) {

        LinkedList<ArrayList<Integer>> pathCoordinates = new LinkedList<ArrayList<Integer>>();

        for (int node : path) {

            ArrayList<Integer> coordinates = new ArrayList<>();
            coordinates.add(getRow(node));
            coordinates.add(getCol(node));
            pathCoordinates.add(coordinates);

        }

        return pathCoordinates;

    }
 
}