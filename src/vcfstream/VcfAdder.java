package vcfstream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VcfAdder {
	public static void main (final String[] args) {
		final long startTime = System.currentTimeMillis();
		final String VCF_FILE = args[0];
		final File file = new File(VCF_FILE);
		
		try {
			final BufferedReader br = new BufferedReader(new FileReader(file));
			// Read from hashmap.ser for test
			HashMap<String, String> readmap = new HashMap<String, String>();
			
			// For clear map
			int prev_chr = 0;
			
			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith("#"))
					continue;
				final String parts[] = line.split("\t"); // $chr, $pos
				if (parts.length < 2) continue;
				// Extract index of chromosome after "chr"
				final int len = parts[0].length();
				final String chr = parts[0].substring(3, len);
				int cur_chr = prev_chr;
				try {
					cur_chr = Integer.parseInt(chr);
				} catch(NumberFormatException e){
					if ( chr.equals("X") ) cur_chr = 23;
					else if ( chr.equals("Y") ) cur_chr = 24;
					else cur_chr = prev_chr;
				} 
				if (cur_chr == 10) break;
				// Clear map and re-mapping if chromosome index has been changed
				if ( cur_chr != prev_chr ){
					readmap.clear();
					try {
						FileInputStream fis = new FileInputStream("/Users/zoro/work/Genome/ExACdata/hashmap_file/"+"hashmap"+chr+".ser");
						ObjectInputStream ois = new ObjectInputStream(fis);
						readmap = (HashMap) ois.readObject();
						ois.close();
						fis.close();
					} catch(IOException e) {
						e.printStackTrace();
					} catch(ClassNotFoundException c) {
						c.printStackTrace();
					}
					
					// Update prev_chr
					prev_chr = cur_chr;
				}
				
				// Search 
				final String pos = parts[1];
				final String value = (String)readmap.get(pos);
				if (value != null) {
					System.out.println(line + " ExAC: " + value);
				} else {
					System.out.println(line + " ExAC:-");
				}
			}
			br.close();
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Get total execution time
		final long finishTime = System.currentTimeMillis();

		System.out.println("Total time: " + (finishTime - startTime)
				+ " miliseconds!");
	}
}
