package trainingportal.dao;

import trainingportal.model.User;

import java.util.List;

public interface SubordinateDAO {

    List<User> findSubordinatesById(Long id);

    User findManagerBySubordinateId(Long id);

    List<User> findFreeUsers();

    void setManagerId(Long managerId, Long userId);

    List<User> getSubordinatesByIdAsPage(int page, int rowsPerPage, Long id);

    List<User> getFreeUsersAsPage(int page, int rowsPerPage);

    int countAllByManager(Long id);

    int countFreeUsers();
}
