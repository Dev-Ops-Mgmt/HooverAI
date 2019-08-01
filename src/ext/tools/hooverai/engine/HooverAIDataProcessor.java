package ext.tools.hooverai.engine;

import ext.tools.hooverai.model.DataPoint;
import ext.tools.hooverai.model.HooverAIDataHolder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * HooverAIDataProcessor processes data stored inside a HooverAIDataHolder and
 * performs the logic needed to follow directions and identify when a dirt patch
 * is cleaned.
 */
public class HooverAIDataProcessor {

    public static HooverAIDataHolder processHooverAIDataHolder(HooverAIDataHolder hooverAIDataHolder) {

        // given we know the dirt patches let's just process the instructions and see if we hit any patches 
        // therefore cleaning them.
        // get starting point 
        DataPoint startingPoint = hooverAIDataHolder.getStartingPoint();
        // get grid bounds
        int maxX = hooverAIDataHolder.getMaxSize().getX();
        int maxY = hooverAIDataHolder.getMaxSize().getY();
        // prepare to parse driving insturctions
        String instructions = hooverAIDataHolder.getDrivingDirections();
        char[] discreteDirectionsArray = instructions.toCharArray();

        //intially just start with starting point
        DataPoint nextPoint = new DataPoint(startingPoint.getX(), startingPoint.getY());
        ArrayList<DataPoint> placesHoovered = new ArrayList();
        for (int i = 0; i < discreteDirectionsArray.length; i++) {

            int tempX = 0;
            int tempY = 0;

            char direction = discreteDirectionsArray[i];
            String directionString = Character.toString(direction);
            if (directionString.equalsIgnoreCase("N")) // go north
            {

                tempY = nextPoint.getY();
                tempY = tempY + 1;  // do move
                if (tempY > maxY) // we hit a wall, remove increment
                {
                    tempY = tempY - 1;
                }

                nextPoint.setY(tempY);

                DataPoint entry = new DataPoint(nextPoint.getX(), nextPoint.getY());

                placesHoovered.add(entry);

            } else if (directionString.equalsIgnoreCase("S")) // go south
            {
                tempY = nextPoint.getY();
                tempY = tempY - 1;  // do move
                if (tempY < 0) // we hit a wall add value back in
                {
                    tempY = tempY + 1;
                }
                nextPoint.setY(tempY);
                DataPoint entry = new DataPoint(nextPoint.getX(), nextPoint.getY());
                placesHoovered.add(entry);

            } else if (directionString.equalsIgnoreCase("W")) // go west
            {
                tempX = nextPoint.getX();
                tempX = tempX - 1;  // do move
                if (tempX < 0) // we hit a wall
                {
                    tempX = tempX + 1;
                }
                nextPoint.setX(tempX);
                DataPoint entry = new DataPoint(nextPoint.getX(), nextPoint.getY());
                placesHoovered.add(entry);

            } else if (directionString.equalsIgnoreCase("E")) // go east
            {
                tempX = nextPoint.getX();
                tempX = tempX + 1;  // do move
                if (tempX > maxX) // wall
                {
                    tempX = tempX - 1;
                }
                nextPoint.setX(tempX);
                DataPoint entry = new DataPoint(nextPoint.getX(), nextPoint.getY());
                placesHoovered.add(entry);

            }
            // read each char in string 
            // logic is N = y + 1 
            //          S = y - 1
            //          E = x + 1
            //          W = x - 1

        }

        hooverAIDataHolder.setRestingPoint(nextPoint);
        hooverAIDataHolder.setCleanedZones(placesHoovered);

        return hooverAIDataHolder;
    }

}
