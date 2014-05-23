/*The main program that represents the simulator*/
import java.io.BufferedReader;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;

public class Simulator {
	private BufferedReader reader;
	private Registers Regs;
	private Memory mem;
	int rs,rt,rd;
	int[] temp3;
	int imm,address,val_from_mem;
	//short imm;
	static int total_inx, logic_count,a_count,mem_count, control_count,base;
	String[] temp_Array;
	String type,inx,mem_val,temp,temp2, op1,op2;
	static boolean halt = false;
	public Simulator() throws IOException{
		Regs = new Registers();
		mem = new Memory("./src/file.txt");
	}
	
	
	public static void main(String args[]) throws IOException{
		Simulator f = new Simulator();
			while(halt==false){
			//for(int i=0;i<2;i++){
			int[] inx = f.inx_Fetch(f.Regs.getPC()); //fetch the next instruction from the memory
			f.ParseInx(Arrays.toString(inx));	//Decode the instruction 
			f.Regs.incrementPC();				//increment PC to point to 
			}
		
		
		
	}
	
	//Convert String instruction to binary
	//Get instruction from memory address
	public int[] inx_Fetch(int address){
		int[] result;
		result = this.mem.fetchFromMem(address);
		return result;
	}
	
	//Parse the binary instruction to decode it
	public void ParseInx(String inx){
		String binary_inx = inx; //Memory.ToBinary(inx);
		System.out.println(inx);
		String[] result;
		int value, dec_value,reg_value ;
		int[] decimal_result,val_from_memory;
		String opcode=getOpcode(inx);		//decode
		switch(opcode) {
		
		//ADD
		case "000000":
			System.out.println(opcode);				//result = string[], decimal_result = int[] 
			type = "R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1= Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"add");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			a_count++;
			total_inx++;
			break;
			
		//ADDI	
		case "000001":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			System.out.println("value of imm"+imm);
			value = inx_Execute(op1,null,result[2],"addi");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			a_count++;
			total_inx++;
			break;
		
		//SUB	
		case "000010":
			type="R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1= Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"sub");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			a_count++;
			total_inx++;
			break;
			
		//SUBI	
		case "000011":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null,result[2],"subi");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			a_count++;
			total_inx++;
			break;
		
		//MUL
		case "000100":
			type="R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd  = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"mul");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			a_count++;
			total_inx++;
			break;
		
		//MULI
		case "000101":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null,result[2],"multi");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			a_count++;
			total_inx++;
			break;
		
		
		//AND
		case "000110":
			type="R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1= Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"and");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			logic_count++;
			total_inx++;
			
			break;
		
		//ANDI
		case "000111":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null,result[2],"andi");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			logic_count++;
			total_inx++;
			break;
		
		//OR
		case "001000":
			type="R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1= Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"or");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			logic_count++;
			total_inx++;
			break;
		
		//ORI
		case "001001":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null,result[2],"ori");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			logic_count++;
			total_inx++;
			break;
			
		//XOR
		case "001010":
			type="R";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[2],2).toString(10));
			op1= Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"xor");
			mem.fetchFromMem(0);
			WriteBack(rd,value,true);
			logic_count++;
			total_inx++;
			
			break;
		
		//XORI
		case "001011":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null,result[2],"subi");
			mem.fetchFromMem(0);
			WriteBack(rd, value,true);
			logic_count++;
			total_inx++;
			break;
			
		
		//LDW
		case "001100":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			value = inx_Execute(op1,null, result[2],"add");
			int[] val_from_mem = mem.fetchFromMem(value);
			String temp = Arrays.toString(val_from_mem);
			String[] temp_Array = temp.substring(1,temp.length()-1).split(", ");
			for(int i=0;i<temp_Array.length;i++){
				mem_val+=temp_Array[i];
			} 
			dec_value = Integer.parseInt(mem_val,2);
			WriteBack(rd, value,true);
			mem_count++;
			total_inx++;
			break;
			
		//STW	
		case "001101":
			type="I";
			result = getOperands(binary_inx,type);
			decimal_result = get_rs_rt(result);
			rd = Integer.parseInt(new BigInteger(result[1],2).toString(10));
			reg_value = Regs.getValue(rd);
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			int address = inx_Execute(op1,null, result[2], "addi");
			mem.setMemory(address, Integer.toBinaryString(reg_value));
			WriteBack(0,0,false);
			mem_count++;
			total_inx++;
			break;
			
		//BEQ	
		case "001110":
			type="I";
			result = getOperands(binary_inx, type);
			decimal_result = get_rs_rt(result);
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"beq");
			if(value==1){
				Regs.setPC(Regs.PC+(Integer.parseInt(result[2])*32));
			}
			mem.fetchFromMem(0);
			control_count++;
			total_inx++;
			break;
			
		//BZ	
		case "001111":
			result = getOperands(binary_inx, type);
			decimal_result = get_rs_rt(result);
			op1 = Integer.toBinaryString(Regs.getValue(decimal_result[0]));
			op2 = Integer.toBinaryString(Regs.getValue(decimal_result[1]));
			value = inx_Execute(op1,op2,null,"bz");
			if(value==1){
				Regs.setPC(Regs.PC+(Integer.parseInt(result[2])*32));
			}
			mem.fetchFromMem(0);
			WriteBack(0,0,false);
			control_count++;
			total_inx++;
			break;
		
		//JR	
		case "010000":
			result = getOperands(binary_inx, type);
			decimal_result = get_rs_rt(result);
			value = Regs.getValue(decimal_result[0]);
			if(value!=0)
				Regs.setPC((value-1)*32);
			else
				Regs.setPC(0);
			mem.fetchFromMem(0);
			WriteBack(0,0,false);
			control_count++;
			total_inx++;
			halt=true;
			System.out.println("PC is :"+Regs.getPC());
			break;
			
		//HALT	
		case "010001":
			halt=true;
			mem.fetchFromMem(0);
			WriteBack(0,0,false);
			control_count++;
			total_inx++;
			break;
				
			
		default:
			break;
			
		}
	}
	
	//Decode the instruction based on the type = R or I"
	
	public String getOpcode(String inx){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		//System.out.println(temp[1]+""+temp[2]);
		String opcode = temp[0]+temp[1]+temp[2]+temp[3]+temp[4]+temp[5];
		return opcode;
	}
	
	
	
	public String[] getOperands(String inx,String type){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		String result[] = new String[4];
		String first_operand = temp[6]+temp[7]+temp[8]+temp[9]+temp[10];
		String second_operand = temp[11]+temp[12]+temp[13]+temp[14]+temp[15];
		result[0] = first_operand;
		result[1] = second_operand;
		if(type.equals("R"))
		{
		String third_operand = temp[16]+temp[17]+temp[18]+temp[19]+temp[20];
		result[2] = third_operand;
		System.out.println(result[0]);
		System.out.println(result[1]);
		
		}
		else if (type.equals("I")){
		String imm_operand = temp[16]+temp[17]+temp[18]+temp[19]+temp[20]+
				temp[21]+temp[22]+temp[23]+temp[24]+temp[25]+temp[26]+temp[27]+temp[28]
				+temp[29]+temp[30]+temp[31];
		result[2] = imm_operand;
				
		}
		return result;
		
	}

//Binary to string
  public int[] get_rs_rt(String[] result){
	  int values[] = new int[2];
	  rs = Integer.parseInt(new BigInteger(result[0],2).toString(10));
	  rt = Integer.parseInt(new BigInteger(result[1],2).toString(10));
	  values[0] = rs;
	  values[1] = rt;
	  System.out.println(values[0]+""+values[1]);
	  return values;
  }
  public int inx_Execute(String a, String b, String c, String operator ){
	  int result = 0;	
	  short first, second;
	  switch (operator){
	  case "add":
			first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first + second;
			break;
	  case "addi":
		  	first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(c,2).toString(10));
		  	result = first + second;
		  	//System.out.println(result);
		  	break;
	  case "sub":
		    first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first - second;
			break;
			
	  case "subi":
		    first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(c,2).toString(10));
		  	result = first - second;
		  	break;
	  case "mult":
		  	first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first * second;
			break;
	  case "multi":
		    first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(c,2).toString(10));
		  	result = first * second;
			break;
	  case "and":
		  	first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first & second;
			break;
	  case "andi":
		    first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(c,2).toString(10));
		  	result = first & second;
			break;
	  case "or":
		  	first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first | second;
			break;
	  case "ori":
		  	first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(c,2).toString(10));
		  	result = first | second;
			break;
	  case "xor":
		  	first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
			result = first ^ second;
			break;
	  case "xori":
		    first = (short) Integer.parseInt(a,2);
		  	second = (short)Integer.parseInt(new BigInteger(b,2).toString(10));
		  	result = first ^ second;
			break;
			
	  case "beq":
		    first = (short) Integer.parseInt(a,2);
			second = (short) Integer.parseInt(b,2);
		    if(first == second)
		    	result = 1;
		    else
		    	result = 0;
		    
	  case "bz":
		   first = (short)Integer.parseInt(a,2);
		   second = (short)Integer.parseInt(b,2);
		   if(first == 0)
		    	result = 1;
		    else
		    	result = 0;
	  }
	  
	  return result;
  }
  
  public void WriteBack(int a, int value, boolean flag){
	  if(flag==true){
	  Regs.setValue(a, value);
	  System.out.println(Regs.getValue(a));}
	  
  }
 
}
