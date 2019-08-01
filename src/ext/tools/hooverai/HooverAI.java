/*
 * Demonstration only.
 */
package ext.tools.hooverai;

import ext.tools.hooverai.engine.HooverAIDataProcessor;
import ext.tools.hooverai.model.DataPoint;
import ext.tools.hooverai.model.HooverAIDataHolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * HooverAI is based upon a self cleaning vacuum much like a Roomba. It takes an
 * input file as text and reads in a coordinate grid, starting point, dirt
 * patches on the grid and driving instructions. After execution it prints out
 * the areas it cleaned and its final destination as an x,y coordinate. HooverAI
 * also dumps the grid size and areas which were cleaned in order to potentially
 * learn areas which are often dirty and in the future possibly leverage that
 * data to become more efficient or perhaps suggest areas to spend more time on.
 *
 * For simplicity no external libraries were used. This can be run or modified
 * with Java 1.8 in any IDE.
 */
public class HooverAI {

    // Static variables
    static boolean debug = false;
    static boolean defaultInput = false;
    static boolean defaultProps = false;

    /**
     * @param args the command line arguments, no arguments are needed.
     */
    public static void main(String[] args) {
        // Step 1.  Read input.txt file for instuction
        BufferedReader reader = null;
        try {
            URL url = HooverAI.class.getResource("input.txt");
            File file = new File(url.getFile());
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException fileNotFoundException) {
            if (debug) {
                System.out.println(fileNotFoundException.getMessage());
            }
            System.out.println("Error retriving input.txt file, no worries, continuing with default input");
            defaultInput = true;
        }

        // Step 2.  Read properties file for runtime settings
        Properties properties = new Properties();
        try {
            properties.load(HooverAI.class.getResourceAsStream("HooverAI.properties"));
            String debugString = properties.getProperty("debug");
            debug = Boolean.parseBoolean(debugString);
        } catch (IOException ioException) {
            if (debug) {
                System.out.println(ioException.getMessage());
            }
            System.out.println("Error retriving properties file, no worries, continuing with default properties");
            defaultProps = true;
        } catch (NullPointerException nullPointerException) {
            if (debug) {
                System.out.println(nullPointerException.getMessage());
            }
            System.out.println("Error retriving properties file, no worries, continuing with default properties");
            defaultProps = true;
        }

        // Step 3.  Check if we are to use default input else, build HooverAIDataHolder from sample input.txt file
        HooverAIDataHolder hooverAIDataHolder = new HooverAIDataHolder();
        if (defaultInput) // we don't have input from input.txt
        {
            hooverAIDataHolder = buildHooverAIInputFileStatic(hooverAIDataHolder);  // this data was given 
        } else {  // we have input from text file
            hooverAIDataHolder = buildHooverAIInputFromFile(reader, hooverAIDataHolder); // this is input.txt
        }

        // Step 4. send the data to the process engine which does all of the logic and writes the output
        hooverAIDataHolder = HooverAIDataProcessor.processHooverAIDataHolder(hooverAIDataHolder);

        // Step 6. Write the answer
        System.out.println(hooverAIDataHolder.getRestingPoint().getX() + " " + hooverAIDataHolder.getRestingPoint().getY());
        //int totalPatchesCleaned = compareForPrint(hooverAIDataHolder.getCleanedZones(), hooverAIDataHolder.getDirtPatchList());
        hooverAIDataHolder = compareForPrint(hooverAIDataHolder);
        //hooverAIDataHolder.setPatchesCleaned(totalPatchesCleaned);
        System.out.println(hooverAIDataHolder.getPatchesCleaned());

        // Step 6. write the processed data for later learning enhancement
        try {
            Date date = new Date();
            Long timeInMilliSeconds = date.getTime();
            File file = new File("HooverAIDataHolder" + timeInMilliSeconds + ".xml");
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println("#Begin run output");
            writer.println("Starting Point = " + hooverAIDataHolder.getStartingPoint().getX() + "," + hooverAIDataHolder.getStartingPoint().getY());
            writer.println("Ending Point = " + hooverAIDataHolder.getRestingPoint().getX() + "," + hooverAIDataHolder.getRestingPoint().getY());
            HashSet cleanedDirtySpots = hooverAIDataHolder.getHashSetMatches();
            Iterator iter = cleanedDirtySpots.iterator();
            while (iter.hasNext()) {
                String s = (String) iter.next();
                String[] cleanedSpotXY = s.split(Pattern.quote("."));
                String xCoord = cleanedSpotXY[0];
                String yCoord = cleanedSpotXY[1];
                writer.println("Cleaned Point = " + xCoord + "," + yCoord);

            }
            writer.println("#End of run output");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Step 7. Exit Gracefully
        System.exit(0);

    } // end main

    private static HooverAIDataHolder buildHooverAIInputFromFile(BufferedReader bufferedReader, HooverAIDataHolder hooverAIDataHolder) {
        try {
            // cycle through reader lines, populate HooverAIDataHolder with values from text
            String gridBounds;
            gridBounds = bufferedReader.readLine();  // line one is grid max
            hooverAIDataHolder.setMaxSize(convertStringtoDataPoint(gridBounds));
            if (debug) {
                System.out.println("The gridBounds stream is " + gridBounds);
            }
            String startingPoint;
            startingPoint = bufferedReader.readLine();  // line two is startingPoint
            if (debug) {
                System.out.println("The startingPoint stream is " + startingPoint);
            }
            DataPoint dpp = convertStringtoDataPoint(startingPoint);
            if (debug) {
                dpp.showMe();
            }
            hooverAIDataHolder.setStartingPoint(convertStringtoDataPoint(startingPoint));
            ArrayList<DataPoint> listDataPoint = new ArrayList();
            String nextPoint;
            while ((nextPoint = bufferedReader.readLine()) != null) {  // lines 3 .. N are dirt patches
                if (!isAlpha(nextPoint)) {  // check to make sure we have not reach the driving instructions yet

                    if (nextPoint.length() > 1) {  // use this check in case there are blank newlines in the input file
                        if (debug) {
                            System.out.println("The dirtpatch stream is " + nextPoint);
                        }
                        DataPoint dp = convertStringtoDataPoint(nextPoint);
                        listDataPoint.add(dp);
                    }
                } else {
                    String drivingDir = nextPoint;
                    hooverAIDataHolder.setDrivingDirections(drivingDir);  // the instructions NNSWSWSNN
                    if (debug) {
                        System.out.println("The instructions stream is " + drivingDir);
                    }
                }
            }
            if (debug) {
                System.out.println("setDirtPatchList length is " + listDataPoint.size());
            }
            hooverAIDataHolder.setDirtPatchList(listDataPoint);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return hooverAIDataHolder;
    }

    private static HooverAIDataHolder buildHooverAIInputFileStatic(HooverAIDataHolder hooverAIDataHolder) {
        ArrayList<DataPoint> listDataPoint = new ArrayList();

        // build points
        DataPoint maxSize = new DataPoint(5, 5);
        DataPoint startingPoint = new DataPoint(1, 2);

        hooverAIDataHolder.setMaxSize(maxSize);
        hooverAIDataHolder.setStartingPoint(startingPoint);

        DataPoint dirtPatch1 = new DataPoint(1, 0);
        DataPoint dirtPatch2 = new DataPoint(2, 2);
        DataPoint dirtPatch3 = new DataPoint(2, 3);
        // add to array
        listDataPoint.add(dirtPatch1);
        listDataPoint.add(dirtPatch2);
        listDataPoint.add(dirtPatch3);

        // add to hooverAIDataHolder
        hooverAIDataHolder.setDirtPatchList(listDataPoint);

        // add directions
        String directions = "NNESEESWNWW";
        hooverAIDataHolder.setDrivingDirections(directions);

        return hooverAIDataHolder;
    }

    private static boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private static DataPoint convertStringtoDataPoint(String input) {
        // given a string like so "5 5" convert it to a valid data point 
        String[] s = input.split(" ");
        String sx = s[0];
        String sy = s[1];
        DataPoint dp = new DataPoint(Integer.parseInt(sx), Integer.parseInt(sy));
        return dp;
    }

    //public static int compareForPrint(ArrayList<DataPoint> cleanedZones, ArrayList<DataPoint> dirtyZones) {
    public static HooverAIDataHolder compareForPrint(HooverAIDataHolder hooverAIDataHolder) {
        HashSet hMatches = new HashSet();
        ArrayList<DataPoint> cleanedZones = hooverAIDataHolder.getCleanedZones();
        ArrayList<DataPoint> dirtyZones = hooverAIDataHolder.getDirtPatchList();

        int matches = 0;

        for (int i = 0; i < cleanedZones.size(); i++) {

            DataPoint dp = cleanedZones.get(i);
            int dpx = dp.getX();
            int dpy = dp.getY();

            for (int j = 0; j < dirtyZones.size(); j++) {
                DataPoint dp2 = dirtyZones.get(j);

                int dp2x = dp2.getX();
                int dp2y = dp2.getY();

                if (dpx == dp2x) {
                    if (dp.getY() == dp2.getY()) // Yes, cleaned patch.
                    {
                        String addHash = dpx + "." + dpy;
                        if (hMatches.add(addHash)) {
                            matches = matches + 1;  // this is the cleaned patch count
                        }
                    }
                }
            }
        }
        hooverAIDataHolder.setPatchesCleaned(matches);
        hooverAIDataHolder.setHashSetMatches(hMatches);

        return hooverAIDataHolder;
    }

}  // end class
