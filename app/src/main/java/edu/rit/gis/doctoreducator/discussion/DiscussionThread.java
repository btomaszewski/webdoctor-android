package edu.rit.gis.doctoreducator.discussion;

/**
 * Created by siddeshpillai on 10/27/15.
 */
public class DiscussionThread {

    public String _id, id, author, title, body, time;

    DiscussionThread(String _id, String id, String author, String title, String body, String time) {
        this._id = _id;
        this.author = author;
        this.body = body;
        this.id = id;
        this.time = time;
        this.title = title;
    }

}
