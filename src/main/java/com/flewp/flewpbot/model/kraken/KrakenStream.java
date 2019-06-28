package com.flewp.flewpbot.model.kraken;


public class KrakenStream {
    public String _id;
    public String game;
    public Integer viewers;
    public Integer video_height;
    public Integer average_fps;
    public Integer delay;
    public String created_at;
    public Boolean is_playlist;
    public Preview preview;
    public KrakenChannel channel;

    public static class Preview {
        public String small;
        public String medium;
        public String large;
        public String template;
    }
}
