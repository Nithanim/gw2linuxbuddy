package me.nithanim.gw2linuxbuddy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import me.nithanim.gw2linuxbuddy.services.OsLoginNameProvider;

@ApplicationScoped
@RequiredArgsConstructor
public class ProcessStartService {
  private final OsLoginNameProvider osLoginNameProvider;

  public Process start(ProcessInstanceBuilder builder) throws IOException {

    InstanceDataFolder instanceDataFolder = builder.getInstanceDataFolder();

    if (!Files.exists(instanceDataFolder.getAppdataPath())) {
      Files.createDirectories(instanceDataFolder.getAppdataPath());
    }
    Path realAppdataPath =
        instanceDataFolder
            .getPrefixPath()
            .resolve("drive_c/users")
            .resolve(osLoginNameProvider.getLoginName())
            .resolve("Application Data/Guild Wars 2");

    if (!Files.exists(realAppdataPath)) {
      Files.createDirectories(realAppdataPath.getParent());
      Path rel = realAppdataPath.getParent().relativize(instanceDataFolder.getAppdataPath());
      Files.createSymbolicLink(realAppdataPath, rel);
    }

    ProcessBuilder pb = new ProcessBuilder();
    pb.environment().putAll(builder.getEnvironmentParameters());
    pb.environment()
        .put(
            "WINEPREFIX",
            builder.getInstanceDataFolder().getPrefixPath().toAbsolutePath().toString());
    pb.command(buildCommandlineString(builder));
    pb.inheritIO();

    return pb.start();
  }

  private static List<String> buildCommandlineString(ProcessInstanceBuilder builder) {
    List<String> command = new ArrayList<>();
    command.add(builder.getWinePath().toAbsolutePath().toString());
    command.add(builder.getExecutablePath().toAbsolutePath().toString());
    command.addAll(
        Optional.ofNullable(builder.getArguments()).stream()
            .<String>mapMulti((a, s) -> Arrays.asList(a.split(" ")).forEach(s))
            .collect(Collectors.toList()));
    return command;
  }
}
