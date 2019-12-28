package com.flewp.flewpbot.model.jamisphere;

public class JamisphereRequest implements Comparable<JamisphereRequest> {
    public String requestId;

    public Boolean inList;
    public Boolean playing;
    public Integer status; // 1 regular, 2 vip
    public Integer likes;

    public String youtubeVideoId;
    public String youtubeSongTitle;
    public Long youtubeSongLikes;
    public Long youtubeSongDislikes;
    public Long youtubeSongViews;
    public String youtubeThumbnail;

    public String twitchUserName;
    public String twitchUserId;
    public String twitchProfilePicture;

    public String timePlayed; // ISO String of time played
    public Long streamUptimePlayed; // Time in milliseconds that the stream has been live when played

    @Override
    public int compareTo(JamisphereRequest o) {
        if (status != null && o.status != null && !status.equals(o.status)) {
            return o.status.compareTo(status);
        }

        return requestId.compareTo(o.requestId);
    }
}
