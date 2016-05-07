package app.model;

public enum Category {
	ARTIST("artist"),
	ALBUM("album");
	
	private String lowercase;
	
	private Category(String lowercase) {
		this.lowercase = lowercase;
	}
	
	public String getLowercase() {
		return this.lowercase;
	}
}