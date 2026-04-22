package algoproject5;

public class Road {
    int end1;
    int end2;
    float distance;
    boolean signToPlace;
    float signDistance;

    Road(int end1, int end2, float distance) {
        this.end1 = end1;
        this.end2 = end2;
        this.distance = distance;
        signToPlace = false;
    }
}
