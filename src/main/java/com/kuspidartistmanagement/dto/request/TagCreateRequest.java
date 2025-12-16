package com.kuspidartistmanagement.dto.request;

import com.kuspidartistmanagement.domain.enums.TagType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagCreateRequest {

    @NotBlank(message = "Tag name is required")
    private String name;

    private TagType type = TagType.CUSTOM;
}