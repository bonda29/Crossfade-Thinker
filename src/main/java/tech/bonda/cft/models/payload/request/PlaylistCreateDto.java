package tech.bonda.cft.models.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PlaylistCreateDto implements Serializable {
    private String name;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("is_public")
    private boolean isPublic;
    private String description;
}
