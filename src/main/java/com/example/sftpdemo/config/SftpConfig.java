package com.example.sftpdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix="sftp")
public class SftpConfig {
	
	private String host;
	private String username;
	private String password;
	private String location;
	private String localFileDownloadPath;

}
