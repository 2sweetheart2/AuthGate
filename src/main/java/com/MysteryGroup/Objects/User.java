package com.MysteryGroup.Objects;

import java.util.UUID;

public class User {
    public UUID uuid;
    public String displayName;
    public String password;

    public User(UUID uuid, String displayName, String password) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.password = password;
    }

    public User(String uuid, String displayName, String password) {
        this.uuid = UUID.fromString(uuid);
        this.displayName = displayName;
        this.password = password;
    }
}
