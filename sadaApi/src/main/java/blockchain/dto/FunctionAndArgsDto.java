package blockchain.dto;

public class FunctionAndArgsDto {
	
	private String chaincodeName; //链码的名称
	String function; //函数的名称
	String[] args; //参数数组
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	} 

	public String getChaincodeName() {
		return chaincodeName;
	}

	public void setChaincodeName(String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}
	

}
