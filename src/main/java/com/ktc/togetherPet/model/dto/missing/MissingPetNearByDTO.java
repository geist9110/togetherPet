package com.ktc.togetherPet.model.dto.missing;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record MissingPetNearByDTO(
    long petId,
    double latitude,
    double longitude,
    String petImageUrl

    //TODO Missing Id 추가
) {

}
