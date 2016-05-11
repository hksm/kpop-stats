package app.model;

import java.beans.PropertyEditorSupport;


public class CategoryEditor extends PropertyEditorSupport {

	@Override
    public void setAsText(String text) throws IllegalArgumentException {

        String capitalized = text.toUpperCase();
        Category category = Category.valueOf(capitalized);
        setValue(category);
    }
}
