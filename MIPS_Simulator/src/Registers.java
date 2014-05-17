/* Represents the registers used in the simulator, has 32 GPR, PC*/
public class Registers {

	public int R[];
	public static int PC;
	
	public Registers(){
		R = new int[32];
		for(int i=0;i<32;i++){
			R[i] = 0;
		}
		PC = 0;
		
	}
	
	// Returns value in a register
	public int getValue(int i){
		return R[i];
	}
	
	//Sets a register value
	public void setValue(int i,int x){
		R[i] = x;
	}
	
	//Returns value of PC
	public int getPC(){
		return PC;
	}
	
	//Increments PC to point to next instruction in memory
	public void incrementPC(){
		PC=PC+4;
	}
}
