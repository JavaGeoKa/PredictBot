package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ParserController {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {

		String j = "{\"data\":{\"id\":\"d2CeKkT\",\"title\":null,\"description\":null,\"datetime\":1606549773,\"type\":\"image\\/jpeg\",\"animated\":false,\"width\":489,\"height\":484,\"size\":61778,\"views\":0,\"bandwidth\":0,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null,\"account_id\":0,\"is_ad\":false,\"in_most_viral\":false,\"has_sound\":false,\"tags\":[],\"ad_type\":0,\"ad_url\":\"\",\"edited\":\"0\",\"in_gallery\":false,\"deletehash\":\"9WnTyUA4Tmwx1w6\",\"name\":\"\",\"link\":\"https:\\/\\/i.imgur.com\\/d2CeKkT.jpg\"},\"success\":true,\"status\":200}";
//		String json = "{\"id\":1,\"theName\":\"My bean\"}";

		System.out.println(j);

		System.out.println(j.length());

		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ImgurResponseDto dto = mapper.readValue(j, ImgurResponseDto.class);

		System.out.println(dto.getData().getLink() + " -> " + dto.getStatus());

//		assertThat(result, containsString("My bean"));
//	    assertThat(result, containsString("1"));

	}

}