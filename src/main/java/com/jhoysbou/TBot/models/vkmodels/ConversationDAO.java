package com.jhoysbou.TBot.models.vkmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationDAO {
    private long count;
    private PeerDAO peer;
    private CanWrite can_write;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public PeerDAO getPeer() {
        return peer;
    }

    public void setPeer(PeerDAO peerDAO) {
        this.peer = peerDAO;
    }

    public CanWrite getCan_write() {
        return can_write;
    }

    public void setCan_write(CanWrite can_write) {
        this.can_write = can_write;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PeerDAO {
        private long id;
        private String type;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
