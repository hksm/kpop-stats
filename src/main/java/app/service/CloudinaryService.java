package app.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
@ConfigurationProperties(prefix="cloudinary")
public class CloudinaryService {

	private String cloudName;
	private String apiKey;
	private String apiSecret;
	
	public void upload(File file) throws IOException {
		getCloudinary().uploader().upload(file, ObjectUtils.emptyMap());
	}
	
	public void upload(File file, Map<String,Object> map) throws IOException {
		getCloudinary().uploader().upload(file, map);
	}
	
	@Bean
	public Cloudinary getCloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
			"cloud_name", cloudName,
			"api_key", apiKey,
			"api_secret", apiSecret)
		);
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
}