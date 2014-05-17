/*Represents the memory used for the simulator */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Memory {
	
	private int start_address = 0x0;
	private int final_address = 0x400;  //4kB memory
	private String[] data;
	BufferedReader reader;
	//File f;
	
	public Memory(String file_location) throws IOException{
		data = new String[1024];
		readFile(file_location);
	}

	
	/* Reads the input file and parses it to prepare the initial memory image */
	public void readFile(String file) throws IOException{
		reader = new BufferedReader(new FileReader(file));
		int line_no = 0;
		String line,binary_inx;
		while((line = reader.readLine())!=null)	{
			data[line_no] = line;
			System.out.println(line);
			line_no=line_no+4;
			
		}
	
	}
	//Returns value at an address in memory
	public String getValue(int address){
		String result = data[address];
		return result;
	}
	
	//Sets value of an address in memory
	public void setValue(int address,String value){
		data[address] = value;
 	}
}