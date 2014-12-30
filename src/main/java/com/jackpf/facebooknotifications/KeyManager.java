package com.jackpf.facebooknotifications;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyManager
{
    private Map<String, String> keys = new HashMap<String, String>();

    public void loadKeys() throws KeyManagerException
    {
        String text;

        try {
            text = IOUtils.toString(
                this.getClass().getResourceAsStream("/api_keys"),
                "UTF-8"
            );
        } catch (IOException e) {
            throw new KeyManagerException("Unable to read API keys", e);
        }

        for (String line : text.split("\n")) {
            String[] parts = line.split(":");

            keys.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
    }

    public String getKey(String type)
    {
        return keys.get(type);
    }

    public class KeyManagerException extends Exception
    {
        public KeyManagerException(String message)
        {
            super(message);
        }

        public KeyManagerException(String message, Exception previous)
        {
            super(message, previous);
        }
    }
}
