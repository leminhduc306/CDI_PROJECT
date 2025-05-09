package com.project.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "nameConverter")
public class NameConverter implements Converter<Object> {

    private String formatName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String[] words = input.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(word.substring(0, 1).toUpperCase())
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return formatName(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return formatName(value.toString());
    }
}
