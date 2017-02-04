package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConverterTest {
    private String csvString;
    private String jsonString;

    @Before
    public void setUp() {
		try
		{
			jsonString = Converter.fileReader("src/test/resources/grades.json");
			csvString = Converter.fileReader("src/test/resources/grades.csv");
		}catch(IOException e){e.printStackTrace();}
    }
    
    @Test
    public void testConvertCSVtoJSON() {
        // You should test using the files in src/test/resources.
        assertTrue(Converter.jsonStringsAreEqual(Converter.csvToJson(csvString), jsonString));
    }

    @Test
    public void testConvertJSONtoCSV() {
        // You should test using the files in src/test/resources.
		
        assertEquals(Converter.jsonToCsv(jsonString), csvString);
    }
}







