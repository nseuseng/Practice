package org.nseu.practice.storage;

import org.nseu.practice.arena.Arena;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class FileManager {


    public static void save() {
        CreateFile(PATH);
        DumpYaml(Arena.data(), "Arenas");
    }

    public static void load() {
        CreateFile(PATH);
        HashMap<String, HashMap<String, String>> arenatempfile = new HashMap<>();
        arenatempfile = (HashMap<String, HashMap<String, String>>) LoadYaml("Arenas");
        if(arenatempfile != null) {
            Arena.load(arenatempfile);
        }
    }

    private static String PATH = "plugins" + System.getProperty("file.separator") + "PracticeCore";

    private static void CreateFile(String FileName) {
        try {
            File file = new File(FileName);
            if(!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object LoadYaml(String filename) {
        try {
            Yaml yaml = new Yaml();
            FileReader reader = new FileReader(PATH + System.getProperty("file.separator") + filename + ".yml");
            return yaml.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ERROR] Cant Load Data");
        return null;
    }

    private static void DumpYaml(Object data, String filename) {
        try {
            LoaderOptions options = new LoaderOptions();
            Yaml yaml = new Yaml(new Constructor(data.getClass(), options));
            FileWriter writer;
            writer = new FileWriter( PATH + System.getProperty("file.separator") + filename + ".yml");
            yaml.dump(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
