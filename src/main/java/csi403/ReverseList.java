package csi403;


// Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;


// Extend HttpServlet class
public class ReverseList extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
	  try {
      doService(request, response); 
	  }catch(Exception EX) {
		// Set response content type to be JSON
	      response.setContentType("application/json");
	      // Send back the response JSON message
	      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
	      PrintWriter out = response.getWriter();
	      out.println("Malformed JSON" + outArrayBuilder.build().toString()); 
	  }
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }


  // Our main worker method
  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
  // Returns the list reversed.   
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");

      // Reverse the data in the list
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
      /*for (int i = inArray.size() - 1; i >= 0; i--) {
          outArrayBuilder.add(inArray.getInt(i)); 
      }*/
      
      double array[] = new double[inArray.size()];
      for (int i = 0; i < inArray.size(); i++) {
    	  array[i] = Double.parseDouble(inArray.getString(i));
      }
      double temp = 0;
      for (int i=0; i < array.length; i++) {
    	  for(int j = 1; j < array.length; j++) {
    		  if(array[j-1] > array[j]) {
    			  temp = array[j-1];
    			  array[j-1] = array[j];
    			  array[j] = temp;
    		  }
    	  }
    	  
      }
      
      for (int i=0; i < array.length; i++) {
    	  outArrayBuilder.add(array[i]);
      }
      
      /* int n = numArray.length;
      int temp = 0;
      for (int i = 0; i < n; i++) {
          for (int j = 1; j < n; j++) {
              if (numArray[j - 1] > numArray[j]) {
                  temp = numArray[j - 1];
                  numArray[j - 1] = numArray[j];
                  numArray[j] = temp;
              }
          }
      }
      */
      
      // Set response content type to be JSON
      response.setContentType("application/json");
      // Send back the response JSON message
      PrintWriter out = response.getWriter();
      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}"); 
  }

    
  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

