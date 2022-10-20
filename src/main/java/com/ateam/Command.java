package com.ateam;

import java.util.Arrays;

public enum Command {
    JOIN ("/join"),
    LIST("/list"),
    LEAVE("/leave"),
    NOOP("");

    private String value;

    Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    // parse command
    public static Command parseCommand(String str) {
        //  [/join, sala2]
        for (Command cmd : values()) {
            var strLower = Arrays.asList(str.toLowerCase().split(" "));

            if (strLower.size() > 0) {
                if (strLower.get(0).equals(cmd.getValue())) {
                    return cmd;
                }
            }
        }

        return NOOP;
    }
}
