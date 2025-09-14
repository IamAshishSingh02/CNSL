import java.util.Scanner;

public class SubnettingDemo {

    public static int[] ipToIntArray(String ip) {
        String[] parts = ip.split("\\.");
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static String intArrayToIp(int[] ip) {
        return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
    }

    public static String ipToBinary(String ip) {
        int[] parts = ipToIntArray(ip);
        StringBuilder binary = new StringBuilder();
        for (int part : parts) {
            String binPart = Integer.toBinaryString(part);
            while (binPart.length() < 8) binPart = "0" + binPart;
            binary.append(binPart).append(".");
        }
        return binary.substring(0, binary.length() - 1);
    }

    public static String maskToBinary(int[] mask) {
        StringBuilder binary = new StringBuilder();
        for (int part : mask) {
            String binPart = Integer.toBinaryString(part);
            while (binPart.length() < 8) binPart = "0" + binPart;
            binary.append(binPart).append(".");
        }
        return binary.substring(0, binary.length() - 1);
    }

    public static int[] cidrToSubnetMask(int cidr) {
        int[] mask = new int[4];
        for (int i = 0; i < 4; i++) {
            if (cidr >= 8) {
                mask[i] = 255;
                cidr -= 8;
            } else {
                mask[i] = (int)(256 - Math.pow(2, 8 - cidr));
                cidr = 0;
            }
        }
        return mask;
    }

    public static int bitsNeeded(int subnets) {
        int bits = 0;
        while ((1 << bits) < subnets) bits++;
        return bits;
    }

    public static String[] calculateSubnetRange(int[] ip, int newMaskBits, int subnetNumber) {
        int hostBits = 32 - newMaskBits;
        int totalHosts = (int) Math.pow(2, hostBits);
        int subnetSize = totalHosts;

        int ipInt = ((ip[0] << 24) & 0xFF000000) |
                    ((ip[1] << 16) & 0x00FF0000) |
                    ((ip[2] << 8) & 0x0000FF00) |
                    (ip[3] & 0x000000FF);

        int subnetBase = (ipInt & (~(totalHosts - 1))) + subnetNumber * subnetSize;

        int[] startIp = new int[4];
        startIp[0] = (subnetBase >> 24) & 0xFF;
        startIp[1] = (subnetBase >> 16) & 0xFF;
        startIp[2] = (subnetBase >> 8) & 0xFF;
        startIp[3] = subnetBase & 0xFF;

        int[] endIp = new int[4];
        int broadcast = subnetBase + subnetSize - 1;
        endIp[0] = (broadcast >> 24) & 0xFF;
        endIp[1] = (broadcast >> 16) & 0xFF;
        endIp[2] = (broadcast >> 8) & 0xFF;
        endIp[3] = broadcast & 0xFF;

        return new String[]{
            intArrayToIp(startIp), ipToBinary(intArrayToIp(startIp)),
            intArrayToIp(endIp), ipToBinary(intArrayToIp(endIp))
        };
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter IP address: ");
        String ip = sc.nextLine();

        System.out.println("Binary form of IP: " + ipToBinary(ip));

        System.out.print("Enter network mask in CIDR (e.g. 24): ");
        int cidr = sc.nextInt();

        int[] originalMask = cidrToSubnetMask(cidr);
        System.out.println("Network mask (decimal): " + intArrayToIp(originalMask));
        System.out.println("Network mask (binary) : " + maskToBinary(originalMask));

        System.out.print("Enter number of subnets required: ");
        int subnets = sc.nextInt();

        int bitsForSubnet = bitsNeeded(subnets);
        int newCidr = cidr + bitsForSubnet;

        int[] newSubnetMask = cidrToSubnetMask(newCidr);

        System.out.println("\nSubnet mask after subnetting:");
        System.out.println("Decimal: " + intArrayToIp(newSubnetMask));
        System.out.println("Binary : " + maskToBinary(newSubnetMask));
        System.out.println("CIDR   : /" + newCidr);

        System.out.println("\nIP address ranges for each subnet:");

        for (int i = 0; i < subnets; i++) {
            String[] range = calculateSubnetRange(ipToIntArray(ip), newCidr, i);
            System.out.println("Group " + (i + 1) + " IP address range:");
            System.out.println("  Decimal: " + range[0] + " to " + range[2]);
            System.out.println("  Binary : " + range[1] + " to " + range[3]);
        }

        sc.close();
    }
}
