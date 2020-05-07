package com.flewp.flewpbot.model.events.twitch.pubsub;

public class ChannelPointData {
    public Redemption redemption;

    public class Redemption {
        public String id;
        public RedemptionUser user;
        public String channel_id;
        public String redeemed_at;
        public Reward reward;
        public String user_input;
        public String status;

        public class Reward {
            public String id;
            public String channel_id;
            public String title;
            public String prompt;
            public Integer cost;
            public Boolean is_user_input_required;
            public Boolean is_sub_only;
            public String background_color;
            public Boolean is_enabled;
            public Boolean is_paused;
            public Boolean is_in_stock;
            public Boolean should_redemptions_skip_request_queue;
        }

        public class RedemptionUser {
            public String id;
            public String login;
            public String display_name;
        }
    }
}
