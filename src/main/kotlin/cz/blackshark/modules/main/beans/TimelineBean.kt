package cz.blackshark.modules.main.beans

import cz.blackshark.modules.main.domains.OperationResult
import cz.blackshark.modules.main.domains.ValidationError
import cz.blackshark.modules.main.dto.TimelineVo
import cz.blackshark.modules.main.factories.TimelineEntityFactory
import cz.blackshark.modules.main.persistence.repository.ProjectRepository
import cz.blackshark.modules.main.persistence.repository.TimelineRepository
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class TimelineBean @Inject constructor(
    internal val timelineRepository: TimelineRepository,
    internal val projectRepository: ProjectRepository
) {

    fun saveUpdate(timelineVo: TimelineVo): OperationResult {
        val validationResult = validate(timelineVo)
        if (validationResult.isNotEmpty()) {
            return OperationResult(false, null, validationResult)
        } else {
            val project = projectRepository.findByIdOptional(timelineVo.projectId)
            if (!project.isPresent) {
                return OperationResult(
                    false,
                    null,
                    listOf(ValidationError(TimelineVo::class.java, "projectId", "Requested project ID not found!"))
                )
            } else {
                val entity = if (timelineVo.id != null) {
                    timelineRepository.findById(timelineVo.id).apply {
                        fromTime = timelineVo.fromTime
                        toTime = timelineVo.toTime
                        projectEntity = project.get()
                        note = timelineVo.note
                        pause = timelineVo.pause
                    }
                } else {
                    TimelineEntityFactory.create(timelineVo, project.get())
                }
                timelineRepository.persistAndFlush(entity)
                if (entity.id == null) {
                    return OperationResult(
                        false,
                        null,
                        listOf(ValidationError(TimelineVo::class.java, "id", "Save TimelineEntity failed"))
                    )
                } else {
                    return OperationResult(true, entity.id, listOf())
                }
            }
        }
    }

    fun validate(timelineVo: TimelineVo): List<ValidationError> {
        val res = mutableListOf<ValidationError>()

        if (timelineRepository.isTimeAlreadyUsed(timelineVo.fromTime, timelineVo.id)) {
            res.add(ValidationError(TimelineVo::class.java, "fromTime", "fromTime is alredy used in timeline"))
        }

        if (timelineRepository.isTimeAlreadyUsed(timelineVo.toTime, timelineVo.id)) {
            res.add(ValidationError(TimelineVo::class.java, "toTime", "toTime is alredy used in timeline"))
        }

        return res
    }

}
