package ru.sage.reminderservice.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.sage.reminderservice.domain.model.Reminder
import ru.sage.reminderservice.domain.usecase.*
import ru.sage.reminderservice.presentation.model.CreateReminderRequest
import ru.sage.reminderservice.presentation.model.UpdateReminderRequest
import java.util.UUID

@RestController
@RequestMapping("/api/reminders")
class ReminderController(
    private val createReminderUseCase: CreateReminderUseCase,
    private val findReminderByIdUseCase: FindReminderByIdUseCase,
    private val findReminderByOwnerIdUseCase: FindReminderByOwnerIdUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val deleteReminderByIdUseCase: DeleteReminderByIdUseCase
) {

    @PostMapping("/")
    fun create(@RequestBody request: CreateReminderRequest): ResponseEntity<Reminder> {
        val created = createReminderUseCase.execute(
            topic = request.topic,
            ownerId = request.ownerId,
            remindAt = request.remindAt
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<Reminder> {
        val reminder = findReminderByIdUseCase.execute(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(reminder)
    }

    @GetMapping("/owner/{ownerId}")
    fun getByOwner(@PathVariable ownerId: UUID): ResponseEntity<List<Reminder>> {
        val reminders = findReminderByOwnerIdUseCase.execute(ownerId) ?: emptyList()
        return ResponseEntity.ok(reminders)
    }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: UpdateReminderRequest
    ): ResponseEntity<Void> {
        val success = updateReminderUseCase.execute(
            reminderId = id,
            topic = request.topic,
            remindAt = request.remindAt
        )
        return if (success) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        val success = deleteReminderByIdUseCase.execute(id)
        return if (success) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}