package vcfstream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HashmapReader {
	public static void main(String[] args) {
		// Read from hashmap.ser for test
		HashMap<String, String> readmap = null;
		try {
			FileInputStream fis = new FileInputStream("hashmap_file/hashmapY.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			readmap = (HashMap) ois.readObject();
			ois.close();
			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException c) {
			c.printStackTrace();
		}
		
		System.out.println("Deserialized HashMap..");
	      // Display content using Iterator
	      Set set = readmap.entrySet();
	      Iterator iterator = set.iterator();
	      while(iterator.hasNext()) {
	         Map.Entry mentry = (Map.Entry)iterator.next();
	         System.out.print("key: "+ mentry.getKey() + " & Value: ");
	         System.out.println(mentry.getValue());
	      }

	}
}
