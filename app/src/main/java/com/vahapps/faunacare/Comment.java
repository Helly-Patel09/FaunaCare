package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 25-Feb-18.
 */

public class Comment {
    private String comment;
    private String faunaId;
    private String userId;

    public Comment(String commentId, String faunaId, String userId) {
        this.comment = commentId;
        this.faunaId = faunaId;
        this.userId = userId;
    }
    public String getComment() {
        return comment;
    }
    public String getFaunaId() {
        return faunaId;
    }
    public String getUserId() {
        return userId;
    }
}
