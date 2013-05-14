import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
public class ReadIdentify {
	// We need three things for the language detection step:
	// 1) langdetect.jar (download this from https://code.google.com/p/language-detection/) 
	// 2) jsonic-1.2.5.jar (This jar is needed for the langdetect.jar)
	// 3) profiles (This is a directory that has all the text files for language identifaction)
	public static void main(String[] args){
		// First read in the keywords that we want to identify as an array
		// This is a list of keywords that I made into lower case, I also have the code to extract the body of the tweets and convert them to lower case
		// The keywords are put into a list
		String keywords=null;
		List<String> keylist = new ArrayList<String>();
		try{
			FileReader fileReader= new FileReader("keywords.txt");
			BufferedReader bufferedReader= new BufferedReader(fileReader);
			while((keywords=bufferedReader.readLine())!=null){
				keylist.add(keywords);
			}
		}
		catch(FileNotFoundException ex){
			System.out.println(
					"Unable to open file");	
		}
		catch(IOException ex){
			System.out.println("Error reading file");
		}

		// This part loads in the langauge text files in the directory profiles. The langdetect jar will refer to the text files inside here to detect the language
		String line = null;
		File dir = new File ("profiles");
		try{
			DetectorFactory.loadProfile(dir);
		}catch(LangDetectException ex){
			System.out.print ("WTF");
		}catch(Exception e){
			System.out.print ("WTF");
		}
		try{
			// For the stand alone java file, I read in the text line by line, but for the hadoop step this is not required
			FileReader fileReader= new FileReader("outputclean.txt");
			BufferedReader bufferedReader= new BufferedReader(fileReader);
			while((line=bufferedReader.readLine())!=null){
				for (int i=0; i<keylist.size(); i++){
					if (line.contains(keylist.get(i))){
						// the 'contains' function allows us to see which keyword is in this tweet itself
						System.out.println(line+" : "+keylist.get(i));

		try{
						// This is the tweet langauge identification step
						Detector detector = DetectorFactory.create();
						detector.append(line);
						String lang = detector.detect();
						System.out.println(lang);
		}catch(LangDetectException ex){
			System.out.print ("WTF");
		}
					}
				}
			}
		}
		catch(FileNotFoundException ex){
			System.out.println(
					"Unable to open file");	
		}
		catch(IOException ex){
			System.out.println("Error reading file");
		}


	}
}
