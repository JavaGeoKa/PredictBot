package imagga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImgPredictor {

	public static String predictByLink(String linkUrl) throws IOException {

		String credentialsToEncode = "acc_78f099f9ff97212" + ":" + "aa64b713801196eda9144ef95d560cd3";
		String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

		String endpoint_url = "https://api.imagga.com/v2/tags";
		String image_url = linkUrl;

		String url = endpoint_url + "?image_url=" + image_url + "&language=ru";
		URL urlObject = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

		connection.setRequestProperty("Authorization", "Basic " + basicAuth);

		int responseCode = connection.getResponseCode();

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		String jsonResponse = connectionInput.readLine();

		connectionInput.close();

		System.out.println(jsonResponse);

		// =======get tags
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ObjectDataImaggaDto dto = mapper.readValue(jsonResponse, ObjectDataImaggaDto.class);

		List<TagDto> tags = dto.getResult().getTags();

		List predictTags = tags.stream().map(t -> t.getTag().values()).limit(10).collect(Collectors.toList());

		StringBuilder r = new StringBuilder();
		predictTags.forEach(p -> r.append(p + ", "));
		System.out.println(r);

		return r.toString();

	}

}
