package com.flewp.flewpbot.model.jamisphere;

public class JamisphereRequest {
    public Boolean inList;
    public Integer status; // 1 regular, 2 vip
    public Integer likes;

    public String youtubeVideoId;
    public String youtubeSongTitle;
    public Long youtubeSongLikes;
    public Long youtubeSongDislikes;
    public Long youtubeSongViews;

    public String twitchUserName;
    public String twitchUserId;

    public String timePlayed; // ISO String of time played
    public Long streamUptimePlayed; // Time in milliseconds that the stream has been live when played
}
