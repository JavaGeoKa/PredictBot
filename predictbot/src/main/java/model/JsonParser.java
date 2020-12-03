package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	public static String jsonParse(String string) throws JsonMappingException, JsonProcessingException {

		System.out.println(string.length());

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ImgurResponseDto dto = mapper.readValue(string, ImgurResponseDto.class);
		return dto.getData().getLink();

	}

}
