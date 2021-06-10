package org.sacco.backend.models;

public enum Role {
    ADMIN {
        @Override
        public String toString() {
            return "Admin";
        }
    },

    SUPER_ADMIN {
        @Override
        public String toString() {
            return "Super Admin";
        }
    },

    CLIENT {
        @Override
        public String toString() {
            return "Client";
        }
    },
}
