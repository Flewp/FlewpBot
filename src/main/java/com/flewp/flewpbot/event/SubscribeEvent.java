package com.flewp.flewpbot.event;

import com.github.philippheuer.events4j.domain.Event;

import java.util.Arrays;
import java.util.Map;

public class SubscribeEvent extends Event {
    private Type type;
    private EventUser eventUser;
    private EventUser recipientUser;
    private String message;
    private int months;
    private String subPlan;

    public static SubscribeEvent parse(Map<String, String> tags, String message) {
        if (!isSubscribeEvent(tags)) {
            return null;
        }

        return new SubscribeEvent(tags, message);
    }

    private SubscribeEvent(Map<String, String> tags, String message) {
        this.message = message;

        type = Type.stringToType(tags.get("msg-id"));
        eventUser = new EventUser(tags);
        subPlan = tags.getOrDefault("msg-param-sub-plan", "");

        switch (type.category) {
            case Upgrade:
                months = 1;
                recipientUser = null;
                break;
            case Gift:
                months = Integer.parseInt(tags.get("msg-param-months"));
                recipientUser = new EventUser(tags.get("msg-param-recipient-user-name"),
                        tags.get("msg-param-recipient-id"), "");
                break;
            case Solo:
                months = Integer.parseInt(tags.get("msg-param-cumulative-months"));
                recipientUser = null;
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public EventUser getRecipientUser() {
        return recipientUser;
    }

    public String getMessage() {
        return message;
    }

    public int getMonths() {
        return months;
    }

    public String getSubPlan() {
        return subPlan;
    }

    @Override
    public String toString() {
        return "SubscribeEvent [ type: " + type.label + ", user: " + eventUser.toString() + ", recipient: " +
                (recipientUser == null ? "null" : recipientUser.toString()) + ", months: " + months + ", message: "
                + message + " ]";
    }

    private static boolean isSubscribeEvent(Map<String, String> tags) {
        return tags.containsKey("msg-id") && Type.stringToType(tags.get("msg-id")) != null;
    }

    public enum Type {
        Sub("sub", false, Category.Solo),
        ReSub("resub", false, Category.Solo),
        SubGift("subgift", true, Category.Gift),
        AnonSubGift("anonsubgift", true, Category.Gift),
        GiftPaidUpgrade("giftpaidupgrade", false, Category.Upgrade),
        AnonGiftPaidUpgrade("anongiftpaidupgrade", false, Category.Upgrade);

        private String label;
        private boolean gift;
        Category category;

        Type(String label, boolean gift, Category category) {
            this.label = label;
            this.gift = gift;
            this.category = category;
        }

        public boolean isGift() {
            return gift;
        }

        static Type stringToType(String label) {
            return Arrays.stream(values()).filter(type -> label.equals(type.label)).findFirst().orElse(null);
        }

        enum Category {
            Solo,
            Gift,
            Upgrade
        }
    }
}
