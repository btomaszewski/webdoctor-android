package edu.rit.gis.doctoreducator.discussion;

import java.util.Date;

/**
 * Model of a Comment in a DiscussionThread.
 */
public class Comment {
    String owner;
    Discussion discussion;
    String content;
    Date created;

    public Comment(String owner, Discussion discussion, String content, Date created) {
        this.owner = owner;
        this.discussion = discussion;
        this.content = content;
        this.created = created;
    }

    public String getOwner() {
        return owner;
    }

    public Discussion getDiscussion() {
        return discussion;
    }

    public String getContent() {
        return content;
    }

    public Date getCreated() {
        return created;
    }
}
