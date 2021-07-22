package net.noobsters.core.paper;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor(staticName = "create")
@Data
public class PlayerMask {

    String uuid;
    long joinTime;
    long timePlayed;

    public void update(long timeVariable) {
        timePlayed += (System.currentTimeMillis() - joinTime);
        joinTime = timeVariable;
    }

    public long getTotalNow() {
        return timePlayed + (System.currentTimeMillis() - joinTime);
    }

}
