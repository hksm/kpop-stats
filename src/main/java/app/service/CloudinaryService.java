package app.service;

import java.io.File;
import java.io.IOException;
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
	
	public String upload(File file) throws IOException {
		return (String) getCloudinary().uploader().upload(file, ObjectUtils.emptyMap()).get("secure_url");
	}
	
	public String upload(File file, Map<String,Object> map) throws IOException {
		return (String) getCloudinary().uploader().upload(file, map).get("secure_url");
	}
	
	public String upload(String url) throws IOException {
		String ecoUrl = getCloudinary().url().transformation(new Transformation().quality("auto:eco").fetchFormat("auto")).type("fetch").imageTag(url);
		return (String) getCloudinary().uploader().upload(ecoUrl, ObjectUtils.emptyMap()).get("secure_url");
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