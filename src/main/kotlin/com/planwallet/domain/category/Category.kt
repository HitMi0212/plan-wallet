package com.planwallet.domain.category

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import java.time.Instant

/**
 * 카테고리 엔티티.
 */
@Schema(description = "카테고리 엔티티")
@Entity
@Table(name = "categories")
class Category(
    /** 사용자 ID */
    @field:Schema(description = "사용자 ID", example = "1")
    @Column(nullable = false)
    var userId: Long,

    /** 카테고리 유형(수입/지출) */
    @field:Schema(description = "카테고리 유형", example = "EXPENSE")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: CategoryType,

    /** 카테고리 이름 */
    @field:Schema(description = "카테고리 이름", example = "식비")
    @Column(nullable = false)
    var name: String,
) {
    /** 카테고리 ID */
    @field:Schema(description = "카테고리 ID", example = "10")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    /** 삭제 여부(soft delete) */
    @field:Schema(description = "삭제 여부")
    @Column(nullable = false)
    var isDeleted: Boolean = false
        private set

    /** 생성 시각 */
    @field:Schema(description = "생성 시각")
    @Column(nullable = false)
    var createdAt: Instant? = null
        private set

    /** 수정 시각 */
    @field:Schema(description = "수정 시각")
    @Column(nullable = false)
    var updatedAt: Instant? = null
        private set

    /**
     * 카테고리 이름 변경.
     */
    fun rename(newName: String) {
        name = newName
    }

    /**
     * 카테고리 삭제 처리(soft delete).
     */
    fun markDeleted() {
        isDeleted = true
    }

    @PrePersist
    fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
