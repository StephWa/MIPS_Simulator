
public class Instruction {
	
	private String ix;			// binary instruction from memory
	private String opcode;		// operation
	private String type;		// instruction type (R or I)
	private	String opC;			// destination register (R-type) or immediate (I-type)
	private String RS;			// first register
	private String RT;			// second register
	private int result;			// result of executing operation
	
	public void setIx(String value) {
		ix = value;	
	}
	
	public String getIx() {
		return ix;
	}

	public void setOpcode(String value) {
		opcode = value;
		
	}
	
	public String getOpcode(){
		return opcode;
	}

	public void setType(String value) {
		type = value;
		
	}
	
	public String getType(){
		return type;
	}

	public void setOpC(String value){
		opC = value;
	}
	
	public String getOpC(){
		return opC;
	}

	public void setRS(String value){
		RS = value;
	}
	
	public String getRS(){
		return RS;
	}
	
	public void setRT(String value){
		RT = value;
	}
	
	public String getRT(){
		return RT;
	}

	public void setResult(int value) {
		result = value;
		
	}
	
	public int getResult(){
		return result;
	}

	public void clearAll(){
		ix = null;	
		opcode = null;		
		type = null;		
		opC = null;			
		RS = null;			
		RT = null;			
		result = 0;	
	}
	
	public void printAll(){
		System.out.println("INSTRUCTION DUMP");
		System.out.println("ix is "+ix);
		System.out.println("opcode is "+opcode);
		System.out.println("type is "+type);
		System.out.println("opC is "+Integer.parseInt(opC, 2));
		System.out.println("RS is "+Integer.parseInt(RS, 2));
		System.out.println("RT is "+Integer.parseInt(RT, 2));
		System.out.println("result is "+result);
	}
}


