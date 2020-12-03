package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectDataDto {

//	String id;
//	String title;
//	String description;
////	LocalDateTime datetime;
//	String type;
//	boolean animated;
//	Integer width;
//	Integer height;
//	Integer size;
//	Integer views;
//	Integer bandwidth;
//	String vote;
//	boolean favorite;
//	String nsfw;
//	String section;
//	String account_url;
//	Integer account_id;
//	boolean is_ad;
//	boolean in_most_viral;
//	boolean has_sound;
//	List<String> tags;
//	Integer ad_type;
//	String ad_url;
//	String edited;
//	boolean in_gallery;
//	String deletehash;
//	String name;
	String link;

}
