package com.example.demo.dto.structureXML.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement(localName = "log")
public class Log {

    @JacksonXmlProperty(localName = "timestamp")
    private Long timestamp;

    @JacksonXmlProperty(localName = "user_id")
    private int user_id;

    @JacksonXmlProperty(localName = "url")
    private String url;

    @JacksonXmlProperty(localName = "seconds")
    private Long seconds;
}
