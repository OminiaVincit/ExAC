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

public class ExACReaderOneHashmap {
	public static void main(final String args[]) {
		final long startTime = System.currentTimeMillis();

		final String ExAC_FILE = args[0];

		final List<String> KEYWORDS = Arrays.asList("AC", "AC_EAS", "AN",
				"AN_EAS");
		final File file = new File(ExAC_FILE);

		try {
			// Read ExAC file into one large hashmap file
			final HashMap<String, String> map = new HashMap<String, String>();
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
				String info_txt = String.format("%s:", alt);
				final String chr = String.format("chr%s", parts[0]); 
				final String info_parts[] = info.split(";");
				for (int i = 0; i < info_parts.length; i++) {
					final String kvls[] = info_parts[i].split("=");
					if (KEYWORDS.contains(kvls[0])) {
						info_txt = String.format("%s%s,",info_txt,info_parts[i]);
					}
				}
				// Remove last comma in info_txt
				if (info_txt.endsWith(",")) {
					info_txt = info_txt.substring(0, info_txt.length() - 1);
				}
				
				//final ChrObj chrpos = new ChrObj(chr, pos);
				final String chrpos = String.format("%s%s",chr,pos);
				map.put(chrpos, info_txt);				
				System.out.println(chr + "\t" + pos + "\t" + info_txt);
			}
			br.close();
			// Output serialize hashmapY
			final String OutFileName = "hashmap.ser";
			final FileOutputStream fos = new FileOutputStream(OutFileName);
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
			fos.close();
			System.out.println("Seriazlized HashMap data is save in " + OutFileName );
			
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
