package com.g1ax.namehistory.data;

import java.util.List;

public class PlayerData {
    private final String currentName;
    private final String uuid;
    private final List<NameEntry> nameHistory;
    private final long fetchTime;
    private final String source;

    public PlayerData(String currentName, String uuid, List<NameEntry> nameHistory, String source) {
        this.currentName = currentName;
        this.uuid = uuid;
        this.nameHistory = nameHistory;
        this.fetchTime = System.currentTimeMillis();
        this.source = source;
    }

    public String getCurrentName() { return currentName; }
    public String getUuid() { return uuid; }
    public List<NameEntry> getNameHistory() { return nameHistory; }
    public long getFetchTime() { return fetchTime; }
    public String getSource() { return source; }

    public static class NameEntry {
        private final String name;
        private final Long changedAt;

        public NameEntry(String name, Long changedAt) {
            this.name = name;
            this.changedAt = changedAt;
        }

        public String getName() { return name; }
        public Long getChangedAt() { return changedAt; }
    }
}
