/*
 * FRC 4931 (http://www.evilletech.com)
 *
 * Open source software. Licensed under the FIRST BSD license file in the
 * root directory of this project's Git repository.
 */
package org.frc4931.utils;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class LogDecoder {
    public static void main(String[] args) {
        String inFile = null;
        String outFile = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-in":
                    inFile = args[++i];
                    break;
                case "-out":
                    outFile = args[++i];
                    break;
                default:
                    System.out.println("Usage: LogDecoder [-in INFILE] [-out OUTFILE]");
                    System.exit(1);
            }
        }

        if (inFile == null) {
            inFile = "robot.log";
        }
        if (outFile == null) {
            outFile = inFile.split("\\.", 2)[0] + ".csv";
        }

        try {
            File file = new File(inFile);
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            File out = new File(outFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            
            // Verify Header
            byte[] header = new byte[3];
            in.read(header);
            if(!Arrays.equals(header, "log".getBytes())){
                System.err.println("File format not recognized");
                System.exit(2);
            }
            
            // Get the number of elements
            int numElements = in.read();
            
            // Get the size of each element
            int[] elementSizes = new int[numElements];
            for(int i = 0; i< elementSizes.length; i++)
                elementSizes[i] = in.read();
            
            // Write the name of each element
            for(int i = 0; i< numElements; i++) {
                int nameSize = in.read();
                byte[] b = new byte[nameSize];
                in.read(b);
                writer.write(new String(b) + ", ");
            }
            writer.newLine();
            
            // Read each record
            in.mark(4);
            while(in.readInt()!=0xFFFFFFFF){
                in.reset();
                for(int i = 0; i < numElements; i++){
                    if(elementSizes[i]==4){
                        writer.write(in.readInt() + ", ");
                    }
                    else if(elementSizes[i]==2)
                        writer.write(in.readShort() + ", ");
                }
                writer.newLine();
                in.mark(4);
            }
            writer.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("Log file not found");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
