package algo.trade.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * implements base REST calls as a common service
 * 
 * @author Abhinav Shetty
 *
 */
@Service
public class BaseRestService {

	@Autowired
	private RestTemplate restTemplate;

	private MultiValueMap<String, String> initializeHeaders(Map<String, String> inputHeaders) {
		MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
		result.set("content-type", "application/json");
		
		// add custom headers
		if (inputHeaders != null) {
			for (String key : inputHeaders.keySet()) {
				result.set(key, inputHeaders.get(key));
			}
		}

		return result;
	}

	/**
	 * makes POST REST call for no input and gets single object output
	 * 
	 * @return Object of type T
	 */
	public <T extends Object> T postWithoutInputForSingleOutput(Map<String, String> inputHeaders, String url,
			Class<T> outputClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(headers);
		Object restResponse = restTemplate.exchange(url, HttpMethod.POST, request, outputClass).getBody();

		headers = null;
		url = null;

		return outputClass.cast(restResponse);
	}

	/**
	 * makes POST REST call for object input and gets single object output
	 * 
	 * @return Object of type T
	 */
	public <T extends Object> T postWithSingleInputForSingleOutput(Object input, Map<String, String> inputHeaders,
			String url, Class<T> outputClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(input, headers);
		Object restResponse = restTemplate.exchange(url, HttpMethod.POST, request, outputClass).getBody();

		headers = null;
		input = null;
		url = null;

		return outputClass.cast(restResponse);
	}

	/**
	 * makes POST REST call for no input and gets list output
	 * 
	 * @return ArrayList of objects of type T
	 */
	public <T> List<T> postWithoutInputForListOutput(Map<String, String> inputHeaders, String url, Class<T> outputListClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(headers);
		List<Object> restResponse = restTemplate
				.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<List<Object>>() {
				}).getBody();
		List<T> result = new ArrayList<>();

		for (Object item : restResponse) {
			result.add(outputListClass.cast(item));
		}

		headers = null;
		url = null;
		request = null;
		restResponse = null;
		outputListClass = null;

		return result;
	}

	/**
	 * makes POST REST call for object input and gets list output
	 * 
	 * @return ArrayList of objects of type T
	 */
	public <T> List<T> postWithSingleInputForListOutput(Map<String, String> inputHeaders, Object input, String url, Class<T> outputListClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(input, headers);
		List<Object> restResponse = restTemplate
				.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<List<Object>>() {
				}).getBody();
		List<T> result = new ArrayList<>();

		for (Object item : restResponse) {
			result.add(outputListClass.cast(item));
		}

		headers = null;
		url = null;
		request = null;
		restResponse = null;
		input = null;
		outputListClass = null;

		return result;
	}

	/**
	 * makes GET REST call for no input and gets single object output
	 * 
	 * @return Object of type T
	 */
	public <T extends Object> T getWithoutInputForSingleOutput(Map<String, String> inputHeaders, String url, Class<T> outputClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(headers);

		Object restResponse = restTemplate.exchange(url, HttpMethod.GET, request, outputClass).getBody();

		headers = null;
		url = null;
		request = null;

		return outputClass.cast(restResponse);
	}

	/**
	 * makes GET REST call for no input and gets list output
	 * 
	 * @return List of objects of type T
	 */
	public <T> List<T> getWithoutInputForListOutput(Map<String, String> inputHeaders, String url, Class<T> outputListClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(headers);

		List<Object> restResponse = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Object>>() {
				}).getBody();
		List<T> result = new ArrayList<>();

		for (Object item : restResponse) {
			result.add(outputListClass.cast(item));
		}

		headers = null;
		url = null;
		request = null;
		restResponse = null;
		outputListClass = null;

		return result;
	}

	/**
	 * makes GET REST call for object input and gets single object output
	 * 
	 * @return Object of type T
	 */
	public <T extends Object> T getWithSingleInputForSingleOutput(Map<String, String> inputHeaders, Object input, String url, Class<T> outputClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(input, headers);

		Object restResponse = restTemplate.exchange(url, HttpMethod.GET, request, outputClass).getBody();

		headers = null;
		url = null;
		request = null;
		input = null;

		return outputClass.cast(restResponse);
	}

	/**
	 * makes GET REST call for object input and gets list output
	 * 
	 * @return List of objects of type T
	 */
	public <T> List<T> getWtihSingleInputForListOutput(Map<String, String> inputHeaders, Object input, String url, Class<T> outputListClass) {
		MultiValueMap<String, String> headers = initializeHeaders(inputHeaders);
		HttpEntity<?> request = new HttpEntity<>(input, headers);
		List<Object> restResponse = restTemplate
				.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Object>>() {
				}).getBody();
		List<T> result = new ArrayList<>();

		for (Object item : restResponse) {
			result.add(outputListClass.cast(item));
		}

		headers = null;
		url = null;
		request = null;
		restResponse = null;
		outputListClass = null;
		input = null;

		return result;
	}
}
