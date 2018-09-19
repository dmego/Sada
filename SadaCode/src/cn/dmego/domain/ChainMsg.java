package cn.dmego.domain;

import java.util.Arrays;

/**
 * @Name: ChainMsg
 * @Description: TODO
 * @Author: 刘西宁
 * @Version: V1.00
 * @Create Date: 2018年6月14日
 * 
 */
public class ChainMsg {
	private String id;
	private String chaincodeName;
	private String argsStr;
	private String[] args;
	private String delimit;
	private String username;
	private String function;

	public String getChaincodeName() {
		return chaincodeName;
	}

	public void setChaincodeName(String chaincodeName) {
		this.chaincodeName = chaincodeName;
	}

	public String getArgsStr() {
		return argsStr;
	}

	public void setArgsStr(String argsStr) {
		this.argsStr = argsStr;
	}

	public String[] getArgs() {
		if(args != null){
			return args;
		}
		if(argsStr != null){
			if(delimit != null){
				args = argsStr.split(delimit);
			}
			else{
				args = argsStr.split("-");
			}
		}
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public String getDelimit() {
		return delimit;
	}

	public void setDelimit(String delimit) {
		this.delimit = delimit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	  
	    /* (非 Javadoc)  
	    *   
	    *   
	    * @return  
	    * @see java.lang.Object#toString()  
	    */  
	    
	@Override
	public String toString() {
		return "ChainMsg [id=" + id + ", chaincodeName=" + chaincodeName + ", argsStr=" + argsStr + ", args="
				+ Arrays.toString(args) + ", delimit=" + delimit + ", username=" + username + ", function=" + function
				+ "]";
	}
	
	
	
	

}
