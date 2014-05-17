/*The main program that represents the simulator*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;

public class Simulator {
	private BufferedReader reader;
	private Registers Regs;
	private Memory mem;
	int rs,rt,rd,im;
	String type,inx;
	public Simulator() throws IOException{
		Regs = new Registers();
		mem = new Memory("./src/file.txt");
	}
	
	
	public static void main(String args[]) throws IOException{
		Simulator f = new Simulator();
		for(int i=0;i<4;i++){
			String inx = f.inx_Fetch(f.Regs.PC); //fetch the next instruction from the memory
			System.out.println(inx);			  
			f.ParseInx(inx);					//Decode the instruction 
			f.Regs.incrementPC();				//increment PC to point to 
			
		}
		
		
	}
	
	//Convert String instruction to binary
	public String ToBinary(String line){
		StringBuilder sb = new StringBuilder();
		String result = new BigInteger(line,16).toString(2);
		for(int i=32-result.length();i>0;i--){
			sb.append('0');
		}
		sb.append(result);
		return sb.toString();
	}
	
	
	//Get instruction from memory address
	public String inx_Fetch(int address){
		String inx = this.mem.getValue(address);
		return inx;
	}
	
	//Parse the binary instruction to decode it
	public void ParseInx(String inx){
		String binary_inx = this.ToBinary(inx);
		System.out.println(binary_inx);
		String opcode = binary_inx.subSequence(0, 6).toString();
		switch(opcode) {
		
		// Add rd rs rt
		case "000000":
			System.out.println(opcode);
			type = "R";
			decode(binary_inx, type);
			Regs.setValue(rd, Regs.R[rs]+Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
			
		//Addi rt rs im	
		case "000001":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs]+im);
			System.out.println(Regs.R[rt]);
			break;
		
		//Sub rd rs rt	
		case "000010":
			System.out.println(opcode);
			type="R";
			decode(binary_inx,type);
			Regs.setValue(rd, Regs.R[rs]-Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
			
		//Subi rt rs imm		
		case "000011":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs]-im);
			System.out.println(Regs.R[rt]);
			break;
			
		//Mul rd rs rt	
		case "000100":
			System.out.println(opcode);
			type="R";
			decode(binary_inx, type);
			Regs.setValue(rd, Regs.R[rs] * Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
		
		//Muli rt rs imm
		case "000101":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs] * im);
			System.out.println(Regs.R[rt]);
			break;
		
		//AND rd rs rt	
		case "000110":
			System.out.println(opcode);
			type="R";
			decode(binary_inx, type);
			Regs.setValue(rd, Regs.R[rs] & Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
		
		//ANDi rt rs imm	
		case "000111":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs] & im);
			System.out.println(Regs.R[rt]);
			break;
			
		//OR rd rs rt		
		case "001000":
			System.out.println(opcode);
			type="R";
			decode(binary_inx, type);
			Regs.setValue(rd, Regs.R[rs] | Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
		
		//ORi rt rs imm		
		case "001001":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs] | im);
			System.out.println(Regs.R[rt]);
			break;
		
		//XOR rd rs rt	
		case "001010":
			System.out.println(opcode);
			type="R";
			decode(binary_inx, type);
			Regs.setValue(rd, Regs.R[rs] ^ Regs.R[rt]);
			System.out.println(Regs.R[rd]);
			break;
		
		//XORi rt rs imm		
		case "001011":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);
			Regs.setValue(rt, Regs.R[rs] ^ im);
			System.out.println(Regs.R[rt]);
			break;
			
		//LDW Rt Rs Imm	
		case "001100":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);	
			Regs.setValue(rt, Memory.getValue(Regs.R[rs] + im));
			System.out.println(Regs.R[rt]);
			break;
			
		//STW Rt Rs Imm		
		case "001101":
			System.out.println(opcode);
			type="I";
			decode(binary_inx, type);	
			Mem.setValue(Regs.R[rs] + im, Regs.R[rt]);
			System.out.println( Memory.getValue(Regs.R[rs] + im) );
			break;
		
		//BEQ Rs Rt x	
		case "001110":
			System.out.println("00000000");
			break;
			
		//Bz Rs x	
		case "001111":
			System.out.println(opcode);
			break;
		
		//JR Rs	
		case "010000":
			System.out.println(opcode);
			break;
		
		//HALT	
		case "010001":
			System.out.println("00000000");
			break;
			

			
		default:
			break;
			
		}
	}
	
	//Decode the instruction based on the type = R or I"
	public void decode(String inx, String type){
		
		
		switch(type) {
	
		case "R":
		String rs_binary = inx.subSequence(6, 11).toString();
		rs = Integer.parseInt(new BigInteger(rs_binary,2).toString(10));
		String rt_binary = inx.subSequence(11,16).toString();
		rt = Integer.parseInt(new BigInteger(rt_binary,2).toString(10));
		String rd_binary = inx.subSequence(16,21).toString();
		rd = Integer.parseInt(new BigInteger(rd_binary,2).toString(10));
		System.out.println(rs+" "+rt+ " "+ rd);
			break;
		
		case "I":
		String rs_binary = inx.subSequence(6, 11).toString();
		rs = Integer.parseInt(new BigInteger(rs_binary,2).toString(10));
		String rt_binary = inx.subSequence(11,16).toString();
		rt = Integer.parseInt(new BigInteger(rt_binary,2).toString(10));
		//Immediate field
		String im_binary = inx.subSequence(16,31).toString();
		im = Integer.parseInt(new BigInteger(rd_binary,2).toString(10));
		System.out.println(rs+" "+rt+ " "+ im);
		
			break;
		
		default:
			break;
	}

	}
}