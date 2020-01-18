package com.flewp.flewpbot.model.jamisphere;

import java.util.List;

public class JamisphereVideo {
    public String id;

    public Snippet snippet;
    public ContentDetails contentDetails;
    public Statistics statistics;

    @Override
    public String toString() {
        return "JamisphereVideo{" +
                "snippet=" + snippet +
                '}';
    }

    public static class Snippet {
        @Override
        public String toString() {
            return "Snippet{" +
                    "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

        public String publishedAt;
        public String channelId;
        public String title;
        public String description;
        public String channelTitle;
        public List<String> tags;
        public String categoryId;
        public String liveBroadcastContent;
        public String defaultLanguage;
        public String defaultAudioLanguage;
    }

    public static class ContentDetails {
        public String duration;
        public String dimension;
        public String definition;
        public String caption;
        public boolean licensedContent;
        public String projection;
    }

    public static class Statistics {
        public String viewCount;
        public String likeCount;
        public String dislikeCount;
        public String favoriteCount;
        public String commentCount;
    }
}
