package imagga;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@ToString
public class TagDto {

	double confidence;
	Map<String, String> tag;
}
