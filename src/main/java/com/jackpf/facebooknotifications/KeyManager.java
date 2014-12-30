package com.jackpf.facebooknotifications;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyManager
{
    private Map<String, String> keys = new HashMap<String, String>();

    public void loadKeys() throws IOException
    {
        String text = IOUtils.toString(
            this.getClass().getResourceAsStream("/api_keys"),
            "UTF-8"
        );

        for (String line : text.split("\n")) {
            String[] parts = line.split(":");

            keys.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
    }

    public String getKey(String type)
    {
        return keys.get(type);
    }
}
