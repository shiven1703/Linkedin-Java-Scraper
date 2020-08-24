package scraper;

import java.util.ArrayList;

public class Post {

	public String author;
	public String postContent;
	public ArrayList<String> comments;
	
	public Post(){
		this.author = "";
		this.postContent = "";
		this.comments = new ArrayList<String>();
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
	
	
	public void addComment(String comment) {
		this.comments.add(comment);
	}
	
	public ArrayList<String> getAllComments(){
		return this.comments;
	}
	
	
}
