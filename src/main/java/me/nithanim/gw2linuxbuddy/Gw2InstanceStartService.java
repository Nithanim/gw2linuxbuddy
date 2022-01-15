package me.nithanim.gw2linuxbuddy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class Gw2InstanceStartService {
  @Inject ProcessStartService processStartService;

  public RunningInstance startGame(Gw2InstanceBuilder builder) throws IOException {

    Path burritoPath = builder.instanceDataFolder.getInstancePath().resolve("burrito_link.exe");

    Process gameProcess =
        processStartService.start(
            ProcessInstanceBuilder.builder()
                .instanceId(builder.getInstanceId())
                .arguments(builder.getArguments())
                .environmentParameters(builder.getEnvironmentParameters())
                .executablePath(builder.getExecutablePath())
                .instanceDataFolder(builder.getInstanceDataFolder())
                .winePath(builder.getWinePath())
                .build());
    System.out.println(gameProcess.pid());

    Process burritoProcess;
    if (Files.exists(burritoPath) && false) {
      ProcessInstanceBuilder.ProcessInstanceBuilderBuilder burritoBuilder =
          ProcessInstanceBuilder.builder()
              .instanceId(builder.getInstanceId())
              .instanceDataFolder(builder.getInstanceDataFolder())
              .environmentParameters(builder.getEnvironmentParameters())
              .executablePath(burritoPath.toAbsolutePath())
              .winePath(builder.winePath);
      burritoProcess = processStartService.start(burritoBuilder.build());

      Thread burritoThread =
          new Thread(
              () -> {
                try {
                  gameProcess.waitFor();
                } catch (InterruptedException ex) {
                }
                burritoProcess.descendants().forEach(ProcessHandle::destroy);
              });
      burritoThread.setName("instance-" + builder.getInstanceId() + "-burrito");
      burritoThread.start();
    } else {
      burritoProcess = null;
    }

    return new RunningInstance(gameProcess, burritoProcess);
  }

  @AllArgsConstructor
  public static class RunningInstance {
    @Getter Process gameProcess;
    @Getter Process burritoProcess;

    public void stop() {
      if (burritoProcess != null) {
        burritoProcess.destroy();
      }
      gameProcess.destroy();
    }
  }
}
