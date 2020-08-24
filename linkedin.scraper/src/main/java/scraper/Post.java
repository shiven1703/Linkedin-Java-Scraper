package scraper;

import java.util.ArrayList;

public class Post {

	private String author;
	private String postContent;
	public ArrayList<Comment> comments;
	
	public Post(){
		this.author = "";
		this.postContent = "";
		this.comments = new ArrayList<Comment>();
	}
	
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public ArrayList<Comment> getAllComments(){
		return this.comments;
	}
	
}
