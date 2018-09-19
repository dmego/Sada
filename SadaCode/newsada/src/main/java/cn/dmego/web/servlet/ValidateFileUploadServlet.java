package cn.dmego.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.dmego.domain.User;
import cn.dmego.utils.AssetIdentifyUtil;
import cn.dmego.utils.DateUtil;

/**
 * Servlet implementation class FileUploadServlet
 */
public class ValidateFileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static Map<String,String> fileTypeMap = new HashMap<String,String>();
	
	static{
		//TODO 添加文件格式
		fileTypeMap.put("png", "图片");
		fileTypeMap.put("jpg", "图片");
		fileTypeMap.put("jpeg", "图片");
		fileTypeMap.put("bmp", "图片");
		fileTypeMap.put("pdf", "文档");
		fileTypeMap.put("doc", "文档");
		fileTypeMap.put("docx", "文档");
		fileTypeMap.put("xls", "文档");
	}
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ValidateFileUploadServlet() {
		super();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String realPath = this.getServletContext().getRealPath("/");
		System.out.println("realPath:" + realPath);
//		File realPathDir = new File(realPath);
//		if (!realPathDir.exists()) {
//			realPathDir.mkdirs();
//		}
		String tmpPath = this.getServletContext().getRealPath("tmp");
		System.out.println("tmpPath:" + tmpPath);
		File tmpPathDir = new File(tmpPath);
		if (!tmpPathDir.exists()) {
			tmpPathDir.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024 * 10);
		factory.setRepository(tmpPathDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setProgressListener(new ProgressListener() {

			@Override
			public void update(long readedBytes, long totalBytes, int currentItem) {
				System.out.println("已处理：" + readedBytes + "---总共：" + totalBytes);
			}
		});

		upload.setHeaderEncoding("UTF-8");
		if (!ServletFileUpload.isMultipartContent(request)) {
			System.out.println("不是多媒体方式上传");
			return;
		}
		upload.setFileSizeMax(1024 * 1024 * 100);
		upload.setSizeMax(1024 * 1024 * 1000);
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem fileItem = iterator.next();
				if (fileItem.isFormField()) {
					String name = fileItem.getFieldName();
					String value = fileItem.getString("UTF-8");
					System.out.println("普通表单 " + name + "=" + value);
				} else {
					String fileName = fileItem.getName();
					if (fileName == null || fileName.trim().length() == 0) {
						System.out.println("文件名为空");
						continue;
					}
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
					String commonType = fileTypeMap.get(fileType);
					if(commonType == null){
						commonType = "其他";
					}
					System.out.println("文件名为：" + fileName + "\t扩展名为：" + fileType);
					
					
					if (fileItem.getSize() == 0) {
						System.out.println("文件大小为0");
						continue;
					}
//					if (fileItem.getSize() > 1024 * 1024 * 1) {
//						System.out.println("文件大于1M");
//						continue;
//					}
					InputStream inputStream = fileItem.getInputStream();
					User user = (User) request.getSession().getAttribute("user");
					String username = user.getName();
					String filePath =  "upload/" + username + "/鉴权/" + commonType;
					File filePathF = new File(realPath+filePath);
					if(!filePathF.exists()){
						filePathF.mkdirs();
					}
					String uuidStr = UUID.randomUUID().toString().replaceAll("-", "");
					fileName = uuidStr + "_" + fileName;//保存的文件名
					FileOutputStream out = new FileOutputStream(realPath + "/" + filePath + "/" + fileName);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = inputStream.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					out.close();
					inputStream.close();
					fileItem.delete();
					System.out.println("文件" + fileName + "上传成功！");
					String result = "{\"fileName\":\"" + fileName + "\",\"filePath\":\"" + filePath + "\",\"fileType\":\"" + fileType + "\",\"commonType\":\"" + commonType + "\"}";
					response.getWriter().print(result);
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
			response.getWriter().print("error");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}

}
