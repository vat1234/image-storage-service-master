package com.mycomp.image.storage.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class ServiceTest {
	@Bean
	@Primary
	public AlbumService albumService() {
		return Mockito.mock(AlbumService.class);
	}

}
