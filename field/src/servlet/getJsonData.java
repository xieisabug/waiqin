package servlet;


import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class getJsonData extends HttpServlet {

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedInputStream in = new BufferedInputStream(request
				.getInputStream());
		
		StringBuilder responseBuffer = new StringBuilder();
		String readLine;

		// 处理响应流，必须与服务器响应流输出的编码一致
		BufferedReader responseReader = new BufferedReader(
				new InputStreamReader(in, "utf-8"));
		while ((readLine = responseReader.readLine()) != null)
		{
			System.out.println(readLine);
			responseBuffer.append(readLine).append("\n");
		}
		
		//读取输入流的信息
		System.out.println("Http Response:" + responseBuffer.toString());
		JSONArray array = JSONArray.fromObject(responseBuffer.toString());
        for (Object anArray : array) {
            //JSONObject jsonObject = array.getJSONObject(i);
            System.out.println("111111:" + anArray);

        }
		 response.getWriter().write("1");
		 
}
}