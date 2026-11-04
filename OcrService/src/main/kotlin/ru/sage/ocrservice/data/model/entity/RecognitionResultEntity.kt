package ru.sage.ocrservice.data.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "recognition_result")
class RecognitionResultEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var noteId: UUID,

    @Column(nullable = false)
    var fileId: UUID,

    @Column(columnDefinition = "TEXT", nullable = true)
    var text: String?
)