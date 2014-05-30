
/*The main program that represents the pipelined simulator*/
import java.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;

public class Pipeline {
	
	private Registers Regs;
	private Memory mem;
	private int rs,rt;		
	private int total_inx, logic_count, a_count, mem_count, control_count, base;
	private boolean halt = false;
	private static Instruction[] stages;
	
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
		for(int i=0;i<26;i++){
			// IF
			f.inx_Fetch(f.Regs.getPC());
			//System.out.println("IF");
			// ID
			if(Pipeline.stages[1].getIx() != null){
			//	System.out.println("ID");
				f.ParseInx(Pipeline.stages[1].getIx());
			}
			// EXE
			if(Pipeline.stages[2].getOpcode() != null){
			//	System.out.println("EX");
				f.inx_Execute(Pipeline.stages[2]);
			}
			// MEM
			if(Pipeline.stages[3].getOpcode() != null){
			//	System.out.println("MEM");
				f.memoryAccess(Pipeline.stages[3]);
			}
			// WB
			if(Pipeline.stages[4].getOpcode() != null){
			//	System.out.println("WB");
				f.WriteBack(Pipeline.stages[4]);
				Pipeline.stages[4].printAll();
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
		
		f.Regs.printAll();
	}
	
	//Get instruction from memory address (IF)
	public void inx_Fetch(int address){
		int[] result;
		result = this.mem.fetchFromMem(address);
		stages[0].setIx(Arrays.toString(result));
	}
	
	//Parse the binary instruction and decode it (ID)
	public void ParseInx(String inx){
		String type;
		String[] ops;
		
		// parse and set opcode
		String opcode=getOpcode(inx);
		stages[0].setOpcode(opcode);
		
		// Determine and set instruction type
		if((Integer.parseInt(opcode) < 001011) && Integer.parseInt(opcode)%2 == 0) {
			type = "R";
		} else {
			type = "I";
		}
		
		//System.out.println("type is "+type);
		stages[1].setType(type);
		
		// parse and set operands	
		ops = getOperands(inx, type);
		stages[1].setRS(ops[0]);
		stages[1].setRT(ops[1]);
		stages[1].setOpC(ops[2]);	
	}
		
	//Return opcode from a given instruction
	public String getOpcode(String inx){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		String opcode = temp[0]+temp[1]+temp[2]+temp[3]+temp[4]+temp[5];
		return opcode;
	}
		
	// Return operands for a given instruction
	public String[] getOperands(String inx, String type){
		String[] temp = inx.substring(1,inx.length()-1).split(", ");
		String result[] = new String[3];
		
		// Bits 6-10 are RS, bits 11-15 are RT
		String first_operand = temp[6]+temp[7]+temp[8]+temp[9]+temp[10];
		String second_operand = temp[11]+temp[12]+temp[13]+temp[14]+temp[15];
		result[0] = first_operand;
		result[1] = second_operand;
		
		if(type.equals("R"))
		{
			// bits 16-20 are RD
			String third_operand = temp[16]+temp[17]+temp[18]+temp[19]+temp[20];
			result[2] = third_operand;
		}
		else if (type.equals("I")){
			// remaining bits are Immediate
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

	  return values;
  }
  
  //Perform operation on operands (EX)
  public int inx_Execute( Instruction inx ){
	  int result = 0;
	  String opcode = inx.getOpcode();

	  // get values from registers
	  short RS = (short) Regs.getValue((short) Integer.parseInt(inx.getRS(), 2));
	  short RT = (short)Regs.getValue((short) Integer.parseInt(inx.getRT(), 2));
	  short imm = (short)Integer.parseInt(new BigInteger(inx.getOpC(), 2).toString(10));
	  
	  // perform operation
	  switch (opcode){
	  // ADD
	  case "000000":		
			result = RS + RT;
			break;
	  // ADDI
	  case "000001":
		  	result = RS + imm;
		  	break;
	  // SUB
	  case "000010":
			result = RS - RT;
			break;
	  // SUBI	
	  case "000011":
		  	result = RS - imm;
		  	break;
	  // MUL
	  case "000100":
			result = RS * RT;
			break;
	  // MULI
	  case "000101":
		  	result = RS * imm;
			break;
	  // AND
	  case "000110":
			result = RS & RT;
			break;
	  // ANDI
	  case "000111":
		  	result = RS & imm;
			break;
	  // OR
	  case "001000":
			result = RS | RT;
			break;
	  // ORI
	  case "001001":
		  	result = RS | imm;
			break;
	  // XOR
	  case "001010":
			result = RS ^ RT;
			break;
	  // XORI
	  case "001011":
		  	result = RS ^ imm;
			break;
	  // LDW
	  case "001100":
			result = RS + imm;	  
			break;
	 // STW
	  case "001101":
		    result = RS + imm;
		  	break;  
	  // BEQ
	  case "001110":
		    if(RS == RT)
		    	Regs.setPC(imm);	    
	  // BZ    
	  case "001111":
		   if(RS == 0)
			   Regs.setPC(imm);
	  // JR
	  case "010000":
		  Regs.setPC(RS);
		  break;
	  // HALT
	  case "010001":
		  halt = true;
		  break;
	  // unsupported
	  default:
		  break;
	  }
	  
	  inx.setResult(result);
//	  System.out.println("result is "+result);
	  
	  return result;
  }
  
  //Store value in register (WB)
  public void WriteBack(Instruction inx){	  
	  if(inx.getOpcode().compareTo("1011") < 0){
		  int reg;
		  if(inx.getType() == "R")
			  reg = Integer.parseInt(inx.getOpC(),2);
		  else
			  reg = Integer.parseInt(inx.getRT(),2);
	  
		  int value = inx.getResult();
		  Regs.setValue(reg, value);
	  }
	  
  }
  
  // store or retrieve value from memory (MEM)
  public void memoryAccess(Instruction inx){
	  String opcode = inx.getOpcode();
	  int address = inx.getResult();
	  
	  int temp = this.Regs.getValue(Integer.parseInt(inx.getRT(), 2));
	  String value = Integer.toString(temp, 2);
	  
	  // LDW
	  if(opcode == "001100"){
		  this.mem.fetchFromMem(address);
	  }
	  
	  // STW
	  if(opcode == "001101"){
		  this.mem.setMemory(address, value);
	  } 
  }
 
}
