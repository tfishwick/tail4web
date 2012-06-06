package ca.tef.tail4web;

import java.util.HashMap;
import java.util.Map;

public class JsonResponse {
	Map<String, Object> result = new HashMap<String, Object>();

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

}
