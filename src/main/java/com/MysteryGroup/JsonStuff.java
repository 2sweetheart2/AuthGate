package com.MysteryGroup;

import com.MysteryGroup.Objects.User;
import com.google.gson.*;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsonStuff {

    private File jsonFile;

    public JsonStuff(File jsonFile) {
        this.jsonFile = jsonFile;
    }

    public void save(List<User> users) {
        Gson gson = new Gson();
        String json = gson.toJson(users);
        try {
            OutputStream os = new FileOutputStream(jsonFile);
            os.flush();
            os.write(json.getBytes());
        } catch (Exception ignored) {
        }
    }

    public List<User> read() {
        List<User> users = new ArrayList<>();
        try {
            JsonParser jsonParser = new JsonParser();
            Object parsed = jsonParser.parse(new FileReader(jsonFile.getPath()));
            JsonArray jsonObject = (JsonArray) parsed;
            for (JsonElement jsonElement : jsonObject) {
                JsonObject json = jsonElement.getAsJsonObject();
                UUID uuid = UUID.fromString(json.get("uuid").getAsString());
                String name = json.get("displayName").getAsString();
                String password = json.get("password").getAsString();
                users.add(new User(uuid, name, password));
                Bukkit.getLogger().info(name + " " + password);
            }
            return users;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return users;
        }
    }
}
