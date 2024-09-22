package org.nseu.practice.core.kits;

import java.util.HashMap;
import java.util.UUID;

public class KitStorage {

    private static HashMap<UUID, KitConfig> kitConfigs = new HashMap<>();



    public static void LoadKitConfigs(HashMap<String, HashMap<String, HashMap<String, String>>> kits) {
        kits.forEach((k, v) -> {
            KitConfig kitConfig = new KitConfig(UUID.fromString(k));
            kitConfig.deserialize(v);
            kitConfigs.put(UUID.fromString(k), kitConfig);
        });
    }

    public static HashMap<String, HashMap<String, HashMap<String, String>>> GetKitConfigs() {
        HashMap<String, HashMap<String, HashMap<String, String>>> data = new HashMap<>();
        kitConfigs.forEach((k, v) -> {
            data.put(k.toString(), v.serialize());
        });
        return data;
    }
}
