import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

class DNSLookup {

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== DNS Lookup Program =====");
        System.out.print("Enter an IP address or hostname: ");
        String input = scanner.nextLine().trim();

        // Check if input is IP or hostname
        if (isIPAddress(input)) {
            // Input is IP → find hostname
            try {
                InetAddress inet = InetAddress.getByName(input);
                System.out.println("------------------------------------");
                System.out.println("IP Address   : " + input);
                System.out.println("Host Name    : " + inet.getCanonicalHostName());
                System.out.println("------------------------------------");
            } catch (UnknownHostException e) {
                System.out.println("Error: Could not resolve hostname for IP: " + input);
            }
        } else {
            // Input is hostname → find IP
            try {
                InetAddress inet = InetAddress.getByName(input);
                System.out.println("------------------------------------");
                System.out.println("Host Name    : " + input);
                System.out.println("IP Address   : " + inet.getHostAddress());
                System.out.println("------------------------------------");
            } catch (UnknownHostException e) {
                System.out.println("Error: Could not resolve IP for hostname: " + input);
            }
        }

        scanner.close();
    }

    // Utility: Check if input string is an IP address
    private static boolean isIPAddress(String input) {
        return input.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
    }
}
