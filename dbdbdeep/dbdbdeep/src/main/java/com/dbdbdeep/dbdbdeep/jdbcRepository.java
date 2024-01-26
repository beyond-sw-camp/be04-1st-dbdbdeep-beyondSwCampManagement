package com.dbdbdeep.dbdbdeep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class jdbcRepository {
    private final   JdbcTemplate jdbcTemplate;

    @Autowired
    public  jdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
///////////// 매니저
    public void insertManager(String mg_name, String mg_phone, String mg_mail) {
        String sql = "INSERT INTO manager (mg_name, mg_phone, mg_mail) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, mg_name, mg_phone, mg_mail);
    }
    public void deleteManager(Integer mg_id) {
        String sql = "DELETE FROM manager WHERE mg_id = ?";
        jdbcTemplate.update(sql, mg_id);
    }
    public List<Manager> getAllManagers() {
        String sql = "SELECT * FROM manager" ;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manager.class));
    }
    public List<Manager> getNameManagers(String mg_name) {
        String sql = "SELECT * FROM manager WHERE mg_name = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manager.class), mg_name);
    }

    public List<Manager> getPhoneManagers(String mg_phone) {
        String sql = "SELECT * FROM manager WHERE mg_phone = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manager.class), mg_phone);
    }

    public List<Manager> getMailManagers(String mg_mail) {
        String sql = "SELECT * FROM manager WHERE mg_mail = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Manager.class), mg_mail);
    }
////////////////////


    /////////////student
    public List<Student> getAllStudent() {
        String sql = "SELECT * FROM student" ;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class));
    }

    public void insertStudent(String std_name, String std_phone, String std_mail, Integer gen_id, Integer mt_id, String std_vol) {
        String sql = "INSERT INTO student (std_name, std_phone, std_mail, gen_id, mt_id, std_vol) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, std_name, std_phone, std_mail, gen_id, mt_id, std_vol);
    }

    public void deleteStudent(Integer std_id) {
        String sql = "DELETE FROM student WHERE std_id = ?";
        jdbcTemplate.update(sql, std_id);
    }
    public List<Student> getNameStudent(String std_name) {
        String sql = "SELECT * FROM student WHERE std_name = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class), std_name);
    }

    public List<Student> getPhoneStudent(String std_phone) {
        String sql = "SELECT * FROM student WHERE std_phone = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class), std_phone);
    }

    public List<Student> getMailStudent(String std_mail) {
        String sql = "SELECT * FROM student WHERE std_mail = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Student.class), std_mail);
    }

    ////////////////////
///////////// 강사
    public void insertTeacher(String tc_name, String tc_phone, String tc_mail) {
        String sql = "INSERT INTO manager (tc_name, tc_phone, tc_mail) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, tc_name, tc_phone, tc_mail);
    }
    public void deleteTeacher(Integer tc_id) {
        System.out.println(tc_id);
        String sql = "DELETE FROM manager WHERE tc_id = ?";
        jdbcTemplate.update(sql, tc_id);
    }
    public List<Teacher> getAllTeacher() {
        String sql = "SELECT * FROM teacher" ;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Teacher.class));
    }
    public List<Teacher> getNameTeacher(String tc_name) {
        String sql = "SELECT * FROM teacher WHERE tc_name = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Teacher.class), tc_name);
    }

    public List<Teacher> getPhoneTeacher(String tc_phone) {
        String sql = "SELECT * FROM teacher WHERE tc_phone = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Teacher.class), tc_phone);
    }

    public List<Teacher> getMailTeacher(String tc_mail) {
        String sql = "SELECT * FROM teacher WHERE tc_mail = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Teacher.class), tc_mail);
    }
////////////////////


    ///////////////기능
    /// 출결조회
    public List<Rollbook> getRollbooksByStudentId(Integer std_id) {
        System.out.println(std_id);
        String sql = "SELECT * FROM rollbook WHERE std_id = ?";
        return jdbcTemplate.query(sql, new Object[]{std_id}, new BeanPropertyRowMapper<>(Rollbook.class));
    }
    /// 지원금 산정
    public List<Money> getMoneyByStudentId(Integer std_id) {
        System.out.println(std_id);
        String sql = "SELECT  a.std_id AS '학번'\n" +
                "\t\t  , b.std_name\n" +
                "\t\t  , CONCAT(TRUNCATE(((a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0),'%') AS '출석율'\n" +
                "\t\t  , CASE\n" +
                "\t\t  \t\tWHEN TRUNCATE(((a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))/a.총교육일수)*100,0) >= 80\n" +
                "\t\t  \t\tTHEN (a.총교육일수- (a.결석 + TRUNCATE((a.조퇴+a.지각+a.외출)/3,0)))*5800 + 200000\n" +
                "\t\t\t \tELSE 0 END AS '지원금산정'\n" +
                "  FROM  (\n" +
                "\t\t\tSELECT  std_id\n" +
                "\t\t\t\t\t  , COUNT(CASE WHEN STATUS = '출석' THEN 1 ELSE NULL END) AS '출석'\n" +
                "\t\t\t\t\t  , COUNT(CASE WHEN STATUS = '결석' THEN 1 ELSE NULL END) AS '결석'\n" +
                "\t\t\t\t\t  , COUNT(CASE WHEN STATUS = '조퇴' THEN 1 ELSE NULL END) AS '조퇴'\n" +
                "\t\t\t\t\t  , COUNT(CASE WHEN STATUS = '지각' THEN 1 ELSE NULL END) AS '지각'\n" +
                "\t\t\t\t\t  , COUNT(CASE WHEN STATUS = '외출' THEN 1 ELSE NULL END) AS '외출'\n" +
                "\t\t\t\t\t  , COUNT(std_id) AS '총교육일수'       \n" +
                "\t\t\tFROM  rollbook r\n" +
                "\t\t\tWHERE MONTH(rb_date) = 12 AND r.std_id = ?\n" +
                "\t\t) AS a\n" +
                "  JOIN  student AS b\n" +
                "  ON a.std_id = b.std_id\n" +
                ";";
        return jdbcTemplate.query(sql, new Object[]{std_id}, new BeanPropertyRowMapper<>(Money.class));
    }

}
