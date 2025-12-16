package com.kuspidartistmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {

    // Summary statistics
    private Long totalSends;
    private Long successfulSends;
    private Long failedSends;
    private Long pendingSends;
    private Long totalReplies;
    private Double overallReplyRate;

    // Top performers
    private List<BeatStatistic> mostSentBeats;
    private List<PackStatistic> mostSentPacks;
    private List<ArtistStatistic> mostContactedArtists;
    private List<ArtistStatistic> artistsWithHighestReplyRate;

    // Time-series data
    private Map<String, Long> sendsPerDay;
    private Map<String, Long> sendsPerWeek;
    private Map<String, Long> sendsPerMonth;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BeatStatistic {
        private UUID beatId;
        private String beatTitle;
        private Long sendCount;
        private Long replyCount;
        private Double replyRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackStatistic {
        private UUID packId;
        private String packName;
        private Long sendCount;
        private Long replyCount;
        private Double replyRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArtistStatistic {
        private UUID artistId;
        private String artistName;
        private Long contactCount;
        private Long replyCount;
        private Double replyRate;
    }
}