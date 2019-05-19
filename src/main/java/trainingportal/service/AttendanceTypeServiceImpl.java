package trainingportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trainingportal.dao.AttendanceTypeDao;
import trainingportal.model.AttendanceType;
import trainingportal.service.generic.GenericService;
import trainingportal.service.generic.GenericServiceImpl;

import java.util.List;

@Service
public class AttendanceTypeServiceImpl extends GenericServiceImpl<AttendanceType> implements AttendanceTypeService {

    private final AttendanceTypeDao attendanceTypeDao;

    @Autowired
    public AttendanceTypeServiceImpl(AttendanceTypeDao attendanceTypeDao) {
        this.attendanceTypeDao = attendanceTypeDao;
    }

    @Override
    public List<AttendanceType> getAllAttendanceList() {
        return attendanceTypeDao.findAllAttendanceList();
    }
}
