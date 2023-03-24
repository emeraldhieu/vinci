package com.emeraldhieu.vinci.shipping.logic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Status {

    @JsonProperty("unrecognized")
    UNRECOGNIZED("unrecognized", "Unrecognized"),

    @JsonProperty("inProgress")
    IN_PROGRESS("inProgress", "In progress"),

    @JsonProperty("done")
    DONE("done", "Done"),

    @JsonProperty("pending")
    PENDING("pending", "Pending");

    private final String keyword;
    private final String name;
    private static final Map<String, Status> statusesByKeyword = new HashMap<>();

    static {
        Arrays.stream(Status.values()).forEach(status -> {
            statusesByKeyword.put(status.getKeyword(), status);
        });
    }

    public static Status forKeyword(String keyword) {
        return Optional.ofNullable(statusesByKeyword.get(keyword))
            .orElseThrow(() -> new IllegalArgumentException(String.format("Unit '%s' not found.", keyword)));
    }

    public String toKeyword() {
        return keyword;
    }
}
