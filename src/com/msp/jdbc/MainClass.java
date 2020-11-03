package com.msp.jdbc;

import com.msp.jdbc.model.MarkBoardModel;
import com.msp.jdbc.model.StudentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.msp.jdbc.dao.DatabaseConnection.connection;
import static com.msp.jdbc.util.DateUtils.isValidDate;
import static com.msp.jdbc.util.FormatDate.FORMAT_4;
import static com.msp.jdbc.util.StringUtils.isNullOrBlank;
import static com.msp.jdbc.util.SubjectEnum.values;

public class MainClass {

    private static final Scanner sc = new Scanner(System.in);
    private static final Connection con = connection();

    static final String studentDeleteById = "DELETE FROM student WHERE id = ?";
    static final String studentFindByAvgMarkLessThanFive = "SELECT * FROM student WHERE avg_mark < 5";
    static final String studentFindTopTen = "SELECT * FROM student ORDER BY avg_mark DESC LIMIT 10";
    static final String studentFindByClass = "SELECT * FROM student WHERE class_code LIKE ? ORDER BY avg_mark DESC";
    static final String studentFindByCodeOrName = "SELECT * FROM student WHERE code LIKE ? OR name LIKE ?";
    static final String studentFindAllSQL = "SELECT * FROM student";
    static final String studentFindById = "SELECT * FROM student WHERE id = ?";
    static final String studentInsertSQL = "INSERT INTO student(code, name, date_of_birth, class_code) VALUES(?, ?, ?, ?)";
    static final String studentUpdateSQL = "UPDATE student SET code = ? , name = ? , date_of_birth = ? , class_code = ? WHERE id = ?";
    static final String studentUpdateAvg = "UPDATE student SET avg_mark = ? WHERE id = ?";
    static final String markInsertSQL = "INSERT INTO mark_board(student_id, subject_name, mark, mark_year) VALUES(?, ?, ?, ?)";
    static final String markDeleteByStudentId = "DELETE FROM mark_board WHERE student_id = ?";
    static final String markFindByStudentId = "SELECT * FROM mark_board WHERE student_id = ?";

    public static void main(String[] args) throws SQLException {
        System.out.println("--- Chào mừng tới ứng dụng quản lý học sinh ---");
        int selection = 0;

        do {
            System.out.println("Chọn chức năng: ");
            System.out.println("1. Thêm học sinh");
            System.out.println("2. Sửa học sinh");
            System.out.println("3. Xóa học sinh");
            System.out.println("4. Tìm kiếm học sinh (theo mã hoặc tên)");
            System.out.println("5. Liệt kê danh sách học sinh theo lớp");
            System.out.println("6. Tìm kiếm top 10 học sinh xuất sắc nhất theo năm");
            System.out.println("7. Tìm kiếm các học sinh có điểm trung bình các môn nhỏ hơn 5");
            System.out.println("#. Thoát");

            while (!sc.hasNextInt()) {
                System.out.println("Lựa chọn không hợp lệ!");
                sc.next();
            }
            selection = sc.nextInt();

            switch (selection) {
                case 1:
                    createStudent(null);
                    break;
                case 2:
                    editStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    findStudentByCodeOrName();
                    break;
                case 5:
                    findStudentsByClass();
                    break;
                case 6:
                    findTopTen();
                    break;
                case 7:
                    findByAverage();
                    break;
            }
        } while (selection > 0 && selection < 8);
    }

    private static boolean isStudentCodeExist(String code) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE code LIKE '" + code + "'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        return rs != null && rs.next();
    }

    static void createStudent(StudentModel foundModel) throws SQLException {
        StudentModel student = foundModel == null ? new StudentModel() : foundModel;
        System.out.println("--- Nhập vào thông tin học sinh ---");
        System.out.println("Mã học sinh: ");
        sc.nextLine();

        String code;
        String name;
        String dateOfBirth;
        String classCode;

        do {
            code = sc.nextLine();
            if (isStudentCodeExist(code)) {
                System.out.println("Mã học sinh đã tồn tại!");
            } else if (foundModel == null && isNullOrBlank(code)) {
                System.out.println("Bắt buộc nhập mã học sinh!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        code = (foundModel != null && isNullOrBlank(code)) ? foundModel.getCode() : code;

        System.out.println("Tên học sinh: ");
        do {
            name = sc.nextLine();
            if (foundModel == null && isNullOrBlank(name)) {
                System.out.println("Bắt buộc nhập tên học sinh!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        name = (foundModel != null && isNullOrBlank(name)) ? foundModel.getName() : name;

        System.out.println("Ngày sinh (yyyy-mm-dd): ");
        do {
            dateOfBirth = sc.nextLine();
            if (foundModel == null && isNullOrBlank(dateOfBirth)) {
                System.out.println("Bắt buộc nhập ngày sinh!");
            } else if (!isNullOrBlank(dateOfBirth) && !isValidDate(dateOfBirth, FORMAT_4)) {
                System.out.println("Sai định dạng ngày tháng!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        dateOfBirth = (foundModel != null && isNullOrBlank(dateOfBirth)) ? foundModel.getDateOfBirth() : dateOfBirth;

        System.out.println("Mã lớp: ");
        do {
            classCode = sc.nextLine();
            if (foundModel == null && isNullOrBlank(classCode)) {
                System.out.println("Bắt buộc nhập mã lớp!");
            } else {
                break;
            }
        } while (sc.hasNextLine());
        classCode = (foundModel != null && isNullOrBlank(classCode)) ? foundModel.getClassCode() : classCode;


        if (foundModel == null) { //Thêm
            PreparedStatement stm = con.prepareStatement(studentInsertSQL, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, code);
            stm.setString(2, name);
            stm.setString(3, dateOfBirth);
            stm.setString(4, classCode);
            stm.execute();

            long insertedId = 0;
            ResultSet generatedKeys = stm.getGeneratedKeys();
            while (generatedKeys.next()) {
                insertedId = generatedKeys.getLong(1);
            }

            createSubjectsForStudent(insertedId);
        } else {
            PreparedStatement stm = con.prepareStatement(studentUpdateSQL);
            stm.setString(1, code);
            stm.setString(2, name);
            stm.setString(3, dateOfBirth);
            stm.setString(4, classCode);
            stm.setLong(5, foundModel.getId());
            stm.execute();
        }

        System.out.println("==>> " + (foundModel == null ? "Thêm" : "Sửa") + " học sinh thành công <<===");
    }

    static void createSubjectsForStudent(long insertedId) throws SQLException {
        System.out.println("--- Nhập vào thông tin bảng điểm học sinh ---");
        System.out.println("Nhập số năm:");
        int subjectLen = values().length;
        float mark = 0;
        float sum = 0;

        int numberOfYear;
        do {
            numberOfYear = sc.nextInt();
            if (numberOfYear < 3) {
                System.out.println("Số năm bắt buộc nhập khi thêm mới và phải là số >= 3!");
            } else {
                break;
            }
        } while (sc.hasNextInt());

        List<MarkBoardModel> markBoardList = new ArrayList<>();

        for (int year = 0; year < numberOfYear; year++) {
            System.out.println("-- Năm thứ " + (year + 1));

            for (int i = 0; i < subjectLen; i++) {
                System.out.println(values()[i]); //Tên môn học

                while (true) {
                    while (!sc.hasNextFloat()) {
                        System.out.println("Điểm phải ở dạng số thập phân");
                        sc.next();
                    }
                    mark = sc.nextFloat();
                    if (mark < 0 || mark > 10) {
                        System.out.println("Điểm phải nằm trong khoảng 0-10!");
                    } else {
                        break;
                    }
                }
                markBoardList.add(new MarkBoardModel(insertedId, values()[i].name(), mark, year + 1));
                sum += mark;
            }
        }

        float avgMark = Math.round(sum / (numberOfYear * 9) * 100) / 100.0f;

        PreparedStatement avgStm = con.prepareStatement(studentUpdateAvg);
        avgStm.setFloat(1, avgMark);
        avgStm.setLong(2, insertedId);
        avgStm.execute();

        PreparedStatement markStm = con.prepareStatement(markInsertSQL);
        for (MarkBoardModel model : markBoardList) {
            markStm.setLong(1, insertedId);
            markStm.setString(2, model.getSubjectName());
            markStm.setFloat(3, model.getMark());
            markStm.setInt(4, model.getMarkYear());
            markStm.addBatch();
        }
        markStm.executeBatch();
    }

    static void editStudent() throws SQLException {
        Integer foundIndex = getStudent("--- Sửa thông tin học sinh ---");
        if (foundIndex != null) {
            StudentModel student = null;
            PreparedStatement stm = con.prepareStatement(studentFindById);
            stm.setLong(1, foundIndex);
            ResultSet rs = stm.executeQuery();

            while (rs != null && rs.next()) {
                student = new StudentModel();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setCode(rs.getString("code"));
                student.setDateOfBirth(rs.getString("date_of_birth"));
                student.setClassCode(rs.getString("class_code"));
                student.setAvgMark(rs.getFloat("avg_mark"));
            }

            if (student == null) {
                System.out.println("==> Không có học sinh nào <==");
                return;
            }
            createStudent(student);
        }
    }

    static Integer getStudent(String title) throws SQLException {
        System.out.println(title);
        System.out.println("Chọn học sinh bằng ID:");

        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(studentFindAllSQL);

        int i = 0;
        while (rs != null && rs.next()) {
            i++;
            System.out.println(i + ". " + studentToString(rs, i));
        }

        return sc.nextInt();
    }

    private static void deleteStudent() throws SQLException {
        Integer foundIndex = getStudent("--- Xóa thông tin học sinh ---");
        if (foundIndex != null) {
            PreparedStatement stm = con.prepareStatement(studentFindById);
            stm.setLong(1, foundIndex);
            ResultSet rs = stm.executeQuery();

            if (!(rs != null && rs.next())) {
                System.out.println("==> Không có học sinh nào <==");
                return;
            }

            PreparedStatement deleteStm = con.prepareStatement(studentDeleteById);
            deleteStm.setLong(1, foundIndex);
            deleteStm.execute();

            PreparedStatement markStm = con.prepareStatement(markDeleteByStudentId);
            markStm.setLong(1, foundIndex);
            markStm.execute();

            System.out.println("===> Xóa thông tin học sinh thành công <===");
        }
    }

    private static void findStudentByCodeOrName() throws SQLException {
        System.out.println("--- Tìm kiếm học sinh bằng mã hoặc tên ---");
        System.out.println("Nhập mã hoặc tên cần tìm:");
        sc.nextLine();
        String val = sc.nextLine();
        if (isNullOrBlank(val)) {
            System.out.println("==> Không có học sinh nào <==");
            return;
        }

        val = "%" + val + "%";
        PreparedStatement stm = con.prepareStatement(studentFindByCodeOrName);
        stm.setString(1, val);
        stm.setString(2, val);
        ResultSet rs = stm.executeQuery();

        int i = 0;
        while (rs != null && rs.next()) {
            i++;
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(rs.getLong("id")).append(", ")
                    .append("Mã học sinh: ").append(rs.getString("code")).append(", ")
                    .append("Tên học sinh: ").append(rs.getString("name")).append(", ")
                    .append("Ngày sinh: ").append(rs.getString("date_of_birth")).append(", ")
                    .append("Mã lớp: ").append(rs.getString("class_code")).append(", ")
                    .append("Điểm TB: ").append(rs.getFloat("avg_mark")).append(" _ Bảng điểm: [");

            PreparedStatement markStm = con.prepareStatement(markFindByStudentId);
            markStm.setLong(1, rs.getLong("id"));
            ResultSet markRs = markStm.executeQuery();

            int year = 1;
            boolean isHasYearStr = false;
            while (markRs != null && markRs.next()) {
                if (!isHasYearStr) {
                    sb.append("Năm: ").append(year).append(" __ ");
                    isHasYearStr = true;
                }

                if (markRs.getInt("mark_year") != year) {
                    year = markRs.getInt("mark_year");
                    isHasYearStr = false;
                }

                sb.append("{")
                        .append(markRs.getString("subject_name"))
                        .append(" - ")
                        .append(markRs.getFloat("mark"))
                        .append("}, ");
            }

            System.out.println(i + ". " + sb.toString());
        }
        if (i < 1) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    static void findStudentsByClass() throws SQLException {
        System.out.println("--- Liệt kê danh sách học sinh của một lớp ----");
        System.out.println("Nhập mã lớp cần tìm:");
        sc.nextLine();
        String val = sc.nextLine();
        if (isNullOrBlank(val)) {
            System.out.println("==> Không có học sinh nào <==");
            return;
        }

        PreparedStatement stm = con.prepareStatement(studentFindByClass);
        stm.setString(1, val);
        ResultSet rs = stm.executeQuery();

        int i = 0;
        while (rs != null && rs.next()) {
            i++;
            System.out.println(i + ". " + studentToString(rs, i));
        }
        if (i < 1) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    static void findTopTen() throws SQLException {
        System.out.println("--- Tìm kiếm top 10 học sinh xuất sắc nhất ----");
        PreparedStatement stm = con.prepareStatement(studentFindTopTen);
        ResultSet rs = stm.executeQuery();

        int i = 0;
        while (rs != null && rs.next()) {
            i++;
            System.out.println(i + ". " + studentToString(rs, i));
        }
        if (i < 1) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    static void findByAverage() throws SQLException {
        System.out.println("--- Danh sách học sinh có điểm trung binh các môn nhỏ hơn 5 ----");
        PreparedStatement stm = con.prepareStatement(studentFindByAvgMarkLessThanFive);
        ResultSet rs = stm.executeQuery();

        int i = 0;
        while (rs != null && rs.next()) {
            i++;
            System.out.println(i + ". " + studentToString(rs, i));
        }
        if (i < 1) {
            System.out.println("==> Không có học sinh nào <==");
        }
    }

    static String studentToString(ResultSet rs, int i) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(rs.getLong("id")).append(", ")
                .append("Mã học sinh: ").append(rs.getString("code")).append(", ")
                .append("Tên học sinh: ").append(rs.getString("name")).append(", ")
                .append("Ngày sinh: ").append(rs.getString("date_of_birth")).append(", ")
                .append("Mã lớp: ").append(rs.getString("class_code")).append(", ")
                .append("Điểm TB: ").append(rs.getFloat("avg_mark")).append(", ")
                .append("Xếp hạng: ").append(i);
        return sb.toString();
    }
}