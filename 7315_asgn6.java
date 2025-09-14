// 7315_asgn6.java
// Simulation of Link State Routing using Dijkstra's Algorithm
// Ashish Kumar - Roll No: 15

import java.util.*;

class _7315_asgn6 {   // removed "public"
    static final int INF = 9999;

    void dijkstra(int graph[][], int src, int V) {
        int dist[] = new int[V];
        boolean visited[] = new boolean[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, visited, V);
            visited[u] = true;
            for (int v = 0; v < V; v++) {
                if (!visited[v] && graph[u][v] != 0 && dist[u] != INF
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }
        printSolution(dist, V, src);
    }

    int minDistance(int dist[], boolean visited[], int V) {
        int min = INF, minIndex = -1;
        for (int v = 0; v < V; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    void printSolution(int dist[], int V, int src) {
        System.out.println("Shortest paths from Router " + src + ":");
        for (int i = 0; i < V; i++) {
            System.out.println("To Router " + i + " -> Cost = " + dist[i]);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        _7315_asgn6 obj = new _7315_asgn6();

        System.out.print("Enter number of routers: ");
        int V = sc.nextInt();

        int graph[][] = new int[V][V];
        System.out.println("Enter adjacency matrix (0 if no direct link): ");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = sc.nextInt();
            }
        }

        System.out.print("Enter source router: ");
        int src = sc.nextInt();

        obj.dijkstra(graph, src, V);
        sc.close();
    }
}
