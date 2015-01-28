package vcfstream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExACReader {
	public static void main(final String args[]) {
		final long startTime = System.currentTimeMillis();

		final String ExAC_FILE = args[0];

		final List<String> KEYWORDS = Arrays.asList("AC", "AC_EAS", "AN",
				"AN_EAS");
		final File file = new File(ExAC_FILE);

		try {
			// Read ExAC file into 24 hashmap file
			final HashMap<String, String> map = new HashMap<String, String>();
			int prev_chr = 1;

			final BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line; (line = br.readLine()) != null;) {
				if (line.startsWith("#"))
					continue;
				final String parts[] = line.split("\t"); // ($chr, $start,
															// $id, $ref,
															// $alt, $qual,
															// $filter,
															// $info)
				if (parts.length < 8)
					continue;

				final String pos = parts[1];
				final String alt = parts[4];
				final String info = parts[7];
				String info_txt = alt + ":";

				final String info_parts[] = info.split(";");
				for (int i = 0; i < info_parts.length; i++) {
					final String kvls[] = info_parts[i].split("=");
					if (KEYWORDS.contains(kvls[0])) {
						info_txt += info_parts[i] + ",";
					}
				}
				// Remove last comma in info_txt
				if (info_txt.endsWith(",")) {
					info_txt = info_txt.substring(0, info_txt.length() - 1);
				}

				// Write to hashmap file if finish one type of chr
				int cur_chr = 1;
				if ( parts[0].equals("X") ) cur_chr = 23;
				else if ( parts[0].equals("Y") ) cur_chr = 24;
				else cur_chr = Integer.parseInt(parts[0]);
					
				if ( cur_chr != prev_chr ) {
					// Output serialize hashmap
					final String OutFileName = "hashmap"
							+ prev_chr + ".ser";
					final FileOutputStream fos = new FileOutputStream(OutFileName);
					final ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(map);
					oos.close();
					fos.close();
					System.out
							.println("Seriazlized HashMap data is save in " + OutFileName );

					// Clear hashmap to free heap space memory
					map.clear();
					prev_chr = cur_chr;
				} else {
					// Put into map
					//final ChrObj chrpos = new ChrObj(parts[0], pos);
					map.put(pos, info_txt);
				}
				System.out.println("chr" + parts[0] + "\t" + pos + "\t" + info_txt);
			}
			br.close();
			// Output serialize hashmapY
			final String OutFileName = "hashmap"
					+ "Y" + ".ser";
			final FileOutputStream fos = new FileOutputStream(OutFileName);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
			fos.close();
			System.out
					.println("Seriazlized HashMap data is save in " + OutFileName );

			// Clear hashmap to free heap space memory
			map.clear();
			
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// Get read ExAC file execution time
		final long hashTime = System.currentTimeMillis();

		// Get total execution time
		final long finishTime = System.currentTimeMillis();

		System.out.println("Hash time: " + (hashTime - startTime)
				+ " miliseconds!");
		System.out.println("Total time: " + (finishTime - startTime)
				+ " miliseconds!");
	}
}
