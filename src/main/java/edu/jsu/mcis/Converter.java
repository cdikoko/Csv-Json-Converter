package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    /*
        Consider a CSV file like the following:
        
        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500
        
        The corresponding JSON file would be as follows (note the curly braces):
        
        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }  
    */
	
	public static String fileReader(String path) throws IOException
	{
		File input = new File(path);
		Scanner scan = new Scanner(input);
		String output = "";
		
			while(scan.hasNextLine())
			{
				output += scan.nextLine() + "\n" ;
			}
			scan.close();
		
		return output;
	}
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString)
	{
		
		JSONObject jsonObject = new JSONObject();
		
		JSONArray colHeaders = new JSONArray();
		colHeaders.add("Total");
		colHeaders.add("Assignment 1");
		colHeaders.add("Assignment 2");
		colHeaders.add("Exam 1");
		
		JSONArray rowHeaders = new JSONArray();
		JSONArray data = new JSONArray();
		
		jsonObject.put("colHeaders", colHeaders);
		jsonObject.put("rowHeaders", rowHeaders);
		jsonObject.put("data", data);
		
		
		
			CSVParser parser = new CSVParser();
			BufferedReader b =  new BufferedReader(new StringReader(csvString));
			
			try
			{
				String line = b.readLine();
					
					while((line = b.readLine()) != null)
					{
						String[] input = parser.parseLine(line);
						JSONArray dataRow = new JSONArray();
						
						rowHeaders.add(input[0]);
						dataRow.add(new Integer(input[1]));
						dataRow.add(new Integer(input[2]));
						dataRow.add(new Integer(input[3]));
						dataRow.add(new Integer(input[4]));
						data.add(dataRow);
					
					}
			} catch(IOException e){}
			
		return jsonObject.toString();
    }
    
    public static String jsonToCsv(String jsonString) 
	{
		JSONObject jsonObject = null;
		
		try
		{
			JSONParser parser = new JSONParser();
			jsonObject = (JSONObject) parser.parse(jsonString);
		
		}catch(Exception e){	e.printStackTrace();	}
		
		String csvContent = "\"ID\"," + Converter.<String>joinArray((JSONArray) jsonObject.get("colHeaders")) + "\n";
		
		JSONArray rowHeaders = (JSONArray) jsonObject.get("rowHeaders");
		JSONArray rowData = (JSONArray) jsonObject.get("data");
		int numHeaders = rowHeaders.size();
		
			for(int i = 0; i < numHeaders; i++)
			{
				csvContent += ( "\"" + (String) rowHeaders.get(i) + "\"," + Converter.<Double>joinArray((JSONArray) rowData.get(i)) + "\n");
				
			}
        return csvContent;
    }
	
	@SuppressWarnings("unchecked")
    private static <T> String joinArray(JSONArray inputJSONArray)
	{
        String ouput = "";
        for (int i = 0, length = inputJSONArray.size(); i < length; i++)
		{
			ouput += "\"" + ((T) inputJSONArray.get(i)) + "\"";
			
			if (i < length - 1) 
			{
				ouput += ",";
			}
        }
        return ouput;
    }
	
	
	public static boolean jsonStringsAreEqual(String a, String b) 
	{
        try 
		{
            return jsonEqaul(new JSONParser().parse(a), new JSONParser().parse(b));
        } catch (Exception exception) { exception.printStackTrace();
            return false;
        }
    }
	
	  private static boolean jsonEqaul(Object a, Object b)
	  {
		  
        if (a instanceof JSONObject && b instanceof JSONObject) 
		{
            return jsonObjectEqaul((JSONObject) a, (JSONObject) b);
			
        }
		
		else if (a instanceof JSONArray && b instanceof JSONArray)
		{
            return jsonArrayEqaul((JSONArray) a, (JSONArray) b);
        } 
		
		else 
		{
            return a.equals(b);
        }
    }
	
	private static boolean jsonObjectEqaul(JSONObject a, JSONObject b) 
	{
        for (Object k : a.keySet()) 
		{
            String key = (String) k;

            if (!b.containsKey(key) || !jsonEqaul(a.get(key), b.get(key))) 
			{
                return false;
            }
        }
        return true;
    }
    
    private static boolean jsonArrayEqaul(JSONArray a, JSONArray b) 
	{
        int aSize = a.size();

        if (aSize != b.size()) 
		{
            return false;
        }
		else 
		{
            for (int i = 0, il = aSize; i < il; i++) 
			{
                if (!jsonEqaul(a.get(i), b.get(i))) 
				{
                    return false;
                }
            }
            return true;
        }
    }
}













