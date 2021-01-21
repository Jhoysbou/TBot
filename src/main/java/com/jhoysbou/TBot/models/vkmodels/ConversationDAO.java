package com.jhoysbou.TBot.models.vkmodels;

public class ConversationDAO {
    private long count;
    private PeerDAO peerDAO;
    private int can_write;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public PeerDAO getPeer() {
        return peerDAO;
    }

    public void setPeer(PeerDAO peerDAO) {
        this.peerDAO = peerDAO;
    }

    public int getCan_write() {
        return can_write;
    }

    public void setCan_write(int can_write) {
        this.can_write = can_write;
    }

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
