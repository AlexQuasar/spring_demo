package com.example.demo.dto.xmlStructure.input;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JacksonXmlRootElement(localName = "log")
public class Log {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "logEntry")
    private List<LogEntry> logEntries;
}
