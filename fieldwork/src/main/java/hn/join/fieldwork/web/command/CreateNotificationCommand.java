package hn.join.fieldwork.web.command;

public class CreateNotificationCommand {
	
	private String title;
	
	private String content;
	
	private String city;
	
	private String publishDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CreateNotificationCommand [title=").append(title)
				.append(", content=").append(content).append(", publishDate=")
				.append(publishDate).append("]");
		return builder.toString();
	}
	
	

}
