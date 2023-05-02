package de.lars.Importants.Events;

import java.util.UUID;

public class MutedPlayer {

    private final UUID uuid;
    private final long expire;
    private final String reason;

    public MutedPlayer(UUID uuid, long expire, String reason) {
        this.uuid = uuid;
        this.expire = expire;
        this.reason = reason;
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getExpire() {
        return expire;
    }

    public String getReason() {
        return reason;
    }

}
