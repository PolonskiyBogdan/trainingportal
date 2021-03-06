package trainingportal.service;

import trainingportal.model.Schedule;
import trainingportal.service.generic.GenericService;

import java.util.List;

public interface ScheduleService extends GenericService<Schedule> {
    List<Schedule> findAllByGroupId(Long id);

    List<Schedule> getAllAsPageByGroupId(Long scheduleGroupId, int page, int rowsPerPage);

    int countAllByGroupId(Long scheduleGroupId);

    int getPages(Long scheduleId, double rowsPerPage);

    List<Schedule> getSchedules(Long id);
}
