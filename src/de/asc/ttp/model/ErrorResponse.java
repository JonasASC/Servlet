/**
 * 
 */
package de.asc.ttp.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * @author jonas
 *
 */
public class ErrorResponse {
	
	@Expose
	public Integer id = 0;
	public String message = null;

	/**
	 * 
	 */
	public ErrorResponse() {	
	}

	/**
	 * 
	 */
	public ErrorResponse(Integer code, String message) {
		this.id = code;
		this.message = message;
	}
	
	public ErrorResponse(Integer code, Exception e) {
		this.id = code;
		this.message = e.getMessage();
	}

	/**
	 * 
	 * @return
	 */
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}

}
