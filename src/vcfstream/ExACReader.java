package vcfstream;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExACReader {
	public static void main(String args[]) {
		long startTime = System.currentTimeMillis(); 
//		for (int i = 0; i < args.length; i++ ){
//			System.out.println( args[i] );
//		}
		String ExAC_FILE = args[1];
		String VCF_FILE  = args[0];
		
		List<String> KEYWORDS = Arrays.asList("AC","AC_EAS","AN","AN_EAS");
		
		File file = new File(ExAC_FILE);
		
		// List of hashmap
//		List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
//		
//		for (int i = 0; i < 24; i++) {
//			Map<String, String> map = new HashMap<String, String>();
//			listMaps.add(map);
//		}
		
		//Read ExAC file into hashmap
		Map<ChrObj, String> map = new HashMap<ChrObj, String>();
		try {
			BufferedReader br = new BufferedReader( new FileReader(file));
			String str;
			while ((str = br.readLine()) != null) {
				if (str.startsWith("#")) continue;
				String parts[] = str.split("\t"); //($chr, $start, $id, $ref, $alt, $qual, $filter, $info)
				if (parts.length < 8) continue;
				
				String chr = "chr"+parts[0];
				String pos = parts[1];
				String alt = parts[4];
				String info = parts[7];
				String info_txt = alt + ":";
				
				String info_parts[] = info.split(";");
				for (int i = 0; i < info_parts.length; i++) {
					String kvls[] = info_parts[i].split("=");
					if ( KEYWORDS.contains(kvls[0])) {
						info_txt += info_parts[i] + ",";
					}
				}
				// Remove last comma in info_txt
				if( info_txt.endsWith(",")) {
					info_txt = info_txt.substring(0, info_txt.length() - 1);
				}
				ChrObj chrpos = new ChrObj( chr, pos );
				map.put(chrpos, info_txt);
				
				System.out.println(chr + "\t" +pos + "\t" + info_txt);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		// Get read ExAC file execution time
		long hashTime = System.currentTimeMillis();
		
		// Get total execution time
		long finishTime = System.currentTimeMillis();

		System.out.println("Hash time: "  + (hashTime - startTime) + " miliseconds!" );
		System.out.println("Total time: " + (finishTime - startTime) + " miliseconds!" );
	}
}

	