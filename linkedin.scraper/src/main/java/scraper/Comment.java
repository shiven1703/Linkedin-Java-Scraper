package scraper;

public class Comment {

	private String author;
	private String profileLink;
	private String commentText;
	public String email;
		
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getProfileLink() {
		return profileLink;
	}
	public void setProfileLink(String profileLink) {
		this.profileLink = profileLink;
	}
	public String getCommentText() {
		return commentText;
	}
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	public void setEmail(String email) {
		this.email= email;
	}
	
	public String getEmail() {
		return this.email;
	}

}
