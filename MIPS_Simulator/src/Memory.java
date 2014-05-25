/*Represents the memory used for the simulator */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;


public class Memory {
	
	private int start_address = 0x0;
	private int final_address = 0x400;  //4kB memory
	private int[] data;
	static int j=0;
	BufferedReader reader;

	
	public Memory(String file_location) throws IOException{
		data = new int[32768];
		readFile(file_location);
	}

	
	/* Reads the input file and parses it to prepare the initial memory image */
	public void readFile(String file) throws IOException{
		reader = new BufferedReader(new FileReader(file));
		String line;
		while((line = reader.readLine())!=null)	{
			String inx = Memory.ToBinary(line);
			String[] temp = inx.split("");
			for(int i=0;i<temp.length;i++){
				data[j] = Integer.parseInt(temp[i]);
				j++;
			}
		}
	
	}
	//Returns value at an address in memory
	public int[] getByte(int address){
		int[] result = new int[8];
		for(int i=0;i<8;i++){
			result[i] = data[address+i];
		}

		return result;
	}
	
	public int[] fetchFromMem(int address){
		int[] result = new int[32];
		for(int i=0;i<32;i++){
			result[i] = data[address+i];
		}

		return result;
	}
	
	//Sets value of an address in memory
	public void setMemory(int address,String value){
		for(int i=0;i<value.length();i++){
			data[address+i]=Integer.parseInt(String.valueOf(value.charAt(i)));
		}
 	}
	
	public static String ToBinary(String line){
		StringBuilder sb = new StringBuilder();
		String result = new BigInteger(line,16).toString(2);
		for(int i=32-result.length();i>0;i--){
			sb.append('0');
		}
		sb.append(result);
		return sb.toString();
	}
}