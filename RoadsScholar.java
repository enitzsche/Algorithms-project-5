package algoproject5;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class RoadsScholar {

     static final float infinity = Float.MAX_VALUE;

     //build shortest distance matrix using floyd-warshall
     private static float[][] buildDistMatrix(Road[] roadMap, int n) {
          float[][] dist = new float[n][n];

          //initialize all distances to infinity, zero on diagonal
          for(int i = 0; i<dist.length; i++) Arrays.fill(dist[i], infinity);
          for(int i = 0; i < n; i++) dist[i][i] = 0;

          //seed direct road distances
          for(Road road : roadMap) {
               int u = road.end1, v = road.end2;
               if(road.distance < dist[u][v]) {
                    dist[u][v] = road.distance;
                    dist[v][u] = road.distance;
               }
          }

          //relax all pairs through each intermediate node
          for(int mid = 0; mid < n; mid++) {
               for(int u = 0; u < n; u++) {
                    if(dist[u][mid] == infinity) continue;
                    for(int v = 0; v < n; v++) {
                         if(dist[mid][v] == infinity) continue;
                         if(dist[u][mid] + dist[mid][v] < dist[u][v]) {
                              dist[u][v] = dist[u][mid] + dist[mid][v];
                         }
                    }
               }
          }

          return dist;
     }

     //a variable for float comparision
     static final float epsilon = 0.0001f;

     //build next-hop matrix so we can trace which road a shortest path uses
     private static int[][] buildNextMatrix(Road[] roadMap, float[][] dist, int n) {
          int[][] next = new int[n][n];
          for(int i = 0; i<next.length; i++) Arrays.fill(next[i], -1);

          //fill in next hops: for each pair u->v, find which neighbor of u is on the shortest path
          for(int u = 0; u < n; u++) {
               for(int v = 0; v < n; v++) {
                    if(u == v || dist[u][v] == infinity) continue;
                    for(Road road : roadMap) {
                         int neighbor = -1;
                         if(road.end1 == u) neighbor = road.end2;
                         else if(road.end2 == u) neighbor = road.end1;
                         if(neighbor != -1 && Math.abs(dist[u][neighbor] + dist[neighbor][v] - dist[u][v]) < epsilon) {
                              next[u][v] = neighbor;
                              break;
                         }
                    }
               }
          }

          return next;
     }

     //print cities and distances for a single sign
     private static void printSign(int from, int to, float signDist, Road[] roadMap, City[] cityMap, float[][] dist, int[][] next) {
          //find the length of the road this sign is on
          float roadLen = 0;
          for(Road road : roadMap) {
               if((road.end1 == from && road.end2 == to) ||
                  (road.end1 == to   && road.end2 == from)) {
                    roadLen = road.distance;
                    break;
               }
          }
          float remaining = roadLen - signDist;

          //collect cities whose shortest path from 'from' uses this road
          List<int[]> entries = new ArrayList<>();
          for(int ci = 0; ci < cityMap.length; ci++) {
               int cityInter = cityMap[ci].intersection;
               if(next[from][cityInter] == to) {
                    float totalDist = remaining + dist[to][cityInter];
                    int rounded = (int) Math.floor(totalDist + 0.5f);
                    entries.add(new int[]{rounded, ci});
               }
          }

          //sort by distance then alphabetically
          entries.sort((a, b) -> {
               if(a[0] != b[0]) return Integer.compare(a[0], b[0]);
               return cityMap[a[1]].name.compareTo(cityMap[b[1]].name);
          });

          for(int[] entry : entries) {
               System.out.printf("%-20s%d%n", cityMap[entry[1]].name, entry[0]);
          }
     }

     public static void main(String[] args) {
          Scanner scanner = new Scanner(System.in);

          int n = scanner.nextInt();
          int m = scanner.nextInt();
          int k = scanner.nextInt();

          Road[] roadMap = new Road[m];
          City[] cityMap = new City[k];

          for(int i = 0; i < m; i++) {
               Road newRoad = new Road(scanner.nextInt(), scanner.nextInt(), scanner.nextFloat());
               roadMap[i] = newRoad;
          }

          scanner.nextLine();
          for(int i = 0; i < k; i++) {
               String line = scanner.nextLine().trim();
               int spaceIdx = line.indexOf(' ');
               City newCity = new City(Integer.parseInt(line.substring(0, spaceIdx)), line.substring(spaceIdx + 1).trim());
               cityMap[i] = newCity;
          }

          int numSigns = scanner.nextInt();
          int[] signEnd1 = new int[numSigns];
          int[] signEnd2 = new int[numSigns];
          float[] signDist = new float[numSigns];
          for(int i = 0; i < numSigns; i++) {
               signEnd1[i] = scanner.nextInt();
               signEnd2[i] = scanner.nextInt();
               signDist[i] = scanner.nextFloat();
          }

          float[][] dist = buildDistMatrix(roadMap, n);
          int[][] next = buildNextMatrix(roadMap, dist, n);

          for(int s = 0; s < numSigns; s++) {
               if(s > 0) System.out.println();
               printSign(signEnd1[s], signEnd2[s], signDist[s], roadMap, cityMap, dist, next);
          }

          scanner.close();
     }
}
