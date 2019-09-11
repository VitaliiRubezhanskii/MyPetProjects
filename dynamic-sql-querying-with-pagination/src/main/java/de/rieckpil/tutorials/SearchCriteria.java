package de.rieckpil.tutorials;

import lombok.Data;

@Data
public class SearchCriteria {

    private String fieldName;
    private String fieldValue;
    private String operator;

}
