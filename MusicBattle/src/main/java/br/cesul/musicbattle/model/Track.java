package br.cesul.musicbattle.model;

import org.bson.types.ObjectId;

public class Track {
    private ObjectId id;
    private String title;
    private String artist;
    private int votes;

    public Track(){}

    public Track(Builder builder){
        this.title = builder.title;
        this.artist = builder.artist;
        this.votes = builder.votes;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }

    public static class Builder{
        private String title;
        private String artist;
        private int votes;
        public Builder titleBuilder(String title){
            this.title = title;
            return this;
        }
        public Builder artistBuilder(String artist){
            this.artist = artist;
            return this;
        }
        public Builder votesBuilder(int votes){
            this.votes = votes;
            return this;
        }
        public Track build(){
            return new Track(this);
        }
    }
}
