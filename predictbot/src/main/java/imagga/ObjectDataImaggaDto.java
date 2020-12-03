package imagga;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class ObjectDataImaggaDto {

	ResultDto result;

}

@Getter
@ToString
class ResultDto {
	List<TagDto> tags;
}

//@Getter
//@ToString
//class TagDto {
//	double confidence;
//	Map<String, String> tag;

//}