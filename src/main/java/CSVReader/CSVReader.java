package CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Database.DatabaseConnection;
import General.ConfigReader;

public class CSVReader {

	// Choice of DB
	private static DatabaseConnection connectDB; 

	

	

	public static List<String> getFiles(){
		
		String folderPath = ConfigReader.properties.getProperty("CSVFolder"); 
		List<String> filePaths = new ArrayList<>();
		
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		
		return 	Stream.of(files)
				.map(f->f.getAbsolutePath())
				.filter(f -> f.endsWith(".csv"))
				.collect(Collectors.toList());
	
		 
	}

	private static List<TestResults> readFile(String filePath) {
		
		List<String> lines = new ArrayList<String>();
		List<String> errorLines = new ArrayList<String>();
		 try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			 lines = br.lines()
			 .skip(1)		 
			 .filter(line -> !line.contains("Exception"))
	         .collect(Collectors.toList());
			 errorLines = br.lines()
					 .filter(line -> line.contains("Exception"))
					 .collect(Collectors.toList());
			
	        } catch (IOException e) {
	            e.printStackTrace();
	        }catch (NullPointerException e) {
	            // Handle case where br.lines() returns null (file is empty or not readable)
	            System.err.println("File is empty or cannot be read.");
	        }
			
		 	// write error lines into log file.
		 	try {
				writeErrorLinesToLog(errorLines);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 	// List<TestResultObjects>
			List<TestResults> list =  new ArrayList<TestResults>();
		
			
			
			// if not null lines convert to TestResultsObject
			
			if(lines.size()>0) {
				
				list = 			 lines.stream()
								 .map(CSVReader::createTestResultObjects)
								 .collect(Collectors.toList())
								 ;
				
			}
			
	
		 
			return list;
		 
	}

	private static void writeErrorLinesToLog(List<String> errorLines) throws IOException {
		String fileLocation= "c:\\Users\\u785672\\OneDrive - Lufthansa Group\\RMA\\REPORTS\\log\\error.log";
		File file = new File(fileLocation);
		if(!file.exists()) {
			file.createNewFile();		 		
		}
		trimLogFile(file,10000);
		
		Files.write(file.toPath(), errorLines, StandardOpenOption.APPEND);
	}
	
	  private  static void trimLogFile(File file, int maxLines) throws IOException {
	       

	        List<String> lines = Files.readAllLines(file.toPath());
	        if (lines.size() > maxLines) {
	            List<String> lastLines = lines.subList(lines.size() - maxLines, lines.size());
	            Files.write(file.toPath(), lastLines);
	        }
	    }

	private static TestResults createTestResultObjects(String line) {
		
	
		// Split lines separated by comma
		String[] fields = line.split(",");
		
		// Validate and parse fields
	    String testScope = fields.length > 0 ? fields[0].trim() : "";
	    String date = fields.length > 1 ? fields[1].trim() : "";
	    String time = fields.length > 2 ? fields[2].trim() : "";
	    String env = fields.length > 3 ? fields[3].trim() : "";
	    String description = fields.length > 4 ? fields[4].trim() : "";
	    int timeTaken = parseInteger(fields.length > 5 ? fields[5].trim() : "0");
	    String version = fields.length > 6 ? fields[6].trim() : "";
	
		// create new TestResultObject 
		TestResults object = new TestResults(testScope, // testScope
											date,  // date
											time,  // time
											env,  // env
											description,  // description
											timeTaken,  // time taken
											version);   
		return object;
		
	}
	
	private static int parseInteger(String value) {
	    try {
	        return Integer.parseInt(value);
	    } catch (NumberFormatException e) {
	        return 0; // Default value if parsing fails
	    }
	}
	
	private static String convertDateFormat(String dateStr) {
        String inputFormat = "yyyy-MM-dd";
        String outputFormat = "MM/dd/yyyy";
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
        
        try {
            // Parse the input date string to a Date object
            Date date = inputDateFormat.parse(dateStr);
            // Format the Date object to the desired format
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            // Log an error or handle it as necessary
            System.err.println("Unparseable date: " + dateStr);
            return null;
        }
    }

	static List<TestResults> sortByDate(List<TestResults> listall) {
		return listall.stream()
		.sorted((e1,e2)->e1.parseDate(e1.getDate()).compareTo(e2.parseDate(e2.getDate())))
		.collect(Collectors.toList());
	
	}

	private static void fixDateandTimeFields(List<TestResults> listall) {
		
		// fix dates with format yyyy-MM-dd to MM/dd/yyyy
		listall.stream()
		.filter(e -> e.getDate().contains("-"))
		.forEach(e -> CSVReader.convertDateFormat(e));
		
		
		// fix leading 0s to day and month
		listall.stream()
		.filter(e -> e.getDate().matches("^(\\d)/(\\d{1,2})/(\\d{4})$"))
		.forEach(
				e -> {
			        String originalDate = e.getDate();
			        String[] parts = originalDate.split("/");
			        
			        // Extract day, month, year
			        String day = parts[0];
			        String month = parts[1];
			        String year = parts[2];
			        
			        // Add leading zero if day is a single digit
			        if (day.length() == 1) {
			            day = "0" + day;
			        }
			        if (month.length() == 1) {
			        	month = "0" + month;
			        }
			         
			        
			        // Construct the new formatted date
			        String formattedDate = day + "/" + month + "/" + year;
			        
			        // Print or process the formatted date
			        e.setDate(formattedDate);
			    }
				
				
				
				);
		
		
		
		// Fix Time field , remove : if any at the end
		listall.stream()
		.filter(e -> e.getTime().endsWith(":"))
		.forEach(e -> {
			
			String time = e.getTime();
			String newTime = time.substring(0,time.length()-1);
			//System.out.println(time  + " & " + newTime);
			e.setTime(newTime);
			
			
		});
		
		
		
	}

	private static void convertDateFormat(TestResults t) {
	    String inputFormat = "yyyy-MM-dd";
	    String outputFormat = "MM/dd/yyyy";
	    SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
	    SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);
	    
	    try {
	        // Parse the input date string to a Date object
	        Date date = inputDateFormat.parse(t.getDate());
	        // Format the Date object to the desired format
	        t.setDate(outputDateFormat.format(date));
	    } catch (ParseException e) {
	        // Log an error or handle it as necessary
	        System.err.println("Unparseable date: " + t.getDate());	        
	    }
	}

	public static void csvReadandWriteDB() {
		List<TestResults> listall =  new ArrayList<TestResults>();
		
		// Stream through all files and create TestResultObjects
		listall = getFiles()
				 .stream()
				 .map(l -> readFile(l))
				 .flatMap(List::stream)
				 .collect(Collectors.toList());
	
		
		
		// Do some transformations
		fixDateandTimeFields(listall);
		
	
		// Sort Records by date & Write into Database
		sortByDate(listall).stream().forEach(e -> TestResults.insertData(e));
	}
	
	public static void removeFiles() {
		
		
		getFiles().stream().map(Paths::get).forEach(
			path ->	{
				try {
                    Files.deleteIfExists(path);
                    System.out.println("Deleted: " + path);
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + path);
                    e.printStackTrace();
                }
				
			});
		
	}
	
	
	public static DatabaseConnection getConnectDB() {
		return connectDB;
	}
	
	public static void setConnectDB(DatabaseConnection connectDB) {
		CSVReader.connectDB = connectDB;
	}
	
}
