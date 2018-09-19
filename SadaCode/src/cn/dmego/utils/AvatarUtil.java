package cn.dmego.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

/**
 * 头像上传工具类
 * @author zengk
 *
 */
public class AvatarUtil {

	/**
	 * 完成头像上传的同时，返回路径path
	 * @param file：上传的头像文件
	 * @param string：上传的文件名
	 * @param userHome：用户文件夹名
	 * @return：返回头像路径
	 * 
	 * 1：完成头像上传的要求
		  1：将上传的头像放置到upload里的userName文件夹下,并命名为avatar.jpg(png)
		  2：保存路径path的时候，使用相对路径进行保存，这样便于项目的可移植性
	 */
	public static String fileUploadReturnPath(File file, String fileName, String userHome) {
		//1:获取需要上传文件统一的路径path（即upload）
		String basepath = ServletActionContext.getServletContext().getRealPath("/upload");
		//2.格式（upload\admin）
		String filePath = basepath+"/"+userHome;
		//3：判断该文件夹是否存在，如果不存在，创建
		File dateFile = new File(filePath);
		if(!dateFile.exists()){
			dateFile.mkdirs();//创建
		}
		
		//6：指定对应的文件名
		//文件的后缀
		String prifix = fileName.substring(fileName.lastIndexOf("."));
		//重命名文件名为avatar
		String avatar = "avatar"+prifix;
		//最终上传的文件（目标文件）
		File destFile = new File(dateFile,avatar);
		//上传文件
		file.renameTo(destFile);
		return "/upload"+userHome+"/"+avatar;
	}
	
	public static void main(String[] args) {
		File srcFile = new File("G:\\upload\\avatar.jpg");
		File destFile = new File("F:\\dir\\dir2ddddd\\a.txt");
		//复制
		try {
			org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//剪切
//		boolean flag = srcFile.renameTo(destFile);
//		System.out.println(flag);
	}

}
