package com.flewp.flewpbot.model.events.twitch.pubsub;

public class RedemptionData {
    public String timestamp;
    public Redemption redemption;

    @Override
    public String toString() {
        return "RedemptionData{" +
                "timestamp='" + timestamp + '\'' +
                ", redemption=" + redemption +
                '}';
    }

    public class Redemption {
        public String id;
        public RedemptionUser user;
        public String channel_id;
        public String redeemed_at;
        public Reward reward;
        public String user_input;
        public String status;

        @Override
        public String toString() {
            return "Redemption{" +
                    "id='" + id + '\'' +
                    ", user=" + user +
                    ", channel_id='" + channel_id + '\'' +
                    ", redeemed_at='" + redeemed_at + '\'' +
                    ", reward=" + reward +
                    ", user_input='" + user_input + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }

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

            @Override
            public String toString() {
                return "Reward{" +
                        "id='" + id + '\'' +
                        ", channel_id='" + channel_id + '\'' +
                        ", title='" + title + '\'' +
                        ", prompt='" + prompt + '\'' +
                        ", cost=" + cost +
                        ", is_user_input_required=" + is_user_input_required +
                        ", is_sub_only=" + is_sub_only +
                        ", background_color='" + background_color + '\'' +
                        ", is_enabled=" + is_enabled +
                        ", is_paused=" + is_paused +
                        ", is_in_stock=" + is_in_stock +
                        ", should_redemptions_skip_request_queue=" + should_redemptions_skip_request_queue +
                        '}';
            }
        }

        public class RedemptionUser {
            public String id;
            public String login;
            public String display_name;

            @Override
            public String toString() {
                return "RedemptionUser{" +
                        "id='" + id + '\'' +
                        ", login='" + login + '\'' +
                        ", display_name='" + display_name + '\'' +
                        '}';
            }
        }
    }
}
