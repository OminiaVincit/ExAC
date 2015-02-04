package vcfstream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class VcfSplitAdder {
	public static void main (final String[] args) {
		final long startTime = System.currentTimeMillis();
		try {
			
			// Read from hashmap.ser for test
			FileInputStream fis = new FileInputStream("/Users/zoro/work/Genome/ExACdata/hashmap_file/"+"hashmap1.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			HashMap<String, String> readmap = (HashMap) ois.readObject();
			ois.close();
			fis.close();
			for (int i = 0; i < args.length; i++) {
				final BufferedReader br = new BufferedReader(new FileReader(new File(args[i])));
				final BufferedWriter wr = new BufferedWriter(new FileWriter(new File(args[i]+".out")));
				for (String line; (line = br.readLine()) != null;) {
					if (line.startsWith("#"))
						continue;
					final String parts[] = line.split("\t"); // $chr, $pos
					if (parts.length < 2) continue;				
					// Search 
					final String pos = parts[1];
					final String value = (String)readmap.get(pos);
					if (value != null) {
						wr.write(line + " ExAC: " + value + "\n");
					} else {
						wr.write(line + " ExAC: " + "\n");
					}
				}
				br.close();
				wr.close();
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(final ClassNotFoundException c) {
			c.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Get total execution time
		final long finishTime = System.currentTimeMillis();

		System.out.println("Total time: " + (finishTime - startTime)
				+ " miliseconds!");
	}
}
