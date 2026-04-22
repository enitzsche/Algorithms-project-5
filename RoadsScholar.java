package project5;

import java.util.Scanner;
public class RoadsScholar {
    
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // get values for n, m, and k
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int k = scanner.nextInt();
        Road[] roadMap = new Road[m];
        City[] cityMap = new City[k];

        for (int i = 0; i < m; i++) {
            Road road = new Road(scanner.nextInt(), scanner.nextInt(), scanner.nextFloat());
            roadMap[i] = road;
        }
        for(int i = 0; i < k; i++) {
          City city = new City(scanner.nextInt(), scanner.nextLine());
          cityMap[i] = city;
        }

        int numSigns = scanner.nextInt();

        for(int i = 0; i < numSigns; i++) {
          int signEnd1 = scanner.nextInt();
          int signEnd2 = scanner.nextInt();
          for(int j = 0; j < roadMap.length; j++) {
               if(roadMap[j].end1 == signEnd1 && roadMap[j].end2 == signEnd2) {
                    roadMap[j].signToPlace = true;
                    roadMap[j].signDistance = scanner.nextFloat();
               }
          }
        }
     }
}
