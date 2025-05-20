package com.turbolessons.videoservice.model;

import java.util.Objects;

public class SimpleBlobInfo {
    private String name;
    private String id;

    public SimpleBlobInfo() {
    }

    public SimpleBlobInfo(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleBlobInfo that = (SimpleBlobInfo) o;

        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}

