package com.mycomp.image.storage.service.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageServiceProperties {
	@Value("${minio.access.key}")
	private String minioAccessKey;
	@Value("${minio.secret.key}")
	private String miniosecretKey;
	@Value("${minio.server}")
	private String minioServer;
	@Value("${kafka.broker}")
	private String kafkaBroker;

	public String getMinioAccessKey() {
		return minioAccessKey;
	}

	public void setMinioAccessKey(String minioAccessKey) {
		this.minioAccessKey = minioAccessKey;
	}

	public String getMiniosecretKey() {
		return miniosecretKey;
	}

	public void setMiniosecretKey(String miniosecretKey) {
		this.miniosecretKey = miniosecretKey;
	}

	public String getMinioServer() {
		return minioServer;
	}

	public void setMinioServer(String minioServer) {
		this.minioServer = minioServer;
	}

	public String getKafkaBroker() {
		return kafkaBroker;
	}

	public void setKafkaBroker(String kafkaBroker) {
		this.kafkaBroker = kafkaBroker;
	}

}
