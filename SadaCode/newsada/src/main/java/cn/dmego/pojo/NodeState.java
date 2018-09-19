package cn.dmego.pojo;

/**
 * Jstree 的节点状态
 * @author zengk
 * 
 */
public class NodeState {
	private boolean selected; //是否被选中

	public NodeState() {
	
	} 
	public NodeState(boolean selected) {
		this.selected = selected;
	}

	public boolean getSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
