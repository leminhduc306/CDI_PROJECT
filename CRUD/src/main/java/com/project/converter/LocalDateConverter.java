package com.project.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

@FacesConverter(value = "localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final List<DateTimeFormatter> INPUT_FORMATTER = Arrays.asList(
    		DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
    		);
    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        for (DateTimeFormatter formatter : INPUT_FORMATTER) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException e) {
            }
        }      
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Please input a valid date in one of these formats: dd/MM/yyyy, yyyy-MM-dd, yyyy/MM/dd, dd-MM-yyyy",
                null));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        if (value == null) {
            return "";
        }
        return value.format(FORMATTER);
    }
}
