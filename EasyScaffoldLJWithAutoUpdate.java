
boolean start, allow,fbclicked,migi,nearfb,bang = true,LL;
        int ticks,slot,clicktick,tickss,bindz;

String[] modez = new String[]{"Vanilla","Flat","High","SuperFlat"};
String[] modes = new String[]{"None", "Toggle", "Auto"};


boolean allowedToUse = false;
String aes = "";

String[] colors = {
        "0","1", "2", "3", "4", "5", "6", "7", "8", "9","a","b","c","d","e","f"
};

String[] keyNames = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
        "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "BACK", "CAPITAL", "COMMA", "DELETE", "DOWN", "END", "ESCAPE", "F1", "F2", "F3", "F4", "F5",
        "F6", "F7", "HOME", "INSERT", "LBRACKET", "LCONTROL", "LMENU", "LMETA", "LSHIFT", "MINUS",
        "NUMPAD0", "NUMPAD1", "NUMPAD2", "NUMPAD3", "NUMPAD4", "NUMPAD5", "NUMPAD6", "NUMPAD7",
        "NUMPAD8", "NUMPAD9", "PERIOD", "RETURN", "RCONTROL", "RSHIFT", "RBRACKET", "SEMICOLON",
        "SLASH", "SPACE", "TAB", "GRAVE"
};





void onLoad() {


    modules.registerDescription("ScaffoldLJ");
    modules.registerSlider("Scaffold",1,modes);
    modules.registerDescription(util.color("&7- &9NONE&7: &fLJ only"));
    modules.registerDescription(util.color("&7- &9TOGGLE&7: &fLJ + enable Scaf"));
    modules.registerDescription(util.color("&7- &9AUTO&7: &fLJ + enable/disable Scaf"));
    modules.registerSlider("Mode",1,modez);
    modules.registerDescription(util.color("&7- &9VANILLA"));
    modules.registerDescription(util.color("&7- &9FLAT"));
    modules.registerDescription(util.color("&7- &9HIGH"));
    modules.registerDescription(util.color("&7- &9SUPERFLAT"));
    modules.registerButton("Mode Switcher",true);
    modules.registerSlider("Switcher keybind", "", 20, keyNames);

    modules.registerDescription("LongJump");
    modules.registerButton("Strafe",true);
    modules.registerButton("Higher precision",false);
    modules.registerDescription(util.color("&7(&4self-responsibility&7)"));
    modules.registerDescription(util.color("&7- &fLJ when only fb is nearby"));

    modules.registerDescription("Script");
    modules.registerSlider("Prefix color-1",5,colors);
    modules.registerSlider("Prefix color-2",12,colors);
    modules.registerButton("Intro Skip",false);
    modules.registerDescription(util.color("&7- &fSkip the intro."));
    modules.registerDescription(util.color("&7- &f(AD)"));

    modules.registerDescription("Log Settings");
    modules.registerButton("scaffold log",false);
    modules.registerDescription(util.color("&7- &fscaffold log (on off)"));
    modules.registerButton("longjump log",true);
    modules.registerDescription(util.color("&7- &flongjump log (on off)"));
}

void onPostPlayerInput() {
    unti();
}

String getPrefixz() {
    return util.color("&" + colors[(int)modules.getSlider(scriptName,"Prefix color-1")] + "[&" + colors[(int)modules.getSlider(scriptName,"Prefix color-2")] + "EzScLJ&" +  colors[(int)modules.getSlider(scriptName,"Prefix color-1")] + "]");
}

void unti() {

    String guiName = client.getScreen();
    if (guiName != "") return;
    boolean down = keybinds.isKeyDown(keybinds.getKeyIndex(keyNames[(int)modules.getSlider(scriptName,"Switcher keybind")]));
    if (down && modules.getButton(scriptName,"Mode Switcher")) {
        if (LL) return;
        LL = true;
        bindz++;
        if (bindz == 4) bindz = 0;
        if (bindz == 0) {
            modules.setSlider(scriptName,"Mode",1);
            client.print(util.color(getPrefixz() + " &7matanku&f: Mode set to: &6" + modez[(int)modules.getSlider(scriptName,"Mode")]));
        } else if(bindz == 1) {
            modules.setSlider(scriptName,"Mode",0);
            client.print(util.color(getPrefixz() + " &7matanku&f: Mode set to: &6" + modez[(int)modules.getSlider(scriptName,"Mode")]));
        } else if(bindz == 2) {
            modules.setSlider(scriptName,"Mode",2);
            client.print(util.color(getPrefixz() + " &7matanku&f: Mode set to: &6" + modez[(int)modules.getSlider(scriptName,"Mode")]));
        } else if(bindz == 3) {
            modules.setSlider(scriptName,"Mode",3);
            client.print(util.color(getPrefixz() + " &7matanku&f: Mode set to: &6" + modez[(int)modules.getSlider(scriptName,"Mode")]));
        }
    }
    if (!down) LL = false;
}

    double getHorizontalSpeed() {
        return getHorizontalSpeed(client.getPlayer());
    }

    double getHorizontalSpeed(Entity entity) {
        return Math.sqrt(entity.getMotion().x * entity.getMotion().x + entity.getMotion().z * entity.getMotion().z);
    }
void onPreMotion(PlayerState state) {
    if (client.isMoving() && modules.getButton(scriptName,"Strafe") && start) {
        client.setSprinting(true);
        client.setSpeed(getHorizontalSpeed() + 0.005);
    }
}

void onPreUpdate() {
    Entity player = client.getPlayer();
    Vec3 motion = client.getMotion();

    if (!allow) return;
    List<Entity> entities = world.getEntities();
    nearfb = false;
    if (entities != null) for (Entity ent : entities) {
        if (ent.getPosition().distanceTo(client.getPlayer().getPosition()) <= 4 && ent.type.contains("Fireball")) nearfb = true;
    }

    if (fbclicked) {
        clicktick = 0;
    }
    if (keybinds.isMouseDown(1)) {
        tickss = 0;
        migi = true;
    }

    clicktick++;
    tickss++;

    if (player.getHeldItem() != null) fbclicked = tickss <= 2 && player.getHeldItem().name.equals("fire_charge"); else fbclicked = false;

    if (!(player.getHurtTime() > 0 && clicktick <= 10 && migi && (!modules.getButton(scriptName, "Higher precision") || nearfb))) {
        if (!start) return;
    }

    if (player.getHurtTime() >= 3) {
        start = true;
        if (modules.getButton(scriptName,"longjump log")) if (bang) client.print(util.color(getPrefixz() + " &7matanku ["+ modes[(int)modules.getSlider(scriptName,"Scaffold")] +"]&f: Starting modify motion"));
        bang = false;
    }
    if (start) {
        ticks++;

    }

    if (ticks > 0 && ticks < getTickFromMode(1)) {
        if ((int)modules.getSlider(scriptName,"Scaffold") >= 1) {
            inventory.setSlot(slot);
            modules.enable("Scaffold");
        }
        if (modez[(int)modules.getSlider(scriptName,"Mode")] == "Flat") client.setMotion(motion.x,0.01,motion.z);
        if (modez[(int)modules.getSlider(scriptName,"Mode")] == "Vanilla") client.setMotion(motion.x,0.35,motion.z);
        if (modez[(int)modules.getSlider(scriptName,"Mode")] == "High") client.setMotion(motion.x * 0.94,0.42,motion.z * 0.94);
        if (modez[(int)modules.getSlider(scriptName,"Mode")] == "SuperFlat") client.setMotion(motion.x * 1.06, 0.01,motion.z * 1.06);
    } else if (ticks >= getTickFromMode(2)) {
        if (start) {
            if ((int)modules.getSlider(scriptName,"Scaffold") == 2) {
                modules.disable("Scaffold");

                if (modules.getButton(scriptName,"scaffold log"))  client.print(util.color(getPrefixz() + " &7matanku ["+ modes[(int)modules.getSlider(scriptName,"Scaffold")] +"]&f: Disabled Scaffold &7(Tick)"));
            }
            ticks = 0;
            start = false;
            bang = true;
            if (modules.getButton(scriptName,"longjump log")) client.print(util.color(getPrefixz() + " &7matanku ["+ modes[(int)modules.getSlider(scriptName,"Scaffold")] +"]&f: Stopping modify motion"));
        }
    }

    if (player.onGround() && ticks > 5) {
        if (start) {
            if ((int)modules.getSlider(scriptName,"Scaffold") == 2) {
                if (modules.getButton(scriptName,"scaffold log"))  client.print(util.color(getPrefixz() + " &7matanku ["+ modes[(int)modules.getSlider(scriptName,"Scaffold")] +"]&f: &7OnGround"));
                modules.disable("Scaffold");
                inventory.setSlot(slot);
            }
        }
    }


    if (start && player.getHurtTime() == 9 && keybinds.isPressed("forward")) {
        client.setSpeed(1.6);
    }
    if (start && player.getHurtTime() == 8 && keybinds.isPressed("forward")) {
        client.setSpeed(1.6);
    }

}

void onDisable() {
    start = false;
    ticks = 0;
}

int getTickFromMode(int a) {
    switch (modez[(int)modules.getSlider(scriptName,"Mode")]) {
        case "Flat":
            switch (a) {
                case 1:
                    return 30;
                case 2:
                    return 40;
            }
        case "Vanilla":
            switch (a) {
                case 1:
                    return 20;
                case 2:
                    return 40;
            }
        case "High":
            switch (a) {
                case 1:
                    return 40;
                case 2:
                    return 50;
            }
        case "SuperFlat":
            switch (a) {
                case 1:
                    return 40;
                case 2:
                    return 55;
            }
    }
    return 30;
}


void onEnable() {
    client.async(()->{
        client.sleep(50);
        slot = inventory.getSlot();
        allow = false;
        if (modules.getButton(scriptName,"Intro Skip")) {
            modules.setSlider("Long Jump", "Boost ticks", 0);
            modules.setSlider("Long Jump", "Mode", 0);
            modules.setButton("Long Jump", "Jump", false);
            modules.enable("Long Jump");
            client.ping();
            allow = true;
            allowedToUse = true;
            client.print(util.color(getPrefixz() + " &aEnabled"));
        } else {
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku: &fHi!"));
            client.sleep(1500);
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku: &fMy name is matanku."));
            client.sleep(1500);
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku: &fI will be your &dassistant &fin this system!"));
            client.sleep(1500);
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku&f: Setting LongJump!"));
            modules.setSlider("Long Jump", "Boost ticks", 0);
            modules.setSlider("Long Jump", "Mode", 0);
            modules.setButton("Long Jump", "Jump", false);
            modules.enable("Long Jump");
            client.sleep(1500);
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku&f: Mode is set to &9&l" + modes[(int)modules.getSlider(scriptName,"Scaffold")].toUpperCase()+ "&f!"));
            client.sleep(1500);
            client.ping();
            client.print(util.color(getPrefixz() + " &7matanku: &fHere we go!"));
            allow = true;
            client.sleep(1500);
            client.playSound("random.levelup", 1, 0.5F);
            client.print(util.color("&6&m------------------------"));
            client.print(util.color("&r&7    &bEasy Scaffold LongJump"));
            client.print(util.color("&r&7            &6Premium Script"));
            client.print(util.color("&r&7       made by &6mtnky &c<3"));
            client.print(util.color("&6&m------------------------"));
        }
    });
}
