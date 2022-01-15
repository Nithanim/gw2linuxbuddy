package me.nithanim.gw2linuxbuddy;

import java.nio.file.Path;

import lombok.Value;

@Value
public class InstanceDataFolder {
    String instanceId;
    Path instancePath;
    Path prefixPath;
    Path appdataPath;

    public InstanceDataFolder(String instanceId, Path rootPath) {
        this.instanceId = instanceId;
        this.instancePath = rootPath.resolve(instanceId);
        this.prefixPath = instancePath.resolve("prefix");
        this.appdataPath = instancePath.resolve("appdata");
    }
}
