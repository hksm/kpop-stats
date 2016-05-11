package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.model.Album;
import app.model.Artist;

@Service
@ConfigurationProperties(prefix="lastfm")
public class LastfmService {

	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	private String apiKey;
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	@SuppressWarnings("unchecked")
	public String getMainArtistPicUrl(Artist artist) {
		try {
			Map<String,Object> level1 = (Map<String,Object>) jacksonObjectMapper.readValue(
					new URL("http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&artist=" 
					+ artist.getName() + "&api_key=" + apiKey + "&format=json"), Map.class).get("artist");

			if (level1 == null) {
				return null;
			}
			
			ArrayList<Map<String,Object>> level2 = (ArrayList<Map<String,Object>>) level1.get("image");
			Map<String,Object> level3 = level2.get(4);
			
			return (String) level3.get("#text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String getMainAlbumPicUrl(Album album) {
		try {
			Map<String,Object> level1 = (Map<String,Object>) jacksonObjectMapper.readValue(
					new URL("http://ws.audioscrobbler.com/2.0/?method=album.getInfo&album=" 
					+ album.getTitle() + "&artist=" + album.getArtists().iterator().next() + 
					"&api_key=" + apiKey + "&format=json"), Map.class).get("album");
			
			if (level1 == null) {
				return null;
			}
			
			ArrayList<Map<String,Object>> level2 = (ArrayList<Map<String,Object>>) level1.get("image");
			Map<String,Object> level3 = level2.get(4);
			
			return (String) level3.get("#text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}