package com.flewp.flewpbot.event;

import java.util.*;
import java.util.stream.Collectors;

public class EventUser {
    private String name;
    private String id;
    private Set<Permission> permissions;

    public EventUser(String name, String id, String badges) {
        this.name = name;
        this.id = id;
        permissions = Permission.parsePermissions(badges);
    }

    public EventUser(Map<String, String> tags) {
        this.name = tags.get("display-name").toLowerCase();
        this.id = tags.get("user-id").toLowerCase();
        this.permissions = Permission.parsePermissions(tags.get("badges"));
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public enum Permission {
        Broadcaster("broadcaster"),
        Moderator("moderator"),
        Subscriber("subscriber"),
        Partner("partner");

        private String label;

        Permission(String label) {
            this.label = label;
        }

        static Set<Permission> parsePermissions(String badges) {
            Set<Permission> permissions = new HashSet<>();
            if (badges == null || badges.isEmpty()) {
                return permissions;
            }

            return Arrays.stream(badges.split(","))
                    .map(str -> str.contains("/") ? str.substring(0, str.indexOf("/")) : str)
                    .map(str -> Arrays.stream(values()).filter(permission ->
                            permission.label.equals(str)).findFirst().orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EventUser)) {
            return false;
        }

        return this.getId().equals(((EventUser) obj).getId());
    }

    @Override
    public String toString() {
        return "EventUser [" + name + " " + id + "]";
    }
}