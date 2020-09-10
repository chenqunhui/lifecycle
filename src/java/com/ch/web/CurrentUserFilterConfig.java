package com.ch.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "currentuserfilter")
public class CurrentUserFilterConfig {

    private List<String> ignorePaths = new LinkedList();
}
