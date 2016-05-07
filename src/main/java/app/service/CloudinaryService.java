package app.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
@ConfigurationProperties(prefix="cloudinary")
public class CloudinaryService {

	private String cloudName;
	private String apiKey;
	private String apiSecret;

	private Cloudinary cloudinary;
	
	public CloudinaryService() {
		cloudinary = new Cloudinary(ObjectUtils.asMap(
			"cloud_name", cloudName,
			"api_key", apiKey,
			"api_secret", apiSecret)
		);
	}
	
	public void upload(File file) throws IOException {
		cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
	}
	
	public void upload(File file, Map<String,Object> map) throws IOException {
		cloudinary.uploader().upload(file, map);
	}
}
