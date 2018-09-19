package cn.dmego.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.dmego.pojo.TreeNode;

public class TreeUtil {

	public static List<TreeNode> getNodeList(Map<String, TreeNode> nodelist) {
		List<TreeNode> tnlist = new ArrayList<TreeNode>();
		for(String id: nodelist.keySet()){
			TreeNode node = nodelist.get(id);
			if(StringUtils.isBlank(node.getParentId())){ //如果该节点是父节点（该节点没有父ID）
				tnlist.add(node); //将该父节点加入nodelist
			}else{ //是子节点
				//如果该子节点的父节点下的子节点集合为null
				if(nodelist.get(node.getParentId()).getNodes() == null){
					//给该子节点的父节点初始化一个子节点集合
					nodelist.get(node.getParentId()).setNodes(new ArrayList<TreeNode>());
				}
				//将当前节点加入到当前节点的父节点的子节点集合属性中去
				nodelist.get(node.getParentId()).getNodes().add(node);
			}
		}
		return tnlist;
	}
	
	
	/**
	 * 组织单位构造treeData
	 * @param nodelist
	 * @return
	 */
	public static List<TreeNode> getOrgNodeList(Map<String, TreeNode> nodelist) {
		List<TreeNode> tnlist = new ArrayList<TreeNode>();
		for(String id: nodelist.keySet()){
			TreeNode node = nodelist.get(id);
			tnlist.add(node); //将该节点加入nodelist
		}
		return tnlist;
	}
	
	
}
