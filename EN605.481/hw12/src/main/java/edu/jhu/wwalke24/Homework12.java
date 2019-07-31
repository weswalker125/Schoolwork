package edu.jhu.wwalke24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Homework12", urlPatterns = { "/homework12" }, loadOnStartup = 1)
public class Homework12 extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder sb = new StringBuilder();
		
		// Read POST input stream
		try (InputStream is = request.getInputStream()) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = br.read(charBuffer)) > 0) {
	                sb.append(charBuffer, 0, bytesRead);
	            }
			}
		}
		
		// Decode and split by &.
		String[] keyValuePairs = URLDecoder.decode(sb.toString(), "UTF-8").split("&");
		// Set for use in JSP template.
		request.setAttribute("processedDataArray", keyValuePairs);
		// Load JSP template.
		request.getRequestDispatcher("results.jsp").forward(request, response);
	}
}
