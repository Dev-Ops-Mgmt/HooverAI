# HooverAI
This project is for demonstration only.  Thank you.

This project emulates an automated vacuum based on an x,y coordinate system.  The program takes a file input.txt in the following format:<br>
5 5<br>
1 2<br>
1 0<br>
2 2<br>
2 3<br>
NNESEESWNWW<br>

The first x,y pair 5 5 is the size of the grid the vacuum can hoover over.
The second x,y pair is the starting location of the vacuum.
The third  through N pairs of x,y coordinates are known patches which need to be cleaned.
The last line is driving instructions with N for go north, S for go south, E for go East and W for go West.

The program can be ran by downloading the files and compiling them in any IDE or with javac.  HooverAI was written using Java 1.8.

Program can be run from command line using: java ext.tools.com.hooverai.HooverAI

Note the input.txt file is expected at the same location as the HooverAI.class.  If no input.txt file is found the program defaults to the input shown above as a sample.



Appendix - File Descriptions:


ext.tools.hooverai.HooverAI  // Main class, manages program flow and output

ext.tools.hooverai.model.DataPoint // Simple data structure to hold x,y coordinates
ext.tools.hooverai.model.HooverAIDataHolder // Data structure to hold all program data input and output as well as runtime discovery

ext.tools.hooverai.engine.HooverAIDataProcessor // Engine class which drives the hoover for processing

input.txt  // input file which resides in same direcory as HooverAI.class
HooverAI.properties // properties file for debug which resides in same directory as HooverAI.class



