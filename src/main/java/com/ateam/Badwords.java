package com.ateam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Badwords {
    static final String BADWORDS_FILEPATH = "./db/miccionario.txt";
    private HashSet<String> badwordsList;
    private static final Logger LOGGER = Logger.getLogger("Badwords");



    public Badwords(){
        badwordsList = new HashSet<>();
    }

    public HashSet<String> getBadwordsList() {
        return badwordsList;
    }

    public boolean loadBw(){
        File txtFile = new File(Badwords.BADWORDS_FILEPATH);

        if (!txtFile.exists()) {
            LOGGER.warning("[Badwords]\tThe badwords list file could not be found.");
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(txtFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                badwordsList.add(line.toLowerCase());
            }
        }catch(FileNotFoundException e){
            LOGGER.log(Level.SEVERE, "[Badwords]\tCannot load the list of badwords.", e);
        }catch(IOException ex){
            LOGGER.log(Level.SEVERE, "[Badwords]\tCannot read the list of badwords.", ex);
        }

        return true;
    }
}
