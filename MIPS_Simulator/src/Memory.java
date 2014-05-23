/*Represents the memory used for the simulator */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;


public class Memory {
	
	private int start_address = 0x0;
	private int final_address = 0x400;  //4kB memory
	private int[] data;
	static int j=0;
	BufferedReader reader;
	//File f;
	
	public Memory(String file_location) throws IOException{
		data = new int[32768];
		readFile(file_location);
	}

	
	/* Reads the input file and parses it to prepare the initial memory image */
	public void readFile(String file) throws IOException{
		reader = new BufferedReader(new FileReader(file));
		int line_no = 0;
		//byte[] temp;
		String line,binary_inx;
		while((line = reader.readLine())!=null)	{
			String inx = this.ToBinary(line);
			String[] temp = inx.split("");
			for(int i=1;i<temp.length;i++){
				data[j] = Integer.parseInt(temp[i]);
				j++;
			}
			//line_no=line_no+4;
			//System.out.println(data);
		}
		//System.out.println(this.getValue(2));
	
	}
	//Returns value at an address in memory
	public int[] getByte(int address){
		int[] result = new int[8];
		for(int i=0;i<8;i++){
			result[i] = data[address+i];
		}
		String val = result.toString();
		System.out.println();
		return result;
	}
	
	public int[] fetchFromMem(int address){
		int[] result = new int[32];
		for(int i=0;i<32;i++){
			result[i] = data[address+i];
		}
		//System.out.println(result[0]+""+ result[1]+"" + result[2]+""+result[3]+""+result[4]);
		return result;
	}
	
	//Sets value of an address in memory
	public void setMemory(int address,String value){
		//int[] n = new int [value.length()];
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