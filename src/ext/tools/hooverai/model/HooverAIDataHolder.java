package ext.tools.hooverai.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple data structure for keeping the input sorted and easily processed.
 */
public class HooverAIDataHolder {

    DataPoint maxSize; // i.e 5 5
    DataPoint startingPoint; // i.e. 1 2
    ArrayList<DataPoint> dirtPatchList;  //  i.e. 1 0 and 2 2
    ArrayList<DataPoint> cleanedZones;  //  i.e. 1 0 and 2 2
    String drivingDirections;  //  i.e. NNESEESENWW
    int patchesCleaned;  // answer
    DataPoint restingPoint;  // answer
    HashSet hashSetMatches; // dirt patches which were cleaned 

    public HashSet getHashSetMatches() {
        return hashSetMatches;
    }

    public void setHashSetMatches(HashSet hashSetMatches) {
        this.hashSetMatches = hashSetMatches;
    }

    public HooverAIDataHolder() {
    }  // default constructor

    public DataPoint getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(DataPoint maxSize) {
        this.maxSize = maxSize;
    }

    public DataPoint getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(DataPoint startingPoint) {
        this.startingPoint = startingPoint;
    }

    public ArrayList<DataPoint> getDirtPatchList() {
        return dirtPatchList;
    }

    public void setDirtPatchList(ArrayList<DataPoint> dirtPatchList) {
        this.dirtPatchList = dirtPatchList;
    }

    public String getDrivingDirections() {
        return drivingDirections;
    }

    public void setDrivingDirections(String drivingDirections) {
        this.drivingDirections = drivingDirections;
    }

    public int getPatchesCleaned() {
        return patchesCleaned;
    }

    public void setPatchesCleaned(int patchesCleaned) {
        this.patchesCleaned = patchesCleaned;
    }

    public DataPoint getRestingPoint() {
        return restingPoint;
    }

    public void setRestingPoint(DataPoint restingPoint) {
        this.restingPoint = restingPoint;
    }

    public ArrayList<DataPoint> getCleanedZones() {
        return cleanedZones;
    }

    public void setCleanedZones(ArrayList<DataPoint> cleanedZones) {
        this.cleanedZones = cleanedZones;
    }

}
