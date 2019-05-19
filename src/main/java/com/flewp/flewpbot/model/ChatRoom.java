package com.flewp.flewpbot.model;

public class ChatRoom {
    private String minimum_allowed_role;

    private String is_previewable;

    private String owner_id;

    private String name;

    private String topic;

    private String _id;

    public String getMinimum_allowed_role() {
        return minimum_allowed_role;
    }

    public void setMinimum_allowed_role(String minimum_allowed_role) {
        this.minimum_allowed_role = minimum_allowed_role;
    }

    public String getIs_previewable() {
        return is_previewable;
    }

    public void setIs_previewable(String is_previewable) {
        this.is_previewable = is_previewable;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "ChatRoom [minimum_allowed_role = " + minimum_allowed_role + ", is_previewable = " + is_previewable + ", owner_id = " + owner_id + ", name = " + name + ", topic = " + topic + ", _id = " + _id + "]";
    }
}
