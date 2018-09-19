package cn.dmego.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FileDownload
 */
public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDownloadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("UTF-8");
		String filePath = request.getParameter("filePath");
		String fileName = request.getParameter("fileName");
		String realPath = request.getServletContext().getRealPath("/");
		System.out.println("fileName:" + fileName);
		File file = new File(realPath + "/" + filePath + "/" + fileName);
		if(!file.exists()){
			System.out.println("文件不存在");
			return;
		}
		FileInputStream inputStream = new FileInputStream(file); 
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));//不适用火狐
        response.setContentType("application/octet-stream");
        OutputStream out = response.getOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while((len = inputStream.read(b)) != -1){
        	out.write(b, 0, len);
        }
        inputStream.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
