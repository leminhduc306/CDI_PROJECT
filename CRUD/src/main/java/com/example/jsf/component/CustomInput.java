package com.example.jsf.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputText;

@FacesComponent(value = "customInput")
public class CustomInput extends HtmlInputText {

    @Override
    public void setSubmittedValue(Object submittedValue) {
        if (submittedValue != null && submittedValue instanceof String) {
            String value = (String) submittedValue;
            if (!value.isEmpty()) {
                submittedValue = value + "@gmail.com";
//                submittedValue = value.toUpperCase();
            }
        }
        super.setSubmittedValue(submittedValue);
    }

    @Override
    public String getFamily() {
        return "javax.faces.Input";
    }
}