package cz.blackshark.model

/**
 * Data structure holding information about time overlapping on timeline
 */
data class TimeOverlapVo(val isOverlap: Boolean, val overlapIds: List<Long>)
