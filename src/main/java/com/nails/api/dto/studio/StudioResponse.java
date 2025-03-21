package com.nails.api.dto.studio;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudioResponse {
    private Long id;
    private String name;
    private String description;
    private String logoUrl;
    private String address;
    private String contactPhone;
    private String contactEmail;
    private String businessHours;
    private List<StudioProviderDTO> providers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
