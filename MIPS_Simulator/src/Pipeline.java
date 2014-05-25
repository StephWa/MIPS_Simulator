
/*The main program that represents the pipelined simulator*/
import java.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;

public class Pipeline {
	
	private Registers Regs;
	private Memory mem;
	int rs,rt,rd;
	int[] temp3;
	int imm,address,val_from_mem;
	static int total_inx, logic_count,a_count,mem_count, control_count,base;
	String[] temp_Array;
	static boolean halt = false;
	static boolean stall = false;
	static Instruction[] stages;
	
	public Pipeline() throws IOException{
		Regs = new Registers();
		mem = new Memory("./src/file.txt");
		stages = new Instruction[5];
		for(int i=0;i<5;i++){
			stages[i] = new Instruction();
		}
	}
	
	
	public static void main(String args[]) throws IOException{
		Pipeline f = new Pipeline();
		for(int i=0;i<15;i++){
			// IF
			f.inx_Fetch(f.Regs.getPC());
			System.out.println("IF");
			// ID
			if(Pipeline.stages[1].getIx() != null){
				System.out.println("ID");
				f.ParseInx(Pipeline.stages[1].getIx());
			}
			// EXE
			if(Pipeline.stages[2].getOpcode() != null){
				System.out.println("EX");
				f.inx_Execute(Pipeline.stages[2]);
			}
			// MEM
			if(Pipeline.stages[3].getOpcode() != null){
				System.out.println("MEM");
				f.memoryAccess(Pipeline.stages[3]);
			}
			// WB
			if(Pipeline.stages[4].getOpcode() != null){
				System.out.println("WB");
				f.WriteBack(Pipeline.stages[4]);
			}

			// shift pipeline
			Pipeline.stages[4] = Pipeline.stages[3];
			Pipeline.stages[3] = Pipeline.stages[2];
			Pipeline.stages[2] = Pipeline.stages[1];
			Pipeline.stages[1] = Pipeline.stages[0];
			Pipeline.stages[0].clearAll();
			
			// increment PC to point to next instruction
			f.Regs.incrementPC();					  	
		}	
	}
	
	//Get instruction from memory address (IF)
	public void inx_Fetch(int address){
		int[] result;
		result = this.mem.fetchFromMem(address);
		stages[0].setIx(Arrays.toString(result));
	}
	
	//Parse the binary instruction and decode it (ID)
	public void ParseInx(String inx){
		System.out.print("instruction to parse:");
		System.out.println(inx);
		String type;
		String[] ops;
		
		// parse and set opcode
		String opcode=getOpcode(inx);
		stages[0].setOpcode(opcode);
		System.out.println("opcode is:");
		System.out.println(opcode);	
		
		// Determine and set instruction type
		if((Integer.parseInt(opcode) < 001011) && Integer.parseInt(opcode)%2 == 0) {
			type = "R";
		} else {
			type = "I";
		}
		
		System.out.println("type is:");
		System.out.println(type);
		stages[1].setType(type);
		
		// parse and set operands	
		ops = getOperands(inx, type);
		stages[1].setRD(ops[0]);
		stages[1].setOpA(ops[1]);
		stages[1].setOpB(ops[2]);	
	}
		
	//Return opcode from a given instruction"
	public String getOpcode(String inx){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		String opcode = temp[0]+temp[1]+temp[2]+temp[3]+temp[4]+temp[5];
		return opcode;
	}
		
	// Return operands for a given instruction
	public String[] getOperands(String inx, String type){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		String result[] = new String[4];
		
		// Bits 6-10 are destination register, bits 11-15 are first operand
		String first_operand = temp[6]+temp[7]+temp[8]+temp[9]+temp[10];
		String second_operand = temp[11]+temp[12]+temp[13]+temp[14]+temp[15];
		result[0] = first_operand;
		result[1] = second_operand;
		
		if(type.equals("R"))
		{
			// bits 16-20 are second operand
			String third_operand = temp[16]+temp[17]+temp[18]+temp[19]+temp[20];
			result[2] = third_operand;
			System.out.println("first operand is "+result[0]);
			System.out.println("second operand is "+result[1]);
		}
		else if (type.equals("I")){
			// remaining bits are second operand
			String imm_operand = temp[16]+temp[17]+temp[18]+temp[19]+temp[20]+
				temp[21]+temp[22]+temp[23]+temp[24]+temp[25]+temp[26]+temp[27]+temp[28]
				+temp[29]+temp[30]+temp[31];
			result[2] = imm_operand;		
		}
		return result;
		
	}

  //Convert to decimal
  public int[] get_rs_rt(String[] result){
	  int values[] = new int[2];
	  rs = Integer.parseInt(new BigInteger(result[0],2).toString(10));
	  rt = Integer.parseInt(new BigInteger(result[1],2).toString(10));
	  values[0] = rs;
	  values[1] = rt;
	  System.out.println("RS in decimal: "+values[0]);
	  System.out.println("RT in decimal: "+values[1]);
	  return values;
  }
  
  //Perform operation on operands (EX)
  public int inx_Execute( Instruction inx ){
	  int result = 0;
	  short first, second;
	  
	  // get operation, type and operands
	  String opcode = inx.getOpcode();
	  String type = inx.getType();
	  String opA = inx.getOpA();
	  String opB = inx.getOpB();

	  // convert operands to integers
	  first = (short) Integer.parseInt(opA,2);
	  if(type == "R")
		  second = (short) Integer.parseInt(opB,2);
	  else 
		  second = (short)Integer.parseInt(new BigInteger(opB,2).toString(10));
	  
	  // perform operation
	  switch (opcode){
	  // ADD
	  case "000000":		
			result = first + second;
			break;
	  // ADDI
	  case "000001":
		  	result = first + second;
		  	break;
	  // SUB
	  case "000010":
			result = first - second;
			break;
	  // SUBI	
	  case "000011":
		  	result = first - second;
		  	break;
	  // MUL
	  case "000100":
			result = first * second;
			break;
	  // MULI
	  case "000101":
		  	result = first * second;
			break;
	  // AND
	  case "000110":
			result = first & second;
			break;
	  // ANDI
	  case "000111":
		  	result = first & second;
			break;
	  // OR
	  case "001000":
			result = first | second;
			break;
	  // ORI
	  case "001001":
		  	result = first | second;
			break;
	  // XOR
	  case "001010":
			result = first ^ second;
			break;
	  // XORI
	  case "001011":
		  	result = first ^ second;
			break;
	  // BEQ
	  case "001110":
		    if(first == second)
		    	result = 1;
		    else
		    	result = 0;
	  // BZ    
	  case "001111":
		   if(first == 0)
		    	result = 1;
		    else
		    	result = 0;
	  // JR
	  case "010000":
		  break;
	  // HALT
	  case "010001":
		  break;
	  // unsupported
	  default:
		  break;
	  }
	  
	  inx.setResult(result);
	  System.out.println("result is "+result);
	  
	  return result;
  }
  
  //Store value in register (WB)
  public void WriteBack(Instruction inx){
	  int reg = Integer.parseInt(inx.getRD(),2);
	  int value = inx.getResult();
	  
	  Regs.setValue(reg, value);
	  
  }
  
  // store or retrieve value from memory (MEM)
  public void memoryAccess( Instruction inx){
	  String opcode = inx.getOpcode();
	  
	  // LDW
	  if(opcode == "001100"){
		  
	  }
  			
	  // STW
	  if(opcode == "001101"){
		  
	  }
  
  }
 
}
