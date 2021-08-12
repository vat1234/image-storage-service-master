package com.mycomp.image.storage.service.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mycomp.image.storage.service.AlbumService;
import com.mycomp.image.storage.service.AlbumServiceImpl;
import com.mycomp.image.storage.service.payload.Album;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)

@WebMvcTest(value = StorageServiceController.class)

public class StorageServiceControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@InjectMocks
	@Autowired
	private AlbumService albumService = new AlbumServiceImpl();

	@Ignore

	@Test
	public void testCreateAlbum() throws Exception {
		Album album = new Album("spring");
		String exampleAlbum = "{\"name\":\"Spring\"}";
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock a) throws Throwable {
				albumService.createAlbum("spring", album);

				return null;
			}
		}).when(albumService).createAlbum("spring", album);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/albums").contentType(MediaType.APPLICATION_JSON)
				.header("user", "spring").content(exampleAlbum);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	}

}
