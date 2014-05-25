
public class Instruction {
	
	private String ix;			// binary instruction from memory
	private String opcode;		// operation
	private String type;		// instruction type (R or I)
	private	String RD;			// destination register
	private String opA;			// first source register
	private String opB;			// second source register (R-type) or immediate (I-type)
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

	public void setRD(String value){
		RD = value;
	}
	
	public String getRD(){
		return RD;
	}

	public void setOpA(String value){
		opA = value;
	}
	
	public String getOpA(){
		return opA;
	}
	
	public void setOpB(String value){
		opB = value;
	}
	
	public String getOpB(){
		return opB;
	}

	public void setResult(int value) {
		result = value;
		
	}
	
	public int getResult(){
		return result;
	}

	public void clearAll() {
		ix = null;	
		opcode = null;		
		type = null;		
		RD = null;			
		opA = null;			
		opB = null;			
		result = 0;	
	}
}


