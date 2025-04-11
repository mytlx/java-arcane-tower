package com.mytlx.arcane.snippets.nullObjectPattern;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-18 22:34
 */
public class MyParser {

    public static class Action {

        private final String actionName;

        public Action(String actionName) {
            this.actionName = actionName;
        }

        public void execute() {
            System.out.println("Executing action..." + actionName);
        }
    }

    private static final Action NO_ACTION = new Action("NO_ACTION") {
        @Override
        public void execute() {
            System.out.println("do nothing");
        }
    };

    public Action findAction(String args) {
        if ("add".equals(args)) {
            return new Action("add");
        } else if ("delete".equals(args)) {
            return new Action("delete");
        } else {
            return NO_ACTION;
        }
    }


}
