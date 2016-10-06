package app.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
@ConfigurationProperties(prefix="cloudinary")
public class CloudinaryService {

	private String cloudName;
	private String apiKey;
	private String apiSecret;
	
	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> upload(String url) throws IOException {
		//String ecoUrl = getCloudinary().url().transformation(new Transformation().quality("auto:eco").fetchFormat("auto")).type("fetch").imageTag(url);
		Transformation incoming = new Transformation().quality("auto:eco").fetchFormat("jpg").width(1000).height(1000).crop("limit");
		Map<String, Object> returnedMap = ObjectUtils.emptyMap();
		returnedMap = (Map<String, Object>) getCloudinary().uploader().upload(url, ObjectUtils.asMap("transformation", incoming));
		Map<String, String> myMap = new HashMap<>();
		myMap.put("url", (String) returnedMap.get("secure_url"));
		myMap.put("public_id", (String) returnedMap.get("public_id"));
		return myMap;
	}
	
	@Bean
	public Cloudinary getCloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
			"cloud_name", cloudName,
			"api_key", apiKey,
			"api_secret", apiSecret)
		);
	}
}