import java.util.*;

// Class to represent a router
class Router {
    String name;
    Map<String, Integer> neighbors = new HashMap<>();

    Router(String name) {
        this.name = name;
    }

    void addNeighbor(String neighbor, int cost) {
        neighbors.put(neighbor, cost);
    }

    @Override
    public String toString() {
        return name;
    }
}

// Main Class
class LinkStateRouting {
    Map<String, Router> network = new HashMap<>();

    // Add a link (undirected)
    void addLink(String r1, String r2, int cost) {
        network.putIfAbsent(r1, new Router(r1));
        network.putIfAbsent(r2, new Router(r2));
        network.get(r1).addNeighbor(r2, cost);
        network.get(r2).addNeighbor(r1, cost);
    }

    // Dijkstra’s Algorithm
    void dijkstra(String src) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String router : network.keySet()) {
            dist.put(router, Integer.MAX_VALUE);
        }
        dist.put(src, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(src);

        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (visited.contains(u)) continue;
            visited.add(u);

            for (Map.Entry<String, Integer> entry : network.get(u).neighbors.entrySet()) {
                String v = entry.getKey();
                int cost = entry.getValue();
                int alt = dist.get(u) + cost;

                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.add(v);
                }
            }
        }

        // Print Routing Table
        System.out.println("\nRouting Table for Router " + src + ":");
        System.out.println("+-----------------+----------+----------+");
        System.out.printf("| %-15s | %-8s | %-8s |%n", "Destination", "Cost", "Next Hop");
        System.out.println("+-----------------+----------+----------+");
        for (String dest : network.keySet()) {
            if (dest.equals(src)) continue;
            if (dist.get(dest) == Integer.MAX_VALUE) {
                System.out.printf("| %-15s | %-8s | %-8s |%n", dest, "Unreachable", "-");
            } else {
                System.out.printf("| %-15s | %-8d | %-8s |%n", dest, dist.get(dest), getNextHop(src, dest, prev));
            }
        }
        System.out.println("+-----------------+----------+----------+");
    }

    // Trace back to get the next hop
    private String getNextHop(String src, String dest, Map<String, String> prev) {
        String hop = dest;
        while (prev.containsKey(hop) && !prev.get(hop).equals(src)) {
            hop = prev.get(hop);
        }
        return hop;
    }

    // Driver
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LinkStateRouting sim = new LinkStateRouting();

        System.out.println("=== Link-State Routing Simulator ===");
        System.out.print("Enter number of links in the network: ");
        int n = sc.nextInt();

        System.out.println("Enter links in format: Router1 Router2 Cost");
        for (int i = 1; i <= n; i++) {
            System.out.print("Link " + i + ": ");
            String r1 = sc.next();
            String r2 = sc.next();
            int cost = sc.nextInt();
            sim.addLink(r1, r2, cost);
        }

        System.out.print("Enter source router to compute routing table: ");
        String src = sc.next();

        sim.dijkstra(src);
        sc.close();
    }
}
