package model;

import java.util.List;

abstract class Preference {
	
	private String id;
	private String access_token;
	private String affiliation;
	private String app_id;
	private String artists_we_like;	
	private String attire;
	private String awards;
	private String band_interests;
	private String band_members;
	private String bio;
	private String birthday;
	private String booking_agent;
	private String 	built;
	private String business;
	private boolean can_checkin;
	private boolean can_post;
	private String category;
	private List<Category> category_list;
	private String company_overview;;
	private String country_page_likes;
	private String culinary_team;
	private GeoLocal current_location;
	private String description;
	private String description_html;
	private String directed_by;
	private String 	display_subtext;
	
	private String emails;	
	private String features;
	private String food_styles;
	private String founded;
	private String general_info;
	private String general_manager;
	private String genre;
	private String global_brand_page_name;
	private String global_brand_root_id;
	private boolean has_added_app;
	private boolean leadgen_tos_accepted;
	
	private String hometown;
	private String impressum;
	private String influences;
	private boolean is_always_open;
	private boolean is_community_page;
	private boolean is_permanently_closed;
	private boolean is_published;
	private boolean is_unclaimed;
	private boolean is_verified;	
	private GeoLocal location;

	private String mission;
	private String name;
	private String name_with_location_descriptor;
	
	private String network;
	private String new_like_count; //The number of people who have liked the Page, since the last login. Only visible to a page admin
	private boolean offer_eligible; //Offer eligibility status. Only visible to a page admin
	private String personal_info; //	Personal information. Applicable to Pages representing People
	private String personal_interests; //Personal interests. Applicable to Pages representing People
	private String pharma_safety_info; //Pharmacy safety information. Applicable to Pharmaceutical companies
	private String phone; //Phone number provided by a Page
	private String plot_outline; //The plot outline of the film. Applicable to Films
	private String press_contact; //Press contact information of the band. Applicable to Bands
	private String price_range; //Price range of the business. Applicable to Restaurants or Nightlife. Can be one of $ (0-10), $$ (10-30), $$$ (30-50), $$$$ (50+) or Unspecified
	private String produced_by;   //The productor of the film. Applicable to Films
	private String products; //The products of this company. Applicable to Companies
	private boolean promotion_eligible;  //	Reason why a post isn't eligible for boosting. Only visible to Page Admins
	private String promotion_ineligible_reason; //Reason, for which boosted posts are not eligible. Only visible to a page admin
	private String public_transit; //Public transit to the business. Applicable to Restaurants or Nightlife
	private String record_label; //Record label of the band. Applicable to Bands
	private String release_date; //The film's release date. Applicable to Films
	
	private String 	schedule; //The air schedule of the TV show. Applicable to TV Shows
	private String screenplay_by; //The screenwriter of the film. Applicable to Films
	private String season; //The season information of the TV Show. Applicable to TV Shows
	private String starring; //	The cast of the film. Applicable to Films
	private String store_location_descriptor; // Location Page's store location descriptor
	private String store_number; //Unique store number for this location Page
	private String studio;
	private String single_line_address;
	private String place_type;
	private String username; //The alias of the Page. For example, for www.facebook.com/platform the username is 'platform'	
	private String website;
	private String written_by;

	private float asset_score;
	private String checkins; 
	private String likes;
    private String gmembers;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getArtists_we_like() {
		return artists_we_like;
	}
	public void setArtists_we_like(String artists_we_like) {
		this.artists_we_like = artists_we_like;
	}
	public String getAttire() {
		return attire;
	}
	public void setAttire(String attire) {
		this.attire = attire;
	}
	public String getAwards() {
		return awards;
	}
	public void setAwards(String awards) {
		this.awards = awards;
	}
	public String getBand_interests() {
		return band_interests;
	}
	public void setBand_interests(String band_interests) {
		this.band_interests = band_interests;
	}
	public String getBand_members() {
		return band_members;
	}
	public void setBand_members(String band_members) {
		this.band_members = band_members;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBooking_agent() {
		return booking_agent;
	}
	public void setBooking_agent(String booking_agent) {
		this.booking_agent = booking_agent;
	}
	public String getBuilt() {
		return built;
	}
	public void setBuilt(String built) {
		this.built = built;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public boolean isCan_checkin() {
		return can_checkin;
	}
	public void setCan_checkin(boolean can_checkin) {
		this.can_checkin = can_checkin;
	}
	public boolean isCan_post() {
		return can_post;
	}
	public void setCan_post(boolean can_post) {
		this.can_post = can_post;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Category> getCategory_list() {
		return category_list;
	}
	public void setCategory_list(List<Category> category_list) {
		this.category_list = category_list;
	}
	public String getCompany_overview() {
		return company_overview;
	}
	public void setCompany_overview(String company_overview) {
		this.company_overview = company_overview;
	}
	public String getCountry_page_likes() {
		return country_page_likes;
	}
	public void setCountry_page_likes(String country_page_likes) {
		this.country_page_likes = country_page_likes;
	}
	public String getCulinary_team() {
		return culinary_team;
	}
	public void setCulinary_team(String culinary_team) {
		this.culinary_team = culinary_team;
	}
	public GeoLocal getCurrent_location() {
		return current_location;
	}
	public void setCurrent_location(GeoLocal current_location) {
		this.current_location = current_location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription_html() {
		return description_html;
	}
	public void setDescription_html(String description_html) {
		this.description_html = description_html;
	}
	public String getDirected_by() {
		return directed_by;
	}
	public void setDirected_by(String directed_by) {
		this.directed_by = directed_by;
	}
	public String getDisplay_subtext() {
		return display_subtext;
	}
	public void setDisplay_subtext(String display_subtext) {
		this.display_subtext = display_subtext;
	}
	public String getEmails() {
		return emails;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}
	public String getFeatures() {
		return features;
	}
	public void setFeatures(String features) {
		this.features = features;
	}
	public String getFood_styles() {
		return food_styles;
	}
	public void setFood_styles(String food_styles) {
		this.food_styles = food_styles;
	}
	public String getFounded() {
		return founded;
	}
	public void setFounded(String founded) {
		this.founded = founded;
	}
	public String getGeneral_info() {
		return general_info;
	}
	public void setGeneral_info(String general_info) {
		this.general_info = general_info;
	}
	public String getGeneral_manager() {
		return general_manager;
	}
	public void setGeneral_manager(String general_manager) {
		this.general_manager = general_manager;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getGlobal_brand_page_name() {
		return global_brand_page_name;
	}
	public void setGlobal_brand_page_name(String global_brand_page_name) {
		this.global_brand_page_name = global_brand_page_name;
	}
	public String getGlobal_brand_root_id() {
		return global_brand_root_id;
	}
	public void setGlobal_brand_root_id(String global_brand_root_id) {
		this.global_brand_root_id = global_brand_root_id;
	}
	public boolean isHas_added_app() {
		return has_added_app;
	}
	public void setHas_added_app(boolean has_added_app) {
		this.has_added_app = has_added_app;
	}
	public boolean isLeadgen_tos_accepted() {
		return leadgen_tos_accepted;
	}
	public void setLeadgen_tos_accepted(boolean leadgen_tos_accepted) {
		this.leadgen_tos_accepted = leadgen_tos_accepted;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getImpressum() {
		return impressum;
	}
	public void setImpressum(String impressum) {
		this.impressum = impressum;
	}
	public String getInfluences() {
		return influences;
	}
	public void setInfluences(String influences) {
		this.influences = influences;
	}
	public boolean isIs_always_open() {
		return is_always_open;
	}
	public void setIs_always_open(boolean is_always_open) {
		this.is_always_open = is_always_open;
	}
	public boolean isIs_community_page() {
		return is_community_page;
	}
	public void setIs_community_page(boolean is_community_page) {
		this.is_community_page = is_community_page;
	}
	public boolean isIs_permanently_closed() {
		return is_permanently_closed;
	}
	public void setIs_permanently_closed(boolean is_permanently_closed) {
		this.is_permanently_closed = is_permanently_closed;
	}
	public boolean isIs_published() {
		return is_published;
	}
	public void setIs_published(boolean is_published) {
		this.is_published = is_published;
	}
	public boolean isIs_unclaimed() {
		return is_unclaimed;
	}
	public void setIs_unclaimed(boolean is_unclaimed) {
		this.is_unclaimed = is_unclaimed;
	}
	public boolean isIs_verified() {
		return is_verified;
	}
	public void setIs_verified(boolean is_verified) {
		this.is_verified = is_verified;
	}
	public GeoLocal getLocation() {
		return location;
	}
	public void setLocation(GeoLocal location) {
		this.location = location;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_with_location_descriptor() {
		return name_with_location_descriptor;
	}
	public void setName_with_location_descriptor(String name_with_location_descriptor) {
		this.name_with_location_descriptor = name_with_location_descriptor;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getNew_like_count() {
		return new_like_count;
	}
	public void setNew_like_count(String new_like_count) {
		this.new_like_count = new_like_count;
	}
	public boolean isOffer_eligible() {
		return offer_eligible;
	}
	public void setOffer_eligible(boolean offer_eligible) {
		this.offer_eligible = offer_eligible;
	}
	public String getPersonal_info() {
		return personal_info;
	}
	public void setPersonal_info(String personal_info) {
		this.personal_info = personal_info;
	}
	public String getPersonal_interests() {
		return personal_interests;
	}
	public void setPersonal_interests(String personal_interests) {
		this.personal_interests = personal_interests;
	}
	public String getPharma_safety_info() {
		return pharma_safety_info;
	}
	public void setPharma_safety_info(String pharma_safety_info) {
		this.pharma_safety_info = pharma_safety_info;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPlot_outline() {
		return plot_outline;
	}
	public void setPlot_outline(String plot_outline) {
		this.plot_outline = plot_outline;
	}
	public String getPress_contact() {
		return press_contact;
	}
	public void setPress_contact(String press_contact) {
		this.press_contact = press_contact;
	}
	public String getPrice_range() {
		return price_range;
	}
	public void setPrice_range(String price_range) {
		this.price_range = price_range;
	}
	public String getProduced_by() {
		return produced_by;
	}
	public void setProduced_by(String produced_by) {
		this.produced_by = produced_by;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
	public boolean isPromotion_eligible() {
		return promotion_eligible;
	}
	public void setPromotion_eligible(boolean promotion_eligible) {
		this.promotion_eligible = promotion_eligible;
	}
	public String getPromotion_ineligible_reason() {
		return promotion_ineligible_reason;
	}
	public void setPromotion_ineligible_reason(String promotion_ineligible_reason) {
		this.promotion_ineligible_reason = promotion_ineligible_reason;
	}
	public String getPublic_transit() {
		return public_transit;
	}
	public void setPublic_transit(String public_transit) {
		this.public_transit = public_transit;
	}
	public String getRecord_label() {
		return record_label;
	}
	public void setRecord_label(String record_label) {
		this.record_label = record_label;
	}
	public String getRelease_date() {
		return release_date;
	}
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getScreenplay_by() {
		return screenplay_by;
	}
	public void setScreenplay_by(String screenplay_by) {
		this.screenplay_by = screenplay_by;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getStarring() {
		return starring;
	}
	public void setStarring(String starring) {
		this.starring = starring;
	}
	public String getStore_location_descriptor() {
		return store_location_descriptor;
	}
	public void setStore_location_descriptor(String store_location_descriptor) {
		this.store_location_descriptor = store_location_descriptor;
	}
	public String getStore_number() {
		return store_number;
	}
	public void setStore_number(String store_number) {
		this.store_number = store_number;
	}
	public String getStudio() {
		return studio;
	}
	public void setStudio(String studio) {
		this.studio = studio;
	}
	public String getSingle_line_address() {
		return single_line_address;
	}
	public void setSingle_line_address(String single_line_address) {
		this.single_line_address = single_line_address;
	}
	public String getPlace_type() {
		return place_type;
	}
	public void setPlace_type(String place_type) {
		this.place_type = place_type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getWritten_by() {
		return written_by;
	}
	public void setWritten_by(String written_by) {
		this.written_by = written_by;
	}
	public float getAsset_score() {
		return asset_score;
	}
	public void setAsset_score(float asset_score) {
		this.asset_score = asset_score;
	}
	public String getCheckins() {
		return checkins;
	}
	public void setCheckins(String checkins) {
		this.checkins = checkins;
	}
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	public String getGmembers() {
		return gmembers;
	}
	public void setGmembers(String gmembers) {
		this.gmembers = gmembers;
	}
	
}
