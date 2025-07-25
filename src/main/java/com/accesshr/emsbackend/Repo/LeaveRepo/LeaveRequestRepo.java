package com.accesshr.emsbackend.Repo.LeaveRepo;

import com.accesshr.emsbackend.Entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByLeaveStatus(LeaveRequest.LeaveStatus status);

    List<LeaveRequest> findByManagerId(String managerId);

    List<LeaveRequest> findByEmployeeId(String employeeId);

    List<LeaveRequest> findByManagerIdAndLeaveStatus(String managerId, LeaveRequest.LeaveStatus leaveStatus);

    List<LeaveRequest> findByEmployeeIdAndLeaveStatus(String employeeId, LeaveRequest.LeaveStatus leaveStatus);

    Optional<LeaveRequest> findByEmployeeIdAndLeaveStartDateAndLeaveEndDate(String employeeId, LocalDate leaveStartDate, LocalDate leaveEndDate);

    long countByEmployeeIdAndLeaveType(String employeeId, LeaveRequest.LeaveType leaveType);

    Optional<LeaveRequest> findByEmployeeIdAndLeaveType(String employeeId, LeaveRequest.LeaveType leaveType);

//    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employeeId = :employeeId " +
//            "AND lr.leaveStartDate <= :leaveEndDate AND lr.leaveEndDate >= :leaveStartDate")
//    List<LeaveRequest> findOverlappingLeaves(@Param("employeeId") String employeeId,
//                                             @Param("leaveStartDate") LocalDate leaveStartDate,
//                                             @Param("leaveEndDate") LocalDate leaveEndDate);

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId " +
            "AND lr.leaveStatus IN ('PENDING', 'APPROVED') " +
            "AND (lr.leaveStartDate BETWEEN :leaveStartDate AND :leaveEndDate " +
            "OR lr.leaveEndDate BETWEEN :leaveStartDate AND :leaveEndDate " +
            "OR :leaveStartDate BETWEEN lr.leaveStartDate AND lr.leaveEndDate " +
            "OR :leaveEndDate BETWEEN lr.leaveStartDate AND lr.leaveEndDate)")
    long countOverlappingLeaves(
            @Param("employeeId") String employeeId,
            @Param("leaveStartDate") LocalDate leaveStartDate,
            @Param("leaveEndDate") LocalDate leaveEndDate);

    @Query("SELECT lr FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId " +
            "AND lr.leaveStartDate = :startDate " +
            "AND lr.leaveEndDate = :endDate " +
            "AND lr.leaveStatus IN ('PENDING', 'APPROVED')")
    Optional<LeaveRequest> findActiveLeaveByEmployeeAndStartAndEndDate(
            @Param("employeeId") String employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);



    @Query("SELECT SUM(lr.duration) " +
            "FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId AND lr.leaveType = :leaveType " +
            "AND lr.leaveStatus != 'REJECTED'")
    Optional<Integer> getTotalLeaveDaysByEmployeeIdAndLeaveType(@Param("employeeId") String employeeId,
                                                                @Param("leaveType") LeaveRequest.LeaveType leaveType);

    @Modifying
    @Transactional
    @Query("DELETE FROM LeaveRequest lr WHERE YEAR(lr.leaveStartDate) < :year")
    void resetLeaveBalancesForAllEmployees(@Param("year") int year);

    @Query("SELECT SUM(lr.duration) FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId AND lr.leaveType = :leaveType " +
            "AND lr.leaveStatus = 'APPROVED' AND lr.LOP = false")
    Optional<Integer> getApprovedPaidLeaveDays(@Param("employeeId") String employeeId,
                                               @Param("leaveType") LeaveRequest.LeaveType leaveType);
}
