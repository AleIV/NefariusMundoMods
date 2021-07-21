package net.noobsters.core.paper;

import lombok.Data;

@Data
public class PlayerMask {

    String uuid;
    long joinTime;
    long timePlayed;
    
    public PlayerMask(String uuid, long joinTime, long timePlayed){
        this.uuid = uuid;
        this.joinTime = joinTime;
        this.timePlayed = timePlayed;
    }
}
