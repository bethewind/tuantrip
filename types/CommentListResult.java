package com.tudaidai.tuantrip.types;

public class CommentListResult implements TuanTripType {
	public HttpResult mHttpResult = new HttpResult();
	Group<Comment> comments;
	int total;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Group<Comment> getComments() {
		return comments;
	}

	public void setComments(Group<Comment> comments) {
		this.comments = comments;
	}
}
