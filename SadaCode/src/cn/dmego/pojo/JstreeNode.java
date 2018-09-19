package cn.dmego.pojo;

public class JstreeNode {
	private String id; //节点id
	private String parent; //节点父id
	private String text; //节点显示名称
	private String type; //节点类型
	private NodeState state; //节点状态（是否被选中）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public NodeState getState() {
		return state;
	}
	public void setState(NodeState state) {
		this.state = state;
	}
	
	
}
