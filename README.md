# GW2LinuxBuddy

GW2LinuxBuddy is a launcher for multiple Guild Wars 2 instances on linux.
It aims to be the alternative to e.g. [Gw2Launcher](https://github.com/Healix/Gw2Launcher) or [Gw2 Launchbuddy](https://github.com/TheCheatsrichter/Gw2_Launchbuddy).

CURRENTLY FOR DEVELOPERS ONLY!

## WARNING

This program is in very early development.
Also, it currently satisfies my needs so there is no active development.
A lot of values are hardcoded.
However, It would be great if you made some improvements to it!

Internally, every "instance" (effectively a profile, a game account) has its own wine prefix.
This allows launching multiple instances of the game and also having separate settings.
The game data is stored only once.
The downside ist, that there is (currently) no safety check when updating and it is likely possible that multiple instances updating the game files, destroy them.

Instance data is stored in the subfolder `data` in the current working dir.
In there every instance/profile has their own folder.
The most important subfolder in there is `prefix` which holds the wine prefix.
I symlinked the appdata folder in the prefix to a `appdata` folder right beside `prefix`.
This enables easy upgrading of the prefixes without losing settings (and therefore the stored login details).

Overview:
```
data
    instances
        main
            appdata
            prefix
        2
            appdata
            prefix
        3
            appdata
            prefix
        ...
```

Although there is a "stop" button for a profile, it currently des not work.

